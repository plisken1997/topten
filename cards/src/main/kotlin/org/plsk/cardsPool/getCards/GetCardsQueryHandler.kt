package org.plsk.cardsPool.getCards

import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.core.dao.Query
import org.plsk.core.dao.QueryHandler
import org.plsk.core.dao.QueryResult
import java.util.*

data class GetCardsQuery(val cardsPoolId: UUID): Query

// @todo use a sorted set
data class CardsPoolContent(val highlighted: Set<Card>, val cardsPool: Set<Card>)

class GetCardsQueryHandler(private val cardsPoolRepository: CardsPoolRepository): QueryHandler<GetCardsQuery, CardsPoolContent> {
  override fun handle(query: GetCardsQuery): QueryResult<CardsPoolContent> {
    val cardsPool = cardsPoolRepository.find(query.cardsPoolId)

    return QueryResult(
        cardsPool?.cards?.size ?: 0,
        CardsPoolContent(
            cardsPool?.getHighlighted() ?: emptySet(),
            cardsPool?.getPool() ?: emptySet()
        ))
  }
}
