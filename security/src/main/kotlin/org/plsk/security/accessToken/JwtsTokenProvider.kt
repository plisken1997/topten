package org.plsk.security.accessToken

import arrow.core.Either
import arrow.core.Try
import arrow.core.flatMap
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.plsk.user.User
import io.jsonwebtoken.security.Keys
import org.plsk.core.clock.Clock
import org.plsk.core.dao.DataReader

data class GenerateJWTError(override val error: String): AccessTokenError

data class TokenNotFound(val token: AccessToken): AccessTokenError {
  override val error = "token [$token] not found"
}

data class UserNotFound(val token: AccessToken): AccessTokenError {
  override val error = "user not found for authentication [$token]"
}

data class ExpiredToken(val token: AccessToken): AccessTokenError {
  override val error = "Expired token [$token]"
}

class JwtsTokenProvider(
  private val accessTokenRepository: AccessTokenRepository,
  private val userReader: DataReader<User, String>,
  private val clock: Clock,
  private val appId: String
): AccessTokenProvider {

  override fun getAccessToken(user: User): Either<AccessTokenError, AccessToken> =
      generateToken(user)
          .map {
            accessToken ->
            accessTokenRepository.store(UserAccessToken(user.id, accessToken, clock.plusDays(30)))
            accessToken
          }


  // @todo should be in a dedicated service
  override fun generateToken(user: User): Either<AccessTokenError, AccessToken> {
    val header : Map<String, String> = mapOf(
        Pair("iss", appId),
        Pair("sub", "LoginRequest"),
        Pair("userName", user.name)
    )

    val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    return  Try {
        Jwts.builder()
            .setPayload(user.id)
            .setHeader(header)
            .signWith(key)
            .compact()
      }.toEither { err -> GenerateJWTError(err.localizedMessage) }.map { AccessToken(it)}
  }

  override fun getUser(accessToken: AccessToken): Either<AccessTokenError, User> {
    val token: UserAccessToken? = accessTokenRepository.find(accessToken.token)

    return Either.cond<AccessTokenError, UserAccessToken>(token != null, { token!!}, { TokenNotFound(accessToken)})
        .flatMap {
          userAccessToken ->
          Either.cond<AccessTokenError, String>(userAccessToken.isValid(clock), { userAccessToken.userId }, { ExpiredToken(accessToken) })
              .flatMap {
                userId ->
                  val user = userReader.find(userId)
                  Either.cond<AccessTokenError, User>(user != null, { user!! }, { UserNotFound(accessToken) })
              }
        }
  }
}
