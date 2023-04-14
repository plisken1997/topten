package org.plsk.cardsPool.removeCard

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import kotlinx.coroutines.runBlocking
import org.plsk.cards.Card
import org.plsk.cardsPool.*
import org.plsk.cardsPool.promoteCard.*
import org.plsk.core.clock.FakeClock
import org.plsk.core.validation.Validation
import org.plsk.user.FakeUser
import java.util.*

class RemoveCardValidationTest : BaseCardsActionTest() {

  init {

    "remove card validation" should {

      "fail when the cards pool does not exists" {
        runBlocking {
          val command = RemoveCard(card1.id, UUID.randomUUID(), FakeUser.id)
          shouldThrowExactly<CardsPoolNotFound> {
            validation.validate(command)
          }
        }
      }

      "fail when the cards pool does not contains the cardId " {
        runBlocking {
          val command = RemoveCard(UUID.randomUUID(), baseCardsPool.id, FakeUser.id)
          shouldThrowExactly<CardNotFound> {
            validation.validate(command)
          }
        }
      }

      "fail when the user is not authorized" {
        runBlocking {
          val command = RemoveCard(card1.id, baseCardsPool.id, "not-authorized")
          shouldThrowExactly<Unauthorized> {
            validation.validate(command)
          }
        }
      }

      "return the parent cards pool when the command is valid" {
        runBlocking {
          val command = RemoveCard(card1.id, baseCardsPool.id, FakeUser.id)
          validation.validate(command) shouldBe RemoveCardValidated(command.cardId, baseCardsPool)
        }
      }

    }

  }

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
      DisplayType.ASC,
      FakeClock.now().timestamp(),
      FakeUser.id,
      setOf(card1.id, card2.id),
      setOf(card2.id)
  )

  val validation: Validation<RemoveCard, RemoveCardValidated> =
      RemoveCardValidation(getCardsPoolRepository(baseCardsPool))

}
