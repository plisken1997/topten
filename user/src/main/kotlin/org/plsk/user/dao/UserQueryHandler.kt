package org.plsk.user.dao

import org.plsk.core.dao.*
import org.plsk.user.User
import java.util.*

sealed class GetUserQuery(): Query

data class IdentifyUser(val name: String, val password: String, val grant: Iterable<String>): GetUserQuery()

class UserQueryHandler(private val userReader: DataReader<User, UUID>): QueryHandler<GetUserQuery, List<User>> {

  override fun handle(query: GetUserQuery): QueryResult<List<User>> =
    when(query) {
      is IdentifyUser -> {
          val result = userReader.findAll(setOf<QueryFilter>(
            EqFilter("name", query.name),
            EqFilter("password", query.password),
            InFilter("grant", query.grant.toSet()))
         )
        if (result.isEmpty()) {
          QueryResult(0, emptyList())
        } else {
          if (result.size > 1) {
            throw Exception("more than 1 user found")
          }
          QueryResult(1, result)
        }
      }
    }

}