package org.plsk.security

import arrow.core.Either
import org.plsk.security.accessToken.AccessToken
import org.plsk.user.User

data class Session(val accessToken: AccessToken, val user: User)

interface Authentication<AuthenticationFailure> {
  suspend fun authenticate(token: AuthenticationRequest): Either<AuthenticationFailure, Session>
}
