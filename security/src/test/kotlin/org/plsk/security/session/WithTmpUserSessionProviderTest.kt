package org.plsk.security.session

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.getOrElse
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.core.dao.QueryFilter
import org.plsk.core.id.UUIDGen
import org.plsk.security.*
import org.plsk.security.accessToken.AccessToken
import org.plsk.security.accessToken.AccessTokenError
import org.plsk.security.accessToken.AccessTokenProvider
import org.plsk.security.accessToken.AccessTokenRepository
import org.plsk.user.User
import org.plsk.user.dao.UserQueryHandler
import java.util.*

class WithTmpUserSessionProviderTest : WordSpec() {

  init {

    "authentication provider with tmp user" should {

      "create a session for an existing user" {
        val authUserQuery = AuthUser("5bfa9d6b-04b2-4bce-9b5d-ad546ada55e1", "user-ok", "1234", emptyList())
        val res = sessionProvider.createSession(authUserQuery)

        res.getOrElse { throw Exception("authentication should be a Right(Session))") } shouldBe Session(accessToken, DataReaderTestHelper.user)
      }

      "fail to create a session when the user does not exists" {
        val authUserQuery = AuthUser("5bfa9d6b-04b2-4bce-9b5d-ad546ada55e1", "unknown-user", "1234", emptyList())
        val res = sessionProvider.createSession(authUserQuery)

        res shouldBe Left(UserNotFound(authUserQuery))
      }
    }

  }

  val accessToken = AccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")

  val sessionProvider: SessionProvider<AuthenticationFailure> = {
    val accessTokenProvider: AccessTokenProvider = object: AccessTokenProvider {
      override fun getAccessToken(user: User): Either<AccessTokenError, AccessToken> = Right(accessToken)
    }
    val accessTokenRepository = object: AccessTokenRepository {
      override fun find(id: UUID): AccessToken? = TODO("not implemented")
      override fun findAll(filter: Iterable<QueryFilter>): List<AccessToken> = TODO("not implemented")
      override fun update(data: AccessToken): UUID =  TODO("not implemented")
      override fun store(data: AccessToken): UUID {
        return UUIDGen().fromString(data.token)
      }
    }
    WithTmpUserSessionProvider(UserQueryHandler(DataReaderTestHelper.userReader), accessTokenProvider, accessTokenRepository)
  }()
}