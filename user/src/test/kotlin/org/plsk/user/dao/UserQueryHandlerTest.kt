package org.plsk.user.dao

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import org.plsk.core.dao.DataReader
import org.plsk.core.dao.EqFilter
import org.plsk.core.dao.QueryFilter
import org.plsk.core.dao.QueryResult
import org.plsk.user.AppUser
import org.plsk.user.User
import java.util.*

class UserQueryHandlerTest: WordSpec() {

  init {

    "user query handler" should {

      "return the expected user" {
        val res = userQueryHandler.handle(IdentifyUser("user-ok", "1234", emptySet()))
        res shouldBe QueryResult(1, listOf(user))
      }

      "return an empty list when the user is not found" {
        val res = userQueryHandler.handle(IdentifyUser("user-notfound", "1234", emptySet()))
        res shouldBe QueryResult(0, emptyList<User>())
      }

      "throw an exception when more thant one user is found" {
        val exception = shouldThrow<Exception> {
          userQueryHandler.handle(IdentifyUser("user-multiple", "1234", emptySet()))
        }
        exception.message shouldBe "more than 1 user found"
      }

    }

  }

  val user = AppUser("5bfa9d6b-04b2-4bce-9b5d-ad546ada55e1", "user-ok")
  val user2 = AppUser("50b3e08c-0a36-421e-8a7e-3e5132b5fc5b", "user-2")

  val userReader: DataReader<User, UUID> = object: DataReader<User, UUID>{
    override fun findAll(filter: Iterable<QueryFilter>): List<User> {
      val nameFilter: QueryFilter? = filter.find{ f -> f.name == "name"}

      return when(nameFilter) {
        is EqFilter<*> ->
          if (nameFilter.value == "user-ok") {
            listOf(user)
          } else if (nameFilter.value == "user-multiple") {
            listOf(user, user2)
          } else {
            emptyList()
          }
        else -> throw Exception("filter should be handled")
      }
    }

    override fun find(id: UUID): User? {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

  }

  val userQueryHandler = UserQueryHandler(userReader)
}