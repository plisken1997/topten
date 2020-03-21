package org.plsk.cardsPool.getCards

import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.core.dao.Query
import org.plsk.core.dao.QueryHandler
import org.plsk.core.dao.QueryResult
import java.util.*

sealed class CardQuery: Query

data class GetCardsQuery(val cardsPoolId: UUID): CardQuery()
data class GetCardsPoolsQuery(val userId: String): CardQuery()

// @todo use a sorted set
data class CardsPoolContent(val highlighted: Set<Card>, val cardsPool: Set<Card>)

class GetCardsQueryHandler(private val cardsPoolRepository: CardsPoolRepository): QueryHandler<CardQuery, List<CardsPoolContent>> {
  suspend override fun handle(query: CardQuery): QueryResult<List<CardsPoolContent>> {
    val queryResult: QueryResult<List<CardsPoolContent>> = when (query) {
      is GetCardsQuery -> {
        val cardsPool = cardsPoolRepository.find(query.cardsPoolId)
        QueryResult(
            if (cardsPool != null) 1 else 0,
            listOf(
                CardsPoolContent(
                    cardsPool?.getHighlighted() ?: emptySet(),
                    cardsPool?.getPool() ?: emptySet()
                ))
        )
      }
      is GetCardsPoolsQuery -> {
        val cardsPools = cardsPoolRepository.findByUser(query.userId)
        QueryResult(
            cardsPools.size,
            cardsPools.map {
              cardsPool ->
                CardsPoolContent(
                    cardsPool.getHighlighted(),
                    cardsPool.getPool()
                )
            }
        )
      }
    }
    return queryResult
  }
}

