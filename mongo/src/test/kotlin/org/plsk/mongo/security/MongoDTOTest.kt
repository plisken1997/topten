package org.plsk.mongo.security

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.security.accessToken.AccessToken
import java.time.Instant

class MongoDTOTest: WordSpec() {

  init {

    "mongo access token DTO" should {

      "be created from a security.AccessToken" {
        AccessToken(accessToken).toDTO(now) shouldBe MongoAccessToken(accessToken, now)
      }

    }

  }

  val now = Instant.now().toEpochMilli()
  val accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
}
