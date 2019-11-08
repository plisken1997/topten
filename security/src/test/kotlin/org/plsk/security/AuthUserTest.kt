package org.plsk.security

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.user.AppUser

class AuthUserTest: WordSpec() {

  init {

    "auth user" should {

      "yield a tmp user with empty password" {
        val id = "d5e6cdb4-8239-4060-9e40-39c12569a38c"
        val name = "tmp user 1"
        AuthUser.TmpAuthUser(AppUser(id, name)) shouldBe AuthUser(id, "tmp user 1", "", listOf("topten"))
      }

    }

  }
}