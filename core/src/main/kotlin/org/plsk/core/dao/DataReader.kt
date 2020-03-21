package org.plsk.core.dao

interface DataReader<T, ID> {
  suspend fun find(id: ID): T?
  suspend fun findAll(filter: Iterable<QueryFilter>): List<T>
}
