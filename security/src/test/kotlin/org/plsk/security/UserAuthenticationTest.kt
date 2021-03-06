package org.plsk.security

import arrow.core.Either
import arrow.core.Right
import arrow.core.getOrElse
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import org.plsk.core.command.CommandHandler
import org.plsk.security.accessToken.AccessToken
import org.plsk.security.accessToken.AccessTokenError
import org.plsk.security.accessToken.AccessTokenProvider
import org.plsk.security.session.SessionProvider
import org.plsk.security.session.UserSessionProvider
import org.plsk.user.User
import org.plsk.user.dao.UserQueryHandler
import org.plsk.user.tmpUser.CreateTmpUser

class UserAuthenticationTest : WordSpec() {

  init {

    "withUnknownUserAuthentication" should {

      "create a tmp user session when the user is unknown" {
        runBlocking {
          val unknownAuth = CreateGuestUserSession("127.1")
          val sessionOr = withUnknownUserAuthentication.authenticate(unknownAuth)
          sessionOr.isRight() shouldBe true
          val session = sessionOr.getOrElse { throw Exception("unexpected") }

          session shouldBe Session(accessToken, DataReaderTestHelper.user)
          accessTokenStore.find { it == session.accessToken }
        }
      }

      "fail to create a session when the user is not found" {
        runBlocking {
          val unknownAuth = CreateGuestUserSession("bad-ip")
          val exception = shouldThrow<Exception> {
            withUnknownUserAuthentication.authenticate(unknownAuth)
          }
          exception.message shouldBe "fail to create tmp user"
        }
      }

    }

  }

  val accessToken = AccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")

  val createUser: CommandHandler<CreateTmpUser, User> = object : CommandHandler<CreateTmpUser, User> {
    override suspend fun handle(command: CreateTmpUser): User =
        if (command.ip == "bad-ip") {
          throw Exception("fail to create tmp user")
        } else {
          DataReaderTestHelper.user
        }
  }

  var accessTokenStore: List<AccessToken> = listOf<AccessToken>()

  val sessionProvider: SessionProvider<AuthenticationFailure> = {
    val accessTokenProvider: AccessTokenProvider = object : AccessTokenProvider {
      override suspend fun getUserFromSession(accessToken: AccessToken): Either<AccessTokenError, User> = TODO("not implemented")
      override suspend fun generateToken(user: User): Either<AccessTokenError, AccessToken> = TODO("NOT IMPLEMENTED")

      override suspend fun getAccessToken(user: User): Either<AccessTokenError, AccessToken> {
        accessTokenStore = accessTokenStore + listOf(accessToken)
        return Right(accessToken)
      }
    }

    UserSessionProvider(UserQueryHandler(DataReaderTestHelper.userReader), accessTokenProvider)
  }()

  val withUnknownUserAuthentication = UserAuthentication(createUser, sessionProvider)
}
