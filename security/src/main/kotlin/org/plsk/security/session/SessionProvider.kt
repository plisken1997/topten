package org.plsk.security.session

import arrow.core.Either
import org.plsk.security.AuthUser
import org.plsk.security.Session

interface SessionProvider<AuthenticationFailure> {
  fun createSession(user: AuthUser): Either<AuthenticationFailure, Session>
}
