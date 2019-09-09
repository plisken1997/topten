package org.plsk.cardsPool.addCard

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.WordSpec
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.core.clock.Clock
import org.plsk.core.clock.FakeClock
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.user.FakeUser
import java.util.*

class AddCardHandlerTest: WordSpec() {

    init {

        "add card handler" When {

            "adding a card with an unknown cards pool id" should {

                "fail to validate" {
                    val command = AddCard("test-card", 1, UUID.randomUUID())
                    shouldThrowExactly<Exception>{
                        addCardHandler.handle(command)
                    }
                }
            }

            "adding a card with valid name and cards pool id" should {

                "publish a `card added` event to the event bus" {
                    val command = AddCard("test-card", 0, baseCardsPool.id)
                    addCardHandler.handle(command)

                    val newCard = AddCardValidation.createCard(command, baseCardsPool, clock)
                    val expected = CardAddedEvent(baseCardsPool.copy(cards = listOf(newCard, card1)))

                    events shouldContain expected
                }

            }
        }
    }

    val clock: Clock = FakeClock()
    val card1 = Card(UUID.randomUUID(), "test-card 1", clock.now().timestamp())

    val baseCardsPool = CardsPool(
            UUID.randomUUID(),
            "test cards pool",
            "desc",
            listOf(card1),
            clock.now().timestamp(),
            FakeUser
    )

    var events = emptyList<CardAddedEvent>()

    val addCardHandler = {

        val cardsPoolRepository: CardsPoolRepository = object: CardsPoolRepository {
            override fun find(id: UUID): CardsPool? = if (id == baseCardsPool.id) baseCardsPool else null
        }

        val validation = AddCardValidation(cardsPoolRepository, clock)

        val eventBus: EventBus = object : EventBus {
            override fun publish(event: Event) = when (event) {
                is CardAddedEvent -> events = events.plusElement(event)
                else -> Unit
            }
        }

AddCardHandler(validation, eventBus)
    }()
}