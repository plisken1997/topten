package org.plsk.cardsPool.addCard

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.WordSpec
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.WriteResult
import org.plsk.core.clock.Clock
import org.plsk.core.clock.FakeClock
import org.plsk.core.id.UUIDGen
import org.plsk.user.FakeUser
import java.util.*

class AddCardValidationTest: WordSpec() {

    init {

        "add card validation" When {

            "validate an unknown cards pool" should {

                "fail to validate" {
                    val command = AddCard("insert-1", "desc", 1, UUID.nameUUIDFromBytes("test".toByteArray()))
                    // @todo will be better tested after implemented the monadic effect on validation output
                    shouldThrowExactly<Exception>{
                        validation.validate(command)
                    }
                }
            }

            "validate an existing label" should {

                "fail to validate" {
                    val command = AddCard("test-card 2", "desc",1, baseCardsPool.id)
                    // @todo will be better tested after implemented the monadic effect on validation output
                    shouldThrowExactly<Exception>{
                        validation.validate(command)
                    }
                }

            }

            "validate the card" should {

                "add the card to the cards pool" {
                    val command = AddCard("test-card", "desc",1, baseCardsPool.id)
                    val newCardsPool = validation.validate(command)
                    val expected = Card(
                        idGen.fromString("test-card" + baseCardsPool.id.toString()),
                        "test-card",
                        "desc",
                        clock.now().timestamp()
                    )

                    newCardsPool shouldBe expected
                }
            }

        }

    }

    val clock: Clock = FakeClock()
    val idGen = UUIDGen()

    val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", clock.now().timestamp())
    val card2 = Card(UUID.randomUUID(), "test-card 2", "desc", clock.now().timestamp())
    val card3 = Card(UUID.randomUUID(), "test-card 3", "desc", clock.now().timestamp())

    val baseCardsPool = CardsPool(
            UUID.randomUUID(),
            "test cards pool",
            "desc",
        10,
            mapOf(Pair(card1.id, card1), Pair(card2.id, card2), Pair(card3.id, card3)),
            clock.now().timestamp(),
            FakeUser
    )

    val cardsPoolRepository: CardsPoolRepository = object: CardsPoolRepository{
      override fun update(data: CardsPool): WriteResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
      }

      override fun store(data: CardsPool): WriteResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
      }

      override fun find(id: UUID): CardsPool? = if (id == baseCardsPool.id) baseCardsPool else null
    }
    val validation = AddCardValidation(cardsPoolRepository, clock, idGen)
}