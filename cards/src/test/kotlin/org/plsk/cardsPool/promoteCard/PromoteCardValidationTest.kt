package org.plsk.cardsPool.promoteCard

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import kotlinx.coroutines.runBlocking
import org.plsk.cardsPool.*
import org.plsk.core.clock.FakeClock
import org.plsk.core.validation.Validation
import org.plsk.user.FakeUser
import java.util.*

class PromoteCardValidationTest : BaseCardsActionTest() {

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

  val validation: Validation<PromoteType, PromoteCardValidated> =
      PromoteCardValidation(getCardsPoolRepository(baseCardsPool))

}
