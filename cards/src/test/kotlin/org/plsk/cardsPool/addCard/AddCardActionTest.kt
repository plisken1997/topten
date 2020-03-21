package org.plsk.cardsPool.addCard

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.WordSpec
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.WriteResult
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.QueryFilter
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.id.UUIDGen
import org.plsk.user.FakeUser
import java.util.*
import kotlinx.coroutines.runBlocking

class AddCardActionTest : WordSpec() {

  init {

    "add card handler" When {

      "adding a card with an unknown cards pool id" should {

        "fail to validate" {
          runBlocking {

            val command = AddCard("test-card", "desc", 1, UUID.randomUUID())
            shouldThrowExactly<Exception> {
              addCardHandler.handle(command)
            }
          }
        }
      }

      "adding a card with valid name and cards pool id" should {

        "publish a `card added` event to the event bus" {

          runBlocking {
            val command = AddCard("test-card", "desc", 0, baseCardsPool.id)
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

  val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", FakeClock.now().timestamp())

  val baseCardsPool = CardsPool(
      UUID.randomUUID(),
      "test cards pool",
      "desc",
      10,
      mapOf(Pair(card1.id, card1)),
      FakeClock.now().timestamp(),
      FakeUser.id,
      stock = setOf(card1.id)
  )

  var events = emptyList<CardAddedEvent>()

  val idGen = UUIDGen()

  val addCardHandler: AddCardAction = {

    val cardsPoolRepository: CardsPoolRepository = object : CardsPoolRepository {
      override suspend fun findAll(filter: Iterable<QueryFilter>): List<CardsPool> = TODO("not implemented")
      override suspend fun update(data: CardsPool): WriteResult = TODO("not implemented")
      override suspend fun store(data: CardsPool): WriteResult = TODO("not implemented")
      override suspend fun find(id: UUID): CardsPool? = if (id == baseCardsPool.id) baseCardsPool else null
    }

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