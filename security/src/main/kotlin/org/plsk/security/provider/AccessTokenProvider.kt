package org.plsk.security.provider

import arrow.core.Either
import org.plsk.user.User

interface AccessTokenError {
  val error: String
}

interface AccessTokenProvider {

  fun getAccessToken(user: User): Either<AccessTokenError, String>
}