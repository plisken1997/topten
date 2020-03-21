package org.plsk.cardsPool.getCards

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.WriteResult
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.QueryFilter
import org.plsk.core.dao.QueryResult
import org.plsk.user.FakeUser
import java.util.*

class GetCardsQueryHandlerTest : WordSpec() {

  init {
    "getcards query handler" should {

      "return an empty QueryResult when no cards pool is found" {
        runBlocking {
          val res: QueryResult<List<CardsPoolContent>> = getCardsQueryHandler.handle(GetCardsQuery(UUID.fromString("dd6cc82b-1f1a-458c-b285-44286c7093f9")))
          res shouldBe QueryResult(0, emptyList<CardsPoolContent>())
        }
      }

      "return highlighted and pools list with an ascending sort" {
        runBlocking {

          val res: QueryResult<List<CardsPoolContent>> = getCardsQueryHandler.handle(GetCardsQuery(cardsPoolId))
          val expected = QueryResult(
              1,
              listOf(
                CardsPoolContent(
                    baseCardsPool.id,
                    baseCardsPool.name,
                    baseCardsPool.description,
                    baseCardsPool.slots,
                    setOf(card4, card2, card5),
                    setOf(card1, card3)
                )
              )
          )
          res shouldBe expected
        }
      }

    }
  }

  val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", FakeClock.now().timestamp())
  val card2 = Card(UUID.randomUUID(), "test-card 2", "desc", FakeClock.now().timestamp())
  val card3 = Card(UUID.randomUUID(), "test-card 3", "desc", FakeClock.now().timestamp())
  val card4 = Card(UUID.randomUUID(), "test-card 4", "desc", FakeClock.now().timestamp())
  val card5 = Card(UUID.randomUUID(), "test-card 5", "desc", FakeClock.now().timestamp())
  val cardsPoolId = UUID.fromString("439dac7c-aec3-4597-aa40-fcc96a76b1d2")

  val baseCardsPool = CardsPool(
      cardsPoolId,
      "test cards pool",
      "desc",
      10,
      mapOf(Pair(card2.id, card2), Pair(card5.id, card5), Pair(card3.id, card3), Pair(card1.id, card1), Pair(card4.id, card4)),
      FakeClock.now().timestamp(),
      FakeUser.id,
      stock = setOf(card1.id, card3.id),
      topCards = setOf(card4.id, card2.id, card5.id)
  )

  val cardsPoolRepository = object : CardsPoolRepository {
    override suspend fun findAll(filter: Iterable<QueryFilter>): List<CardsPool> = TODO("not implemented")
    override suspend fun store(data: CardsPool): WriteResult = TODO("not implemented")
    override suspend fun update(data: CardsPool): WriteResult = TODO("not implemented")
    override suspend fun find(id: UUID): CardsPool? = if (id == cardsPoolId) baseCardsPool else null
  }

  val getCardsQueryHandler = GetCardsQueryHandler(cardsPoolRepository)
}
