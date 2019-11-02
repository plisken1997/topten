package org.plsk.cardsPool.promoteCard

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.WordSpec
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.WriteResult
import org.plsk.core.clock.FakeClock
import org.plsk.core.validation.Validation
import org.plsk.user.FakeUser
import java.util.*

class PromoteCardValidationTest: WordSpec() {

  init {

    "promote card validation" should {

      "fail when the cards pool does not exists" {
        val command = PromoteCard(card1.id, 1, UUID.randomUUID())
        shouldThrowExactly<CardsPoolNotFound>{
          validation.validate(command)
        }
      }

      "fail when the cards pool does not contains the cardId " {
        val command = PromoteCard(UUID.randomUUID(), 1, baseCardsPool.id)
        shouldThrowExactly<CardNotFound>{
          validation.validate(command)
        }
      }

      "return the parent cards pool when the command is valid" {
        val command = PromoteCard(card1.id, 1, baseCardsPool.id)
        validation.validate(command) shouldBe PromoteCardValidated(command.cardId, baseCardsPool)
      }

    }

  }

  val clock = FakeClock()

  val card1 = Card(UUID.randomUUID(), "test-card 1", clock.now().timestamp())
  val card2 = Card(UUID.randomUUID(), "test-card 2", clock.now().timestamp())

  val cards = setOf<Card>(
      card1,
      card2
  )

  val baseCardsPool = CardsPool(
      UUID.randomUUID(),
      "test cards pool",
      "desc",
      10,
      cards,
      clock.now().timestamp(),
      FakeUser,
      setOf(card1.id, card2.id),
      setOf(card2.id)
  )

  val cardsPoolRepository: CardsPoolRepository = object: CardsPoolRepository {
    override fun update(data: CardsPool): WriteResult {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun store(data: CardsPool): WriteResult {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun find(id: UUID): CardsPool? = if (id == baseCardsPool.id) baseCardsPool else null
  }

  val validation: Validation<PromoteType, PromoteCardValidated> = PromoteCardValidation(cardsPoolRepository)

}