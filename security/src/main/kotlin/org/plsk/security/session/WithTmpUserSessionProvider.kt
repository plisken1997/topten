package org.plsk.security.session

import arrow.core.Either
import arrow.core.Left
import org.plsk.security.*
import org.plsk.security.accessToken.AccessTokenProvider
import org.plsk.security.accessToken.AccessTokenRepository
import org.plsk.user.dao.IdentifyUser
import org.plsk.user.dao.UserQueryHandler

class WithTmpUserSessionProvider(
    private val userQueryHandler: UserQueryHandler,
    private val accessTokenProvider: AccessTokenProvider,
    private val accessTokenRepository: AccessTokenRepository): SessionProvider<AuthenticationFailure> {

  override fun createSession(user: AuthUser): Either<AuthenticationFailure, Session> {
    val users = userQueryHandler.handle(IdentifyUser(user.name, user.password, user.grants))

    return if (users.size != 1) {
      Left(UserNotFound(user))
    } else {
      val result = users.content.first()
      accessTokenProvider.getAccessToken((result)).bimap(
          {err -> GetAccessTokenError(err.error) },
          {accessToken ->
            accessTokenRepository.store(accessToken)
            Session(accessToken, result)
          }
      )
    }
  }

}