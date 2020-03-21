package org.plsk.user.dao

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import org.plsk.core.dao.QueryResult
import org.plsk.user.User

class UserQueryHandlerTest : WordSpec() {

  init {

    "user query handler" should {

      "return the expected user" {
        runBlocking {

          val res = userQueryHandler.handle(IdentifyUser("user-ok", "1234", emptySet()))
          res shouldBe QueryResult(1, listOf(DataReaderTestHelper.user))
        }
      }

      "return an empty list when the user is not found" {
        runBlocking {
          val res = userQueryHandler.handle(IdentifyUser("user-notfound", "1234", emptySet()))
          res shouldBe QueryResult(0, emptyList<User>())
        }
      }

      "throw an exception when more thant one user is found" {
        runBlocking {
          val exception = shouldThrow<Exception> {
            userQueryHandler.handle(IdentifyUser("user-multiple", "1234", emptySet()))
          }
          exception.message shouldBe "more than 1 user found"
        }
      }

    }

  }

  val userQueryHandler = UserQueryHandler(DataReaderTestHelper.userReader)
}
