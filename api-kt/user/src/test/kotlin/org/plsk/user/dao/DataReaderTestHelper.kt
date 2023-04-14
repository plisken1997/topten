package org.plsk.user.dao

import org.plsk.core.dao.DataReader
import org.plsk.core.dao.EqFilter
import org.plsk.core.dao.QueryFilter
import org.plsk.user.AppUser
import org.plsk.user.User

object DataReaderTestHelper {

  val user = AppUser("5bfa9d6b-04b2-4bce-9b5d-ad546ada55e1", "user-ok")
  val user2 = AppUser("50b3e08c-0a36-421e-8a7e-3e5132b5fc5b", "user-2")

  val userReader: DataReader<User, String> = object: DataReader<User, String> {
    override suspend fun findAll(filter: Iterable<QueryFilter>): List<User> {
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

    override suspend fun find(id: String): User? = TODO("not implemented")
  }

}