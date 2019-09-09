package org.plsk.cardsPool.addCard

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.WordSpec
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.core.clock.Clock
import org.plsk.core.clock.FakeClock
import org.plsk.user.FakeUser
import java.util.*

class AddCardValidationTest: WordSpec() {

    init {

        "add card validation" When {

            "validate an unknown cards pool" should {

                "fail to validate" {
                    val command = AddCard("insert-1", 1, UUID.nameUUIDFromBytes("test".toByteArray()))
                    // @todo will be better tested after implemented the monadic effect on validation output
                    shouldThrowExactly<Exception>{
                        validation.validate(command)
                    }
                }
            }

            "validate an existing label" should {

                "fail to validate" {
                    val command = AddCard("test-card 2", 1, baseCardsPool.id)
                    // @todo will be better tested after implemented the monadic effect on validation output
                    shouldThrowExactly<Exception>{
                        validation.validate(command)
                    }
                }

            }

            "validate the card" should {

                "add the card to the cards pool" {
                    val command = AddCard("test-card", 1, baseCardsPool.id)
                    val newCardsPool = validation.validate(command)
                    val card = Card(
                        AddCardValidation.genereateId("test-card", baseCardsPool),
                        "test-card",
                        clock.now().timestamp()
                    )
                    val expected = CardsPool(
                            baseCardsPool.id,
                            "test cards pool",
                            "desc",
                            listOf(card1, card, card2, card3),
                            clock.now().timestamp(),
                            FakeUser
                    )

                    newCardsPool shouldBe expected
                }
            }

        }

    }

    val clock: Clock = FakeClock()

    val card1 = Card(UUID.randomUUID(), "test-card 1", clock.now().timestamp())
    val card2 = Card(UUID.randomUUID(), "test-card 2", clock.now().timestamp())
    val card3 = Card(UUID.randomUUID(), "test-card 3", clock.now().timestamp())

    val baseCardsPool = CardsPool(
            UUID.randomUUID(),
            "test cards pool",
            "desc",
            listOf(card1, card2, card3),
            clock.now().timestamp(),
            FakeUser
    )

    val cardsPoolRepository: CardsPoolRepository = object: CardsPoolRepository{
        override fun find(id: UUID): CardsPool? = if (id == baseCardsPool.id) baseCardsPool else null
    }
    val validation = AddCardValidation(cardsPoolRepository, clock)
}