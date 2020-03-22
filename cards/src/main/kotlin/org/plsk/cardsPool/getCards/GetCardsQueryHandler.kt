package org.plsk.cardsPool.getCards

import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.core.dao.Query
import org.plsk.core.dao.QueryHandler
import org.plsk.core.dao.QueryResult
import java.util.*

sealed class CardQuery : Query

data class GetCardsQuery(val cardsPoolId: UUID, val userId: String) : CardQuery()
data class GetCardsPoolsQuery(val userId: String) : CardQuery()

// @todo use a sorted set
data class CardsPoolContent(
    val id: UUID,
    val name: String,
    val description: String?,
    val slots: Int?,
    val highlighted: Set<Card>,
    val cardsPool: Set<Card>
)

class GetCardsQueryHandler(private val cardsPoolRepository: CardsPoolRepository) : QueryHandler<CardQuery, List<CardsPoolContent>> {
  suspend override fun handle(query: CardQuery): QueryResult<List<CardsPoolContent>> =
    when (query) {
      is GetCardsQuery -> {
        // @todo filter on user id !!!
        val cardsPool = cardsPoolRepository.find(query.cardsPoolId)
        if (cardsPool == null) QueryResult(0, emptyList())
        else {
          QueryResult(
              1,
              listOf(
                  CardsPoolContent(
                      cardsPool.id,
                      cardsPool.name,
                      cardsPool.description,
                      cardsPool.slots,
                      cardsPool.getHighlighted(),
                      cardsPool.getPool()
                  ))
          )
        }
      }
      is GetCardsPoolsQuery -> {
        val cardsPools = cardsPoolRepository.findByUser(query.userId)
        QueryResult(
            cardsPools.size,
            cardsPools.map { cardsPool ->
              CardsPoolContent(
                  cardsPool.id,
                  cardsPool.name,
                  cardsPool.description,
                  cardsPool.slots,
                  cardsPool.getHighlighted(),
                  cardsPool.getPool()
              )
            }
        )
      }
    }
}
