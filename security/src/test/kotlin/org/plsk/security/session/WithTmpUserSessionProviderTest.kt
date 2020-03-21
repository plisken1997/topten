package org.plsk.security.session

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import org.plsk.security.*
import org.plsk.security.accessToken.AccessToken
import org.plsk.security.accessToken.AccessTokenError
import org.plsk.security.accessToken.AccessTokenProvider
import org.plsk.security.accessToken.TokenNotFound
import org.plsk.user.AppUser
import org.plsk.user.User
import org.plsk.user.dao.UserQueryHandler

class WithTmpUserSessionProviderTest : WordSpec() {

  init {

    "authentication provider with tmp user" should {

      "create a session for an existing user" {
        runBlocking {
          val authUserQuery = AuthUser("5bfa9d6b-04b2-4bce-9b5d-ad546ada55e1", "user-ok", "1234", emptyList())
          val res = sessionProvider.createSession(authUserQuery)

          res shouldBe Right(Session(testAccessToken, DataReaderTestHelper.user))
        }
      }

      "fail to create a session when the user does not exists" {
        runBlocking {
          val authUserQuery = AuthUser("5bfa9d6b-04b2-4bce-9b5d-ad546ada55e1", "unknown-user", "1234", emptyList())
          val res = sessionProvider.createSession(authUserQuery)

          res shouldBe Left(UserNotFound(authUserQuery))
        }
      }

      "validate an existing session" {
        runBlocking {
          val session = sessionProvider.validateSession(testAccessToken)
          session shouldBe Right(Session(testAccessToken, DataReaderTestHelper.user))
        }
      }

      "fail to validate an existing session when the token is not found" {
        runBlocking {
          val session = sessionProvider.validateSession(AccessToken("unknown-token"))
          session shouldBe Left(GetAccessTokenError("unknown-token"))
        }
      }
    }

  }

  val testAccessToken = AccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")

  val user = AppUser("5bfa9d6b-04b2-4bce-9b5d-ad546ada55e1", "user-ok")

  val sessionProvider: SessionProvider<AuthenticationFailure> = {
    val accessTokenProvider: AccessTokenProvider = object : AccessTokenProvider {
      override suspend fun getUserFromSession(accessToken: AccessToken): Either<AccessTokenError, User> =
          Either.cond<AccessTokenError, User>(accessToken == testAccessToken, { user }, { TokenNotFound(accessToken) })

      override suspend fun generateToken(user: User): Either<AccessTokenError, AccessToken> = TODO("not implemented")
      override suspend fun getAccessToken(user: User): Either<AccessTokenError, AccessToken> = Right(testAccessToken)
    }

    WithTmpUserSessionProvider(UserQueryHandler(DataReaderTestHelper.userReader), accessTokenProvider)
  }()
}