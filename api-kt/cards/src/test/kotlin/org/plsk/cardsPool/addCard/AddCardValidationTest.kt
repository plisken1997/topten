package org.plsk.cardsPool.addCard

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import kotlinx.coroutines.runBlocking
import org.plsk.cards.Card
import org.plsk.cardsPool.*
import org.plsk.core.clock.FakeClock
import org.plsk.user.FakeUser
import java.util.*

class AddCardValidationTest : BaseCardsActionTest() {

  init {

    "add card validation" When {

      "validate an unknown cards pool" should {

        "fail to validate" {
          runBlocking {

            val command = AddCard("insert-1", "desc", 1, UUID.nameUUIDFromBytes("test".toByteArray()), "unauthorized-user")
            // @todo will be better tested after implemented the monadic effect on validation output
            shouldThrowExactly<Exception> {
              validation.validate(command)
            }
          }
        }
      }

      "validate an existing label" should {

        "fail to validate" {
          runBlocking {
            val command = AddCard("test-card 2", "desc", 1, baseCardsPool.id, FakeUser.id)
            // @todo will be better tested after implemented the monadic effect on validation output
            shouldThrowExactly<Exception> {
              validation.validate(command)
            }
          }
        }

      }

      "validate an unauthorized user" should {

        "fail to validate" {
          runBlocking {
            val command = AddCard("test-card", "desc", 1, baseCardsPool.id, "unauthorized-userd")
            // @todo will be better tested after implemented the monadic effect on validation output
            shouldThrowExactly<Exception> {
              validation.validate(command)
            }
          }
        }

      }

      "validate the card" should {

        "add the card to the cards pool" {
          runBlocking {
            val command = AddCard("test-card", "desc", 1, baseCardsPool.id, FakeUser.id)
            val newCardsPool = validation.validate(command)
            val expected = Card(
                idGen.fromString("test-card" + baseCardsPool.id.toString()),
                "test-card",
                "desc",
                FakeClock.now().timestamp()
            )

            newCardsPool shouldBe expected
          }
        }
      }

    }

  }

  val baseCardsPool = CardsPool(
      UUID.randomUUID(),
      "test cards pool",
      "desc",
      10,
      mapOf(Pair(card1.id, card1), Pair(card2.id, card2), Pair(card3.id, card3)),
      DisplayType.ASC,
      FakeClock.now().timestamp(),
      FakeUser.id
  )

  val validation = AddCardValidation(
      getCardsPoolRepository(baseCardsPool),
      FakeClock,
      idGen
  )
}
