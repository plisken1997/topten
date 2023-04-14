package org.plsk.user.dao

import org.plsk.core.dao.*
import org.plsk.user.User

sealed class GetUserQuery: Query

data class IdentifyUser(val name: String, val password: String, val grant: Iterable<String>): GetUserQuery()

class UserQueryHandler(private val userReader: DataReader<User, String>): QueryHandler<GetUserQuery, List<User>> {

  suspend override fun handle(query: GetUserQuery): QueryResult<List<User>> =
    when(query) {
      is IdentifyUser -> identifyUser(query)
    }

  private suspend fun identifyUser(query: IdentifyUser): QueryResult<List<User>> {
    val result = userReader.findAll(setOf<QueryFilter>(
        EqFilter("name", query.name),
        EqFilter("password", query.password),
        InFilter("grants", query.grant.toSet()))
    )
    if (result.isEmpty()) {
      return QueryResult(0, emptyList())
    }

    if (result.size > 1) {
      throw Exception("more than 1 user found")
    }

    return QueryResult(1, result)
  }
}