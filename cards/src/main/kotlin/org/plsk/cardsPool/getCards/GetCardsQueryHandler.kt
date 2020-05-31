package org.plsk.cardsPool.getCards

import org.plsk.cards.Card
import org.plsk.cardsPool.*
import org.plsk.core.dao.QueryHandler
import org.plsk.core.dao.QueryResult
import java.util.*

// @todo use a sorted set
data class CardsPoolContent(
    val id: UUID,
    val name: String,
    val description: String?,
    val slots: Int?,
    val display: DisplayType?,
    val highlighted: Set<Card>,
    val cardsPool: Set<Card>
) {
  companion object {

    fun fromCardsPool(cardsPool: CardsPool): CardsPoolContent =
        CardsPoolContent(
            cardsPool.id,
            cardsPool.name,
            cardsPool.description,
            cardsPool.slots,
            cardsPool.display,
            cardsPool.getHighlighted(),
            cardsPool.getPool()
        )
  }

  private fun isSortedAsc(): Boolean = display?.let { it == DisplayType.ASC} ?: true

  fun getOrderedHighlighted(): Set<Card> = if(isSortedAsc()) highlighted else highlighted.reversed().toSet()
}

class GetCardsQueryHandler(private val cardsPoolRepository: CardsPoolRepository) : QueryHandler<CardQuery, List<CardsPoolContent>> {
  suspend override fun handle(query: CardQuery): QueryResult<List<CardsPoolContent>> =
    when (query) {
      is GetCardsQuery -> getCards(query)
      is GetCardsPoolByUser -> getCardsPool(query)
    }

  private suspend fun getCards(query: GetCardsQuery): QueryResult<List<CardsPoolContent>> {
    // @todo filter on user id !!!
    val cardsPool = cardsPoolRepository.find(query)
    if (cardsPool == null) {
      return QueryResult(0, emptyList())
    }

    return QueryResult(1, listOf(CardsPoolContent.fromCardsPool(cardsPool)))
  }

  private suspend fun getCardsPool(query: GetCardsPoolByUser): QueryResult<List<CardsPoolContent>> {
    val cardsPools = cardsPoolRepository.findByUser(GetCardsPoolByUser(query.userId))
    return QueryResult(
        cardsPools.size,
        cardsPools.map { CardsPoolContent.fromCardsPool(it) }
    )
  }
}
