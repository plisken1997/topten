package org.plsk.cardsPool.removeCard

import io.kotlintest.matchers.collections.shouldContain
import kotlinx.coroutines.runBlocking
import org.plsk.cardsPool.*
import org.plsk.core.clock.FakeClock
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.validation.Validation
import org.plsk.user.FakeUser
import java.util.*

class RemoveCardActionTest : BaseCardsActionTest() {

  init {

    "remove card" should {

      "remove a card from a cards pool" {
        runBlocking {
          val command = RemoveCard(card1.id, baseCardsPool.id, FakeUser.id)
          val removedCardsPool =
              CardsPool(
                  baseCardsPool.id,
                  "test cards pool",
                  "desc",
                  10,
                  mapOf(
                      Pair(card2.id, card2),
                      Pair(card3.id, card3)
                  ),
                  DisplayType.ASC,
                  FakeClock.now().timestamp(),
                  FakeUser.id,
                  setOf(card2.id, card3.id),
                  setOf(card2.id)
              )

          removeHandler.handle(command)

          events shouldContain CardRemoved(removedCardsPool, command.cardId)
        }
      }

    }

  }

  val cards = mapOf(
      Pair(card1.id, card1),
      Pair(card2.id, card2),
      Pair(card3.id, card3)
  )

  val baseCardsPool = CardsPool(
      UUID.randomUUID(),
      "test cards pool",
      "desc",
      10,
      cards,
      DisplayType.ASC,
      FakeClock.now().timestamp(),
      FakeUser.id,
      setOf(card1.id, card2.id, card3.id),
      setOf(card1.id, card2.id)
  )

  val validation: Validation<RemoveCard, RemoveCardValidated> =
      RemoveCardValidation(getCardsPoolRepository(baseCardsPool))

  var events = emptyList<CardRemoved>()

  val eventBus: EventBus = object : EventBus {
    override suspend fun dispatch(event: Event) = when (event) {
      is CardRemoved -> events = events.plusElement(event)
      else -> Unit
    }
  }

  val removeHandler = RemoveCardAction(validation, eventBus)
}
