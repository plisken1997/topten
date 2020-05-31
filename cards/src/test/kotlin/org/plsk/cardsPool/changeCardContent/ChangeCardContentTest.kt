package org.plsk.cardsPool.changeCardContent

import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldThrowExactly
import kotlinx.coroutines.runBlocking
import org.plsk.cardsPool.*
import org.plsk.cardsPool.promoteCard.CardNotFound
import org.plsk.core.clock.FakeClock
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.user.FakeUser
import java.util.*

class ChangeCardContentTest: BaseCardsActionTest() {

  init {

    "Update card action" should {

      "fail to update a card in a cardspool when the card is not found" {
        runBlocking {
          shouldThrowExactly<CardNotFound> {
            changeCardContentAction.handle(validCommand.copy(cardId = UUID.randomUUID()))
          }
        }
      }

      "succeed to update a card in a cardspool" {
        runBlocking {
          changeCardContentAction.handle(validCommand)
          val expectedEvent = ChangeCardContentValidated(
              card1.id,
              baseCardsPool.copy(cards = mapOf(Pair(card1.id, card1.copy(label = "update name"))))
          )
          events shouldContainExactly listOf(expectedEvent)
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
      FakeUser.id
  )

  val validCommand =
      ChangeCardContent(card1.id, baseCardsPool.id, "label", "update name", FakeUser.id)

  var events = emptyList<Event>()

  val eventBus: EventBus = object: EventBus {
    override suspend fun dispatch(event: Event) {
      events = events.plus(event)
    }
  }

  val changeCardContentAction = ChangeCardContentAction(
      ValidateChangeCardContent(getCardsPoolRepository(baseCardsPool)),
      eventBus
  )
}
