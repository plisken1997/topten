package org.plsk.security

import arrow.core.Either
import arrow.core.Right
import arrow.core.getOrElse
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import org.plsk.core.command.CommandHandler
import org.plsk.core.dao.QueryFilter
import org.plsk.core.id.UUIDGen
import org.plsk.security.accessToken.AccessToken
import org.plsk.security.accessToken.AccessTokenError
import org.plsk.security.accessToken.AccessTokenProvider
import org.plsk.security.accessToken.AccessTokenRepository
import org.plsk.security.session.SessionProvider
import org.plsk.security.session.WithTmpUserSessionProvider
import org.plsk.user.User
import org.plsk.user.dao.UserQueryHandler
import org.plsk.user.tmpUser.CreateTmpUser
import java.util.*

class WithUnknownUserAuthenticationTest: WordSpec() {

  init {

    "withUnknownUserAuthentication" should {

      "create a tmp user session when the user is unknown" {
        val unknownAuth = UnknownUserRequest("127.1")
        val sessionOr = withUnknownUserAuthentication.authenticate(unknownAuth)
        sessionOr.isRight() shouldBe true
        val session = sessionOr.getOrElse{ throw Exception("unexpected")  }

        session shouldBe Session(accessToken, DataReaderTestHelper.user)
        accessTokenStore.find{ it == session.accessToken }
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

  var accessTokenStore: List<AccessToken> = listOf<AccessToken>()

  val sessionProvider: SessionProvider<AuthenticationFailure> = {
    val accessTokenProvider: AccessTokenProvider = object: AccessTokenProvider {
      override fun getAccessToken(user: User): Either<AccessTokenError, AccessToken> = Right(accessToken)
    }
    val accessTokenRepository = object: AccessTokenRepository {
      override fun find(id: String): AccessToken? = TODO("not implemented")
      override fun findAll(filter: Iterable<QueryFilter>): List<AccessToken> = TODO("not implemented")
      override fun update(data: AccessToken): String =  TODO("not implemented")
      override fun store(data: AccessToken): String {
        accessTokenStore = accessTokenStore + listOf(data)
        return UUIDGen().fromString(data.token).toString()
      }
    }
    WithTmpUserSessionProvider(UserQueryHandler(DataReaderTestHelper.userReader), accessTokenProvider, accessTokenRepository)
  }()

  val withUnknownUserAuthentication = WithUnknownUserAuthentication(createUser, sessionProvider)
}
