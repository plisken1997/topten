package org.plsk.core.dao

interface Query

data class QueryResult<DataType>(
  val size: Int,
  val content: DataType
)

interface QueryHandler<Q: Query, DataType> {
  suspend fun handle(query: Q): QueryResult<DataType>
}
