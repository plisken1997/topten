package org.plsk.security.accessToken

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
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

  override suspend fun getAccessToken(user: User): Either<AccessTokenError, AccessToken> =
      generateToken(user)
          .fold (
              {err -> Left(err) },
              { accessToken ->
                accessTokenRepository.store(UserAccessToken(user.id, accessToken, clock.plusDays(30)))
                Right(accessToken)
              }
          )

  // @todo should be in a dedicated service
  override suspend fun generateToken(user: User): Either<AccessTokenError, AccessToken> {
    val header : Map<String, String> = mapOf(
        Pair("iss", appId),
        Pair("sub", "LoginRequest"),
        Pair("userName", user.name)
    )

    val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    val res: Either<Throwable, String> =  Either.Right(
        Jwts.builder()
            .setPayload(user.id)
            .setHeader(header)
            .signWith(key)
            .compact()
    )

    return res.bimap(
        {err -> GenerateJWTError(err.localizedMessage)},
        {ok -> AccessToken(ok)}
    )
  }

  override suspend fun getUserFromSession(accessToken: AccessToken): Either<AccessTokenError, User> {
    val token: UserAccessToken = accessTokenRepository.find(accessToken.token) ?: return Left(TokenNotFound(accessToken))

    if (!token.isValid(clock)) return Left(ExpiredToken(accessToken))

    val user: User? = userReader.find(token.userId)

   return  Either.cond<AccessTokenError, User>(user != null, { user!! }, { UserNotFound(accessToken) })
  }
}
