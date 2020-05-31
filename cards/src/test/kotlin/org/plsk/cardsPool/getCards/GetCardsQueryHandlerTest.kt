package org.plsk.cardsPool.getCards

import io.kotlintest.shouldBe
import kotlinx.coroutines.runBlocking
import org.plsk.cardsPool.*
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.QueryResult
import org.plsk.user.FakeUser
import java.util.*

class GetCardsQueryHandlerTest : BaseCardsActionTest() {

  init {
    "getcards query handler" should {

      "return an empty QueryResult when no cards pool is found" {
        runBlocking {
          val res: QueryResult<List<CardsPoolContent>> =
              getCardsQueryHandler.handle(GetCardsQuery(UUID.fromString("dd6cc82b-1f1a-458c-b285-44286c7093f9"), FakeUser.id))
          res shouldBe QueryResult(0, emptyList<CardsPoolContent>())
        }
      }

      "return highlighted and pools list with an ascending sort" {
        runBlocking {

          val res: QueryResult<List<CardsPoolContent>> = getCardsQueryHandler.handle(GetCardsQuery(cardsPoolId, FakeUser.id))
          val expected = QueryResult(
              1,
              listOf(
                  CardsPoolContent(
                      baseCardsPool.id,
                      baseCardsPool.name,
                      baseCardsPool.description,
                      baseCardsPool.slots,
                      DisplayType.ASC,
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

  val cardsPoolId = UUID.fromString("439dac7c-aec3-4597-aa40-fcc96a76b1d2")

  val baseCardsPool = CardsPool(
      cardsPoolId,
      "test cards pool",
      "desc",
      10,
      mapOf(Pair(card2.id, card2), Pair(card5.id, card5), Pair(card3.id, card3), Pair(card1.id, card1), Pair(card4.id, card4)),
      DisplayType.ASC,
      FakeClock.now().timestamp(),
      FakeUser.id,
      stock = setOf(card1.id, card3.id),
      topCards = setOf(card4.id, card2.id, card5.id)
  )

  val getCardsQueryHandler = GetCardsQueryHandler(getCardsPoolRepository(baseCardsPool))
}
