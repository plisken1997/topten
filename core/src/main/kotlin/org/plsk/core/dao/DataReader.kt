package org.plsk.core.dao

@Deprecated("use repositories instead")
interface DataReader<T, ID> {
  suspend fun find(id: ID): T?
  suspend fun findAll(filter: Iterable<QueryFilter>): List<T>
}
