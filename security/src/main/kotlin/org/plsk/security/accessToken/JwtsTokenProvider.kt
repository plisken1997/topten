package org.plsk.security.accessToken

import arrow.core.Either
import arrow.core.Try
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.plsk.user.User
import io.jsonwebtoken.security.Keys

data class GenerateJWTError(override val error: String): AccessTokenError

class JwtsTokenProvider(private val accessTokenRepository: AccessTokenRepository, private val appId: String, private val appSecret: String): AccessTokenProvider {

  override fun getAccessToken(user: User): Either<AccessTokenError, AccessToken> =
      generateToken(user)
          .map {
            accessToken ->
            accessTokenRepository.store(accessToken)
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

}
