package org.plsk.cardsPool.addCard

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import org.plsk.core.clock.FakeClock
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.user.FakeUser
import java.util.*
import kotlinx.coroutines.runBlocking
import org.plsk.cardsPool.*

class AddCardActionTest : BaseCardsActionTest() {

  init {

    "add card handler" When {

      "adding a card with an unknown cards pool id" should {

        "fail to validate" {
          runBlocking {

            val command = AddCard("test-card", "desc", 1, UUID.randomUUID(), FakeUser.id)
            shouldThrowExactly<Exception> {
              addCardHandler.handle(command)
            }
          }
        }
      }

      "adding a card with valid name and cards pool id" should {

        "publish a `card added` event to the event bus" {

          runBlocking {
            val command = AddCard("test-card", "desc", 0, baseCardsPool.id, FakeUser.id)
            val createdId = addCardHandler.handle(command)

            val expectedId = idGen.fromString(command.title + baseCardsPool.id.toString())

            createdId shouldBe expectedId

            val newCard = AddCardValidation.createCard(command, FakeClock, expectedId)
            val expected = CardAddedEvent(baseCardsPool.copy(
                cards = mapOf(Pair(card1.id, card1), Pair(newCard.id, newCard)),
                stock = setOf(newCard.id, card1.id)
            ))

            events shouldContain expected
          }
        }

      }
    }
  }

  val baseCardsPool = CardsPool(
      UUID.randomUUID(),
      "test cards pool",
      "desc",
      10,
      mapOf(Pair(card1.id, card1)),
      DisplayType.ASC,
      FakeClock.now().timestamp(),
      FakeUser.id,
      stock = setOf(card1.id)
  )

  var events = emptyList<CardAddedEvent>()

  val addCardHandler: AddCardAction = {

    val cardsPoolRepository: CardsPoolRepository = getCardsPoolRepository(baseCardsPool)
    val validation = AddCardValidation(cardsPoolRepository, FakeClock, idGen)

    val eventBus: EventBus = object : EventBus {
      override suspend fun dispatch(event: Event) = when (event) {
        is CardAddedEvent -> events = events.plusElement(event)
        else -> Unit
      }
    }

    AddCardAction(validation, cardsPoolRepository, eventBus)
  }()
}
