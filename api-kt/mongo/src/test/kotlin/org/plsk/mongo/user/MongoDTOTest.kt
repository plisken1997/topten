package org.plsk.mongo.user

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.user.AppUser
import org.plsk.user.UnknownUser

class MongoDTOTest: WordSpec() {

  init {
      "mongo user dto" should {

        "convert an unknown user to a mongo dto object" {
          UnknownUser("3c5c2d0f-5fd3-4852-9493-a5025d6fd7d9").toDTO() shouldBe MongoUser("3c5c2d0f-5fd3-4852-9493-a5025d6fd7d9", MongoUser.TYPE_UNKNOWN)
          MongoUser("3c5c2d0f-5fd3-4852-9493-a5025d6fd7d9", MongoUser.TYPE_UNKNOWN).toModel() shouldBe UnknownUser("3c5c2d0f-5fd3-4852-9493-a5025d6fd7d9")
        }

        "convert an app user to a mongo dto object" {
          AppUser("3c5c2d0f-5fd3-4852-9493-a5025d6fd7d9", "user 1").toDTO() shouldBe MongoUser("3c5c2d0f-5fd3-4852-9493-a5025d6fd7d9", MongoUser.TYPE_APPUSER, "user 1")
          MongoUser("3c5c2d0f-5fd3-4852-9493-a5025d6fd7d9", MongoUser.TYPE_APPUSER, "user 1").toModel() shouldBe AppUser("3c5c2d0f-5fd3-4852-9493-a5025d6fd7d9", "user 1")
        }

      }
  }

}
