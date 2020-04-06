package org.plsk.cardsPool.promoteCard

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.DisplayType
import org.plsk.cardsPool.WriteResult
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.QueryFilter
import org.plsk.core.validation.Validation
import org.plsk.user.FakeUser
import java.util.*

class PromoteCardValidationTest : WordSpec() {

  init {

    "promote card validation" should {

      "fail when the cards pool does not exists" {
        runBlocking {
          val command = PromoteCard(card1.id, 1, UUID.randomUUID(), FakeUser.id)
          shouldThrowExactly<CardsPoolNotFound> {
            validation.validate(command)
          }
        }
      }

      "fail when the cards pool does not contains the cardId " {
        runBlocking {
          val command = PromoteCard(UUID.randomUUID(), 1, baseCardsPool.id, FakeUser.id)
          shouldThrowExactly<CardNotFound> {
            validation.validate(command)
          }
        }
      }

      "fail when the user is not authorized" {
        runBlocking {
          val command = PromoteCard(card1.id, 1, baseCardsPool.id, "not-authorized-user")
          shouldThrowExactly<Unauthorized> {
            validation.validate(command)
          }
        }

      }


      "return the parent cards pool when the command is valid" {
        runBlocking {
          val command = PromoteCard(card1.id, 1, baseCardsPool.id, FakeUser.id)
          validation.validate(command) shouldBe PromoteCardValidated(command.cardId, baseCardsPool)
        }
      }

    }

  }

  val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", FakeClock.now().timestamp())
  val card2 = Card(UUID.randomUUID(), "test-card 2", "desc", FakeClock.now().timestamp())

  val cards = mapOf(
      Pair(card1.id, card1),
      Pair(card2.id, card2)
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
      setOf(card1.id, card2.id),
      setOf(card2.id)
  )

  val cardsPoolRepository: CardsPoolRepository = object : CardsPoolRepository {
    override suspend fun findAll(filter: Iterable<QueryFilter>): List<CardsPool> = TODO("not implemented")
    override suspend fun update(data: CardsPool): WriteResult = TODO("not implemented")
    override suspend fun store(data: CardsPool): WriteResult = TODO("not implemented")
    override suspend fun find(id: UUID): CardsPool? = if (id == baseCardsPool.id) baseCardsPool else null
  }

  val validation: Validation<PromoteType, PromoteCardValidated> = PromoteCardValidation(cardsPoolRepository)

}
