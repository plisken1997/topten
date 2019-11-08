package org.plsk.security

import arrow.core.Either
import arrow.core.Right
import arrow.core.getOrElse
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import org.plsk.core.command.CommandHandler
import org.plsk.security.accessToken.AccessToken
import org.plsk.security.accessToken.AccessTokenError
import org.plsk.security.accessToken.AccessTokenProvider
import org.plsk.security.session.SessionProvider
import org.plsk.security.session.WithTmpUserSessionProvider
import org.plsk.user.User
import org.plsk.user.dao.UserQueryHandler
import org.plsk.user.tmpUser.CreateTmpUser

class WithUnknownUserAuthenticationTest: WordSpec() {

  init {

    "withUnknownUserAuthentication" should {

      "create a tmp user session when the user is unknown" {
        val unknownAuth = UnknownUserRequest("127.1")
        val session = withUnknownUserAuthentication.authenticate(unknownAuth)
        session.isRight() shouldBe true
        session.getOrElse { throw Exception("unexpected")  } shouldBe Session(accessToken, DataReaderTestHelper.user)
      }

      "fail to create a session when the user is not found" {
        val unknownAuth = UnknownUserRequest("bad-ip")
        val exception = shouldThrow<Exception> {
          withUnknownUserAuthentication.authenticate(unknownAuth)
        }
        exception.message shouldBe "fail to create tmp user"
      }

    }

  }

  val accessToken = AccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")

  val createUser: CommandHandler<CreateTmpUser, User> = object : CommandHandler<CreateTmpUser, User> {
    override fun handle(command: CreateTmpUser): User =
      if (command.ip == "bad-ip") {
        throw Exception("fail to create tmp user")
      } else {
        DataReaderTestHelper.user
      }
  }
  val sessionProvider: SessionProvider<AuthenticationFailure> = {
    val accessTokenProvider: AccessTokenProvider = object: AccessTokenProvider {
      override fun getAccessToken(user: User): Either<AccessTokenError, AccessToken> = Right(accessToken)
    }
    WithTmpUserSessionProvider(UserQueryHandler(DataReaderTestHelper.userReader), accessTokenProvider)
  }()

  val withUnknownUserAuthentication = WithUnknownUserAuthentication(createUser, sessionProvider)
}
