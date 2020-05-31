package org.plsk.cardsPool

import java.util.*

sealed class WriteResult

data class WriteSuccess(val id: UUID): WriteResult()


sealed class CardQuery {
  abstract val userId: String
}

data class GetCardsQuery(val cardsPoolId: UUID, override val userId: String) : CardQuery()
data class GetCardsPoolByUser(override val userId: String) : CardQuery()

interface CardsPoolRepository {
  suspend fun findByUser(query: GetCardsPoolByUser): List<CardsPool>

  suspend fun find(query: GetCardsQuery): CardsPool?

  suspend fun add(data: CardsPool): WriteResult
  suspend fun update(data: CardsPool): WriteResult
}