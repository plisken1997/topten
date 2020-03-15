package org.plsk.security

import arrow.core.Either
import arrow.core.Right
import arrow.core.flatMap
import org.plsk.core.command.CommandHandler
import org.plsk.security.accessToken.AccessToken
import org.plsk.security.session.SessionProvider
import org.plsk.user.User
import org.plsk.user.tmpUser.CreateTmpUser

sealed class AuthenticationFailure {
  abstract val error: String
}

data class UserNotFound(val authUser: AuthUser): AuthenticationFailure() {
  override val error = "cound not find user ${authUser.name}"
}

object AccessTokenNotFound: AuthenticationFailure() {
  override val error = "could not find user session"
}

data class GetAccessTokenError(override val error: String): AuthenticationFailure()

class WithUnknownUserAuthentication(
    private val createUser: CommandHandler<CreateTmpUser, User>,
    private val sessionProvider: SessionProvider<AuthenticationFailure>
  ): Authentication<AuthenticationFailure> {

  override suspend fun authenticate(token: AuthenticationRequest): Either<AuthenticationFailure, Session> =
    when (token) {
      is CreateGuestUserSession ->
        createTmpUser(token.token)
            .flatMap{ sessionProvider.createSession(it) }
      is AccessTokenRequest -> sessionProvider.validateSession(AccessToken(token.token))
    }

  private suspend fun createTmpUser(ipAddress: String): Either<AuthenticationFailure, AuthUser> {
    val user = CreateTmpUser(ipAddress)
    val created = createUser.handle(user)
    return Right(AuthUser.TmpAuthUser(created))
  }
}