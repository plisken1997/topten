package org.plsk.security.accessToken

import arrow.core.Left
import arrow.core.Right
import io.kotlintest.matchers.collections.shouldHaveAtLeastSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.DataReader
import org.plsk.core.dao.QueryFilter
import org.plsk.core.id.UUIDGen
import org.plsk.user.AppUser
import org.plsk.user.User

class JwtsTokenProviderTest: WordSpec() {

  init {

    "jwts JWT provider" should {

      "generate the expected token" {
        val jwt = jwtProvider.getAccessToken(user)
        jwt.isRight() shouldBe true
      }

      "store the generated access token" {
        accessTokenStore = listOf<UserAccessToken>()
        val res = jwtProvider.getAccessToken(user)

        accessTokenStore shouldHaveAtLeastSize 1
        res shouldBe Right(accessTokenStore.first().token)
      }

      "find the access token owner" {
        val userOr = jwtProvider.getUser(expectedToken)
        userOr shouldBe Right(user)
      }

      "reject an expired access token" {
        val userOr = jwtProvider.getUser(expiredToken)
        userOr shouldBe Left(ExpiredToken(expiredToken))
      }

      "not find the access token" {
        val userOr = jwtProvider.getUser(unknownToken)
        userOr shouldBe Left(TokenNotFound(unknownToken))
      }

      "not find the access token owner" {
        val userOr = jwtProvider.getUser(unknownUser)
        userOr shouldBe Left(UserNotFound(unknownUser))
      }
    }

  }

  val expectedToken = AccessToken("token-ok")
  val expiredToken = AccessToken("expired-token")
  val unknownToken = AccessToken("unknown-token")
  val unknownUser = AccessToken("unknown-user")

  val user = AppUser("5bfa9d6b-04b2-4bce-9b5d-ad546ada55e1", "user-ok")

  var accessTokenStore: List<UserAccessToken> = listOf<UserAccessToken>()

  val accessTokenRepository = object: AccessTokenRepository {
    override fun find(id: String): UserAccessToken? =
        when(id) {
          expectedToken.token -> UserAccessToken(user.id, expectedToken, FakeClock.plusDays(1L))
          expiredToken.token -> UserAccessToken(user.id, expiredToken, FakeClock.now())
          unknownUser.token -> UserAccessToken("unknown-user", unknownUser, FakeClock.plusDays(1L))
          else -> null
        }

    override fun findAll(filter: Iterable<QueryFilter>): List<UserAccessToken> = TODO("not implemented")
    override fun update(data: UserAccessToken): String =  TODO("not implemented")
    override fun store(data: UserAccessToken): String {
      accessTokenStore = accessTokenStore + listOf(data)
      return UUIDGen().fromString(data.token.token).toString()
    }
  }

  val userReader: DataReader<User, String> = object: DataReader<User, String>{
    override fun findAll(filter: Iterable<QueryFilter>): List<User> = TODO("not implemented")
    override fun find(id: String): User? =
      if (id == user.id) user
      else null
  }

  val appId = "org.plsktopten"

  val jwtProvider = JwtsTokenProvider(accessTokenRepository, userReader, FakeClock, appId)
}
