package org.plsk.cardsPool.removeCard

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.WriteResult
import org.plsk.cardsPool.promoteCard.*
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.QueryFilter
import org.plsk.core.validation.Validation
import org.plsk.user.FakeUser
import java.util.*

class RemoveCardValidationTest : WordSpec() {

  init {

    "remove card validation" should {

      "fail when the cards pool does not exists" {
        runBlocking {
          val command = RemoveCard(card1.id, UUID.randomUUID())
          shouldThrowExactly<CardsPoolNotFound> {
            validation.validate(command)
          }
        }
      }

      "fail when the cards pool does not contains the cardId " {
        runBlocking {
          val command = RemoveCard(UUID.randomUUID(), baseCardsPool.id)
          shouldThrowExactly<CardNotFound> {
            validation.validate(command)
          }
        }
      }

      "return the parent cards pool when the command is valid" {
        runBlocking {
          val command = RemoveCard(card1.id, baseCardsPool.id)
          validation.validate(command) shouldBe RemoveCardValidated(command.cardId, baseCardsPool)
        }
      }

    }

  }

  val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", FakeClock.now().timestamp())
  val card2 = Card(UUID.randomUUID(), "test-card 2", "desc", FakeClock.now().timestamp())

  val cards = mapOf<UUID, Card>(
      Pair(card1.id, card1),
      Pair(card2.id, card2)
  )

  val baseCardsPool = CardsPool(
      UUID.randomUUID(),
      "test cards pool",
      "desc",
      10,
      cards,
      FakeClock.now().timestamp(),
      FakeUser.id,
      setOf(card1.id, card2.id),
      setOf(card2.id)
  )

  val cardsPoolRepository: CardsPoolRepository = object : CardsPoolRepository {
    override suspend fun findAll(filter: Iterable<QueryFilter>): List<CardsPool> {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(data: CardsPool): WriteResult {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun store(data: CardsPool): WriteResult {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun find(id: UUID): CardsPool? = if (id == baseCardsPool.id) baseCardsPool else null
  }

  val validation: Validation<RemoveCard, RemoveCardValidated> = RemoveCardValidation(cardsPoolRepository)

}