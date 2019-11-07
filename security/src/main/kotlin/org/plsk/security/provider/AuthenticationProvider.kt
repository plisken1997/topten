package org.plsk.security.provider

import arrow.core.Either
import org.plsk.security.AuthUser
import org.plsk.security.Session

interface AuthenticationProvider<AuthenticationFailure> {
  fun authenticate(user: AuthUser): Either<AuthenticationFailure, Session>
}
