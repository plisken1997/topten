package org.plsk.security

import arrow.core.Either
import org.plsk.user.User

data class Session(val accessToken: String, val user: User)

interface Authentication<AuthenticationFailure> {
  fun validate(token: AuthenticationRequest): Either<AuthenticationFailure, Session>
}
