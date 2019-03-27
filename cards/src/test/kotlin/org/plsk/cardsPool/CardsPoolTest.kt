package org.plsk.cardsPool

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.cards.Card
import org.plsk.core.clock.FakeClock
import java.util.*

class CardsPoolTest  : WordSpec() {

    init {

        "cards pool" should {

            "add a new card at the end of the list" {
                val inserted = baseCardsPool.addCard(card, 10)
                inserted.cards shouldBe listOf<Card>(
                        card1,
                        card2,
                        card3,
                        card4,
                        card5,
                        card
                )
            }

            "add a new card at the beginning of the list" {
                val inserted = baseCardsPool.addCard(card, 0)
                inserted.cards shouldBe listOf<Card>(
                        card,
                        card1,
                        card2,
                        card3,
                        card4,
                        card5
                )

            }

            "add a new card in the middle of the list" {
                val inserted = baseCardsPool.addCard(card, 3)
                inserted.cards shouldBe listOf<Card>(
                        card1,
                        card2,
                        card3,
                        card,
                        card4,
                        card5
                )

            }

        }
    }

    val clock = FakeClock()

    val card = Card(UUID.randomUUID(), "test-card", clock.now().timestamp())

    val card1 = Card(UUID.randomUUID(), "test-card 1", clock.now().timestamp())
    val card2 = Card(UUID.randomUUID(), "test-card 2", clock.now().timestamp())
    val card3 = Card(UUID.randomUUID(), "test-card 3", clock.now().timestamp())
    val card4 = Card(UUID.randomUUID(), "test-card 4", clock.now().timestamp())
    val card5 = Card(UUID.randomUUID(), "test-card 5", clock.now().timestamp())

    val cards = listOf<Card>(
        card1,
        card2,
        card3,
        card4,
        card5
    )

    val baseCardsPool = CardsPool(
        UUID.randomUUID(),
"test cards pool",
"desc",
        cards,
        clock.now().timestamp()
    )
}
