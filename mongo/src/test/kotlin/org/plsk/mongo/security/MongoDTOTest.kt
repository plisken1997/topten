package org.plsk.mongo.security

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.core.clock.FakeClock
import org.plsk.security.accessToken.AccessToken
import org.plsk.security.accessToken.UserAccessToken

class MongoDTOTest: WordSpec() {

  init {

    "mongo access token DTO" should {

      "be created from a security.AccessToken" {
        UserAccessToken(userId, accessToken, now).toDTO() shouldBe MongoAccessToken(userId, accessToken.token, now.timestamp())
      }

      "be transformed to a model object" {
        MongoAccessToken(userId, accessToken.token, now.timestamp()).toModel() shouldBe UserAccessToken(userId, accessToken, now)
      }

    }

  }

  val userId = "fc417f01-9d4d-42e0-908f-fb4da2506c2a"
  val now = FakeClock.now()
  val accessToken = AccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
}
