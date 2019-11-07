package org.plsk.security.provider

import arrow.core.Either
import arrow.core.Left
import org.plsk.security.*
import org.plsk.user.dao.IdentifyUser
import org.plsk.user.dao.UserQueryHandler

class WithTmpUserAuthenticationProvider(
    private val userQueryHandler: UserQueryHandler,
    private val accessTokenProvider: AccessTokenProvider): AuthenticationProvider<AuthenticationFailure> {

  override fun authenticate(user: AuthUser): Either<AuthenticationFailure, Session> {
    val users = userQueryHandler.handle(IdentifyUser(user.name, user.password, user.grants))

    return if (users.size != 1) {
      Left(UserNotFound(user))
    } else {
      val result = users.content.first()
      accessTokenProvider.getAccessToken((result)).bimap(
          {err -> GetAccessTokenError(err.error) },
          {accessToken -> Session(accessToken, result)}
      )
    }
  }

}