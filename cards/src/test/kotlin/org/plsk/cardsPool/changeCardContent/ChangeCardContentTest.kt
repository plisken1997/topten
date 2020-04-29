package org.plsk.cardsPool.changeCardContent

import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.DisplayType
import org.plsk.cardsPool.WriteResult
import org.plsk.cardsPool.promoteCard.CardNotFound
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.QueryFilter
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.user.FakeUser
import java.util.*

class ChangeCardContentTest: WordSpec() {

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

  val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", FakeClock.now().timestamp())

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

  val cardsPoolRepository: CardsPoolRepository = object: CardsPoolRepository {
    override suspend fun find(id: UUID): CardsPool? =
        if(id == baseCardsPool.id) baseCardsPool
        else null
    override suspend fun findAll(filter: Iterable<QueryFilter>): List<CardsPool> = TODO("Not yet implemented")
    override suspend fun store(data: CardsPool): WriteResult = TODO("Not yet implemented")
    override suspend fun update(data: CardsPool): WriteResult = TODO("Not yet implemented")
  }

  var events = emptyList<Event>()

  val eventBus: EventBus = object: EventBus {
    override suspend fun dispatch(event: Event) {
      events = events.plus(event)
    }
  }

  val changeCardContentAction = ChangeCardContentAction(ValidateChangeCardContent(cardsPoolRepository), eventBus)
}