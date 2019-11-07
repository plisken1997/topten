package org.plsk.security

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class AuthUserTest: WordSpec() {

  init {

    "auth user" should {

      "yield a tmp user with empty password" {
        val id = "d5e6cdb4-8239-4060-9e40-39c12569a38c"
        AuthUser.TmpAuthUser(id) shouldBe AuthUser(id, "", "", listOf("topten"))
      }

    }

  }
}