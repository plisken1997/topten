package org.plsk.cardsPool.getCards

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.WriteResult
import org.plsk.core.clock.Clock
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.QueryResult
import org.plsk.user.FakeUser
import java.util.*

class GetCardsQueryHandlerTest : WordSpec() {

  init {
    "getcards query handler" should {

      "return an empty QueryResult when no cards pool is found" {
        val res = getCardsQueryHandler.handle(GetCardsQuery(UUID.fromString("dd6cc82b-1f1a-458c-b285-44286c7093f9")))
        res shouldBe QueryResult(0, CardsPoolContent(emptySet(), emptySet()))
      }

      "return highlighted and pools list with an ascending sort" {
        val res = getCardsQueryHandler.handle(GetCardsQuery(cardsPoolId))
        val expected = QueryResult(
            5,
            CardsPoolContent(
              setOf(card4, card2, card5),
              setOf(card1, card3)
            )
        )
        res shouldBe expected
      }

    }
  }

  val clock: Clock = FakeClock()
  val card1 = Card(UUID.randomUUID(), "test-card 1", clock.now().timestamp())
  val card2 = Card(UUID.randomUUID(), "test-card 2", clock.now().timestamp())
  val card3 = Card(UUID.randomUUID(), "test-card 3", clock.now().timestamp())
  val card4 = Card(UUID.randomUUID(), "test-card 4", clock.now().timestamp())
  val card5 = Card(UUID.randomUUID(), "test-card 5", clock.now().timestamp())
  val cardsPoolId = UUID.fromString("439dac7c-aec3-4597-aa40-fcc96a76b1d2")

  val baseCardsPool = CardsPool(
      cardsPoolId,
      "test cards pool",
      "desc",
      10,
      setOf(card2, card5, card3, card1, card4),
      clock.now().timestamp(),
      FakeUser,
      stock = setOf(card1.id, card3.id),
      topCards = setOf(card4.id, card2.id, card5.id)
  )

  val cardsPoolRepository = object: CardsPoolRepository{
    override fun store(data: CardsPool): WriteResult {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(data: CardsPool): WriteResult {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun find(id: UUID): CardsPool? = if(id == cardsPoolId){ baseCardsPool } else null
  }

  val getCardsQueryHandler = GetCardsQueryHandler(cardsPoolRepository)
}
