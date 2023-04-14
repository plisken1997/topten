package org.plsk.security.accessToken

import arrow.core.Either
import org.plsk.user.User

interface AccessTokenError {
  val error: String
}

data class AccessToken(val token: String)

interface AccessTokenProvider {
  suspend fun getAccessToken(user: User): Either<AccessTokenError, AccessToken>
  suspend fun generateToken(user: User): Either<AccessTokenError, AccessToken>
  suspend fun getUserFromSession(accessToken: AccessToken): Either<AccessTokenError, User>
}
