package org.plsk.cardsPool.changeCardContent

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import kotlinx.coroutines.runBlocking
import org.plsk.cardsPool.*
import org.plsk.cardsPool.promoteCard.CardNotFound
import org.plsk.core.clock.FakeClock
import org.plsk.user.FakeUser
import java.util.*

class ValidateChangeCardContentTest: BaseCardsActionTest() {

  init {

    "validate change card content" should {

      "fail when the user is not granted" {
        runBlocking {
          shouldThrowExactly<Exception> {
            validator.validate(validCommand.copy(userId = UUID.randomUUID().toString()))
          }
        }
      }

      "fail when the cardspool is not found" {
        runBlocking {
          shouldThrowExactly<Exception> {
            validator.validate(validCommand.copy(cardsPoolId = UUID.randomUUID()))
          }
        }
      }

      "fail when the card is not found" {
        runBlocking {
          shouldThrowExactly<CardNotFound> {
            validator.validate(validCommand.copy(cardId = UUID.randomUUID()))
          }
        }
      }

      "fail when the field does not exists" {
        runBlocking {
          shouldThrowExactly<Exception> {
            validator.validate(validCommand.copy(field = "some field"))
          }
        }
      }

      "succeed when the command is valid" {
        runBlocking {
          val result = validator.validate(validCommand)
          val expected =
              ChangeCardContentValidated(
                  card1.id,
                  baseCardsPool.copy(cards = mapOf(Pair(card1.id, card1.copy(label = "update name"))))
              )
          result shouldBe expected
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

  val validator = ValidateChangeCardContent(getCardsPoolRepository(baseCardsPool))
}
