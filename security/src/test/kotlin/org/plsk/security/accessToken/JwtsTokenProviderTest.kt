package org.plsk.security.accessToken

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
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

  val appId = "org.plsktopten"
  val appSecret = "this is a secret"

  val jwtProvider = JwtsTokenProvider(appId, appSecret)
}
