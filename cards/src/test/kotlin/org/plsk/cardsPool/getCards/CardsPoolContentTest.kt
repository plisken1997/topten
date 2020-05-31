package org.plsk.cardsPool.getCards

import io.kotlintest.matchers.collections.shouldContainExactly
import org.plsk.cardsPool.BaseCardsActionTest
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.DisplayType
import org.plsk.core.clock.FakeClock
import org.plsk.user.FakeUser
import java.util.*

class CardsPoolContentTest: BaseCardsActionTest() {

  init {

    "cards pool content" should {

      "yield highlighted cards ordered ASC" {
        val cardsPool = baseCardsPool
        val cardsPoolContent = CardsPoolContent.fromCardsPool(cardsPool)

        val expected = setOf(card4, card2, card5)
        cardsPoolContent.getOrderedHighlighted() shouldContainExactly expected
      }


      "yield highlighted cards ordered ASC when asc is not set" {
        val cardsPool = baseCardsPool.copy(display = null)
        val cardsPoolContent = CardsPoolContent.fromCardsPool(cardsPool)

        val expected = setOf(card4, card2, card5)
        cardsPoolContent.getOrderedHighlighted() shouldContainExactly expected
      }


      "yield highlighted cards ordered DESC" {
        val cardsPool = baseCardsPool.copy(display = DisplayType.DESC)
        val cardsPoolContent = CardsPoolContent.fromCardsPool(cardsPool)

        val expected = setOf(card5, card2, card4)
        cardsPoolContent.getOrderedHighlighted() shouldContainExactly expected
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
}