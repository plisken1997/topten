package org.plsk.security

import arrow.core.Either
import arrow.core.Right
import arrow.core.getOrElse
import io.kotlintest.fail
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import org.plsk.core.command.CommandHandler
import org.plsk.security.provider.AccessTokenError
import org.plsk.security.provider.AccessTokenProvider
import org.plsk.security.provider.AuthenticationProvider
import org.plsk.security.provider.WithTmpUserAuthenticationProvider
import org.plsk.user.User
import org.plsk.user.dao.UserQueryHandler
import org.plsk.user.tmpUser.CreateTmpUser

class WithUnknownUserAuthenticationTest: WordSpec() {

  init {

    "withUnknownUserAuthentication" should {

      "create a tmp user session when the user is unknown" {
        val unknownAuth = UnknownUser("127.1")
        val session = withUnknownUserAuthentication.validate(unknownAuth)
        session.isRight() shouldBe true
        session.getOrElse { throw Exception("unexpected")  } shouldBe Session(accessToken, DataReaderTestHelper.user)
      }

      "fail to create a session when the user is not found" {
        val unknownAuth = UnknownUser("bad-ip")
        val exception = shouldThrow<Exception> {
          withUnknownUserAuthentication.validate(unknownAuth)
        }
        exception.message shouldBe "fail to create tmp user"
      }

    }

  }

  val accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

  val createUser: CommandHandler<CreateTmpUser, User> = object : CommandHandler<CreateTmpUser, User> {
    override fun handle(command: CreateTmpUser): User =
      if (command.ip == "bad-ip") {
        throw Exception("fail to create tmp user")
      } else {
        DataReaderTestHelper.user
      }
  }
  val authenticationProvider: AuthenticationProvider<AuthenticationFailure> = {
    val accessTokenProvider: AccessTokenProvider = object: AccessTokenProvider{
      override fun getAccessToken(user: User): Either<AccessTokenError, String> = Right(accessToken)
    }
    WithTmpUserAuthenticationProvider(UserQueryHandler(DataReaderTestHelper.userReader), accessTokenProvider)
  }()

  val withUnknownUserAuthentication = WithUnknownUserAuthentication(createUser, authenticationProvider)
}
