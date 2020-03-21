package org.plsk.security.session

import arrow.core.Either
import arrow.core.Left
import org.plsk.security.*
import org.plsk.security.accessToken.AccessToken
import org.plsk.security.accessToken.AccessTokenProvider
import org.plsk.user.dao.IdentifyUser
import org.plsk.user.dao.UserQueryHandler

class WithTmpUserSessionProvider(
    private val userQueryHandler: UserQueryHandler,
    private val accessTokenProvider: AccessTokenProvider): SessionProvider<AuthenticationFailure> {

  override suspend fun createSession(user: AuthUser): Either<AuthenticationFailure, Session> {
    val users = userQueryHandler.handle(IdentifyUser(user.name, user.password, user.grants))

    return if (users.size != 1) {
      Left(UserNotFound(user))
    } else {
      val result = users.content.first()
      accessTokenProvider.getAccessToken((result)).bimap(
          { err -> GetAccessTokenError(err.error) },
          { Session(it, result) }
      )
    }
  }

  override suspend fun validateSession(accessToken: AccessToken): Either<AuthenticationFailure, Session> =
      accessTokenProvider.getUserFromSession(accessToken).bimap(
          { GetAccessTokenError(it.error) },
          { user -> Session(accessToken, user) }
      )
}