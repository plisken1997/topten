package org.plsk.security.accessToken

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.core.dao.QueryFilter
import org.plsk.core.id.UUIDGen
import org.plsk.user.AppUser

class JwtsTokenProviderTest: WordSpec() {

  init {

    "jwts JWT provider" should {

      "generate the expected token" {
        val user = AppUser("5bfa9d6b-04b2-4bce-9b5d-ad546ada55e1", "user-ok")
        val jwt = jwtProvider.getAccessToken(user)
        jwt.isRight() shouldBe true
      }

    }

  }

  var accessTokenStore: List<AccessToken> = listOf<AccessToken>()

  val accessTokenRepository = object: AccessTokenRepository {
    override fun find(id: String): AccessToken? = TODO("not implemented")
    override fun findAll(filter: Iterable<QueryFilter>): List<AccessToken> = TODO("not implemented")
    override fun update(data: AccessToken): String =  TODO("not implemented")
    override fun store(data: AccessToken): String {
      accessTokenStore = accessTokenStore + listOf(data)
      return UUIDGen().fromString(data.token).toString()
    }
  }

  val appId = "org.plsktopten"
  val appSecret = "this is a secret"

  val jwtProvider = JwtsTokenProvider(accessTokenRepository, appId, appSecret)
}
