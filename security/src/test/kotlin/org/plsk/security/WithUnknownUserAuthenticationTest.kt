package org.plsk.security

import arrow.core.Either
import arrow.core.Right
import arrow.core.getOrElse
import io.kotlintest.fail
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.core.command.CommandHandler
import org.plsk.security.provider.AuthenticationProvider
import org.plsk.user.AppUser
import org.plsk.user.tmpUser.CreateTmpUser
import java.util.*

class WithUnknownUserAuthenticationTest: WordSpec() {

  init {

    "withUnknownUserAuthentication" should {

      "create a tmp user session when the user is unknown" {
        val unknownAuth = UnknownUser("127.1")
        val session = withUnknownUserAuthentication.validate(unknownAuth)
        session.isRight() shouldBe true
        session.getOrElse { throw Exception("unexpected")  } shouldBe Session(accessToken, AppUser(expectedId.toString(), ""))
      }

      "fail to create a user when an unexpected CreateUser error occurs" {
        fail("todo")
      }

      "fail to create a session when an unexpected AuthProvider error occurs" {
        fail("todo")
      }

    }

  }

  val accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
  val expectedId = UUID.fromString("d5e6cdb4-8239-4060-9e40-39c12569a38c")

  val createUser: CommandHandler<CreateTmpUser, UUID> = object : CommandHandler<CreateTmpUser, UUID> {
    override fun handle(command: CreateTmpUser): UUID = expectedId
  }
  val authenticationProvider: AuthenticationProvider<AuthenticationFailure> = object : AuthenticationProvider<AuthenticationFailure>{
    override fun authenticate(user: AuthUser): Either<AuthenticationFailure, Session> = Right(Session(accessToken, AppUser(expectedId.toString(), "")))
  }

  val withUnknownUserAuthentication = WithUnknownUserAuthentication(createUser, authenticationProvider)
}
