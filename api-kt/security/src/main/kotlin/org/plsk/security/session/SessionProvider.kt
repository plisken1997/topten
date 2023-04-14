package org.plsk.security.session

import arrow.core.Either
import org.plsk.security.AuthUser
import org.plsk.security.Session
import org.plsk.security.accessToken.AccessToken

interface SessionProvider<AuthenticationFailure> {
  suspend fun createSession(user: AuthUser): Either<AuthenticationFailure, Session>
  suspend fun validateSession(accessToken: AccessToken): Either<AuthenticationFailure, Session>
}
