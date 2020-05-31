package org.plsk.core.dao

@Deprecated("query type should be free")
interface Query

data class QueryResult<DataType>(
  val size: Int,
  val content: DataType
)

interface QueryHandler<Q, DataType> {
  suspend fun handle(query: Q): QueryResult<DataType>
}
