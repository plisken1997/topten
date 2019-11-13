package org.plsk.cardsPool.removeCard

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.specs.WordSpec
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.WriteResult
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.QueryFilter
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.validation.Validation
import org.plsk.user.FakeUser
import java.util.*

class RemoveCardHandlerTest: WordSpec()  {

  init {

    "remove card" should {

      "remove a card from a cards pool" {
        val command = RemoveCard(card1.id, baseCardsPool.id)
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

  val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", FakeClock.now().timestamp())
  val card2 = Card(UUID.randomUUID(), "test-card 2", "desc", FakeClock.now().timestamp())
  val card3 = Card(UUID.randomUUID(), "test-card 3", "desc", FakeClock.now().timestamp())

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
      FakeClock.now().timestamp(),
      FakeUser.id,
      setOf(card1.id, card2.id, card3.id),
      setOf(card1.id, card2.id)
  )

  val cardsPoolRepository: CardsPoolRepository = object: CardsPoolRepository {
    override fun findAll(filter: Iterable<QueryFilter>): List<CardsPool> {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(data: CardsPool): WriteResult {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun store(data: CardsPool): WriteResult {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun find(id: UUID): CardsPool? = if (id == baseCardsPool.id) baseCardsPool else null
  }

  val validation: Validation<RemoveCard, RemoveCardValidated> = RemoveCardValidation(cardsPoolRepository)
  var events = emptyList<CardRemoved>()

  val eventBus: EventBus = object : EventBus {
    override fun dispatch(event: Event) = when (event) {
      is CardRemoved -> events = events.plusElement(event)
      else -> Unit
    }
  }

  val removeHandler = RemoveCardHandler(validation, eventBus)
}