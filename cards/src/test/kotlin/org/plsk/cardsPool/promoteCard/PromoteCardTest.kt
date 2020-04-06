package org.plsk.cardsPool.promoteCard

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.DisplayType
import org.plsk.cardsPool.WriteResult
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.QueryFilter
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.validation.Validation
import org.plsk.user.FakeUser
import java.util.*

class PromoteCardTest: WordSpec() {

  init {

    "promote card" should {

      "promote a card to a fixed position" {
        runBlocking {
        val command = PromoteCard(card3.id, 1, baseCardsPool.id, FakeUser.id)
        val promotedCardsPool = baseCardsPool.promote(command.cardId, command.position)

        val topCards = promoteHandler.handle(command)

        events shouldContain CardPromoted(command.cardId, command.position, promotedCardsPool)
        topCards shouldBe setOf(card1.id, card3.id, card2.id)
      }
      }

    }

  }

  val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", FakeClock.now().timestamp())
  val card2 = Card(UUID.randomUUID(), "test-card 2", "desc", FakeClock.now().timestamp())
  val card3 = Card(UUID.randomUUID(), "test-card 3", "desc", FakeClock.now().timestamp())

  val cards = mapOf<UUID, Card>(
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

  val cardsPoolRepository: CardsPoolRepository = object: CardsPoolRepository {
    override suspend fun findAll(filter: Iterable<QueryFilter>): List<CardsPool> = TODO("not implemented")
    override suspend fun update(data: CardsPool): WriteResult = TODO("not implemented")
    override suspend fun store(data: CardsPool): WriteResult = TODO("not implemented")
    override suspend fun find(id: UUID): CardsPool? = if (id == baseCardsPool.id) baseCardsPool else null
  }

  val validation: Validation<PromoteType, PromoteCardValidated> = PromoteCardValidation(cardsPoolRepository)
  var events = emptyList<CardPromoted>()

  val eventBus: EventBus = object : EventBus {
    override suspend fun dispatch(event: Event) = when (event) {
      is CardPromoted -> events = events.plusElement(event)
      else -> Unit
    }
  }

  val promoteHandler = PromoteCardAction(validation, eventBus)
}