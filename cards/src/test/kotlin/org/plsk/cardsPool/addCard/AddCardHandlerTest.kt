package org.plsk.cardsPool.addCard

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.WordSpec
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.WriteResult
import org.plsk.core.clock.Clock
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.QueryFilter
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.id.UUIDGen
import org.plsk.user.FakeUser
import java.util.*

class AddCardHandlerTest: WordSpec() {

    init {

        "add card handler" When {

            "adding a card with an unknown cards pool id" should {

                "fail to validate" {
                    val command = AddCard("test-card", "desc", 1, UUID.randomUUID())
                    shouldThrowExactly<Exception>{
                        addCardHandler.handle(command)
                    }
                }
            }

            "adding a card with valid name and cards pool id" should {

                "publish a `card added` event to the event bus" {
                    val command = AddCard("test-card", "desc", 0, baseCardsPool.id)
                    val createdId = addCardHandler.handle(command)

                    val expectedId = idGen.fromString(command.title + baseCardsPool.id.toString())

                    createdId shouldBe expectedId

                    val newCard = AddCardValidation.createCard(command, clock, expectedId)
                    val expected = CardAddedEvent(baseCardsPool.copy(
                        cards = mapOf(Pair(card1.id, card1), Pair(newCard.id, newCard)),
                        stock = setOf(newCard.id, card1.id)
                    ))

                    events shouldContain expected
                }

            }
        }
    }

    val clock: Clock = FakeClock()
    val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", clock.now().timestamp())

    val baseCardsPool = CardsPool(
            UUID.randomUUID(),
            "test cards pool",
            "desc",
        10,
            mapOf(Pair(card1.id, card1)),
            clock.now().timestamp(),
            FakeUser,
            stock = setOf(card1.id)
    )

    var events = emptyList<CardAddedEvent>()

    val idGen = UUIDGen()

    val addCardHandler = {

        val cardsPoolRepository: CardsPoolRepository = object: CardsPoolRepository {
          override fun findAll(filter: Iterable<QueryFilter>): List<CardsPool> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
          }

          override fun update(data: CardsPool): WriteResult {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
          }

          override fun store(data: CardsPool): WriteResult {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
          }

          override fun find(id: UUID): CardsPool? = if (id == baseCardsPool.id) baseCardsPool else null
        }

        val validation = AddCardValidation(cardsPoolRepository, clock, idGen)

        val eventBus: EventBus = object : EventBus {
            override fun dispatch(event: Event) = when (event) {
                is CardAddedEvent -> events = events.plusElement(event)
                else -> Unit
            }
        }

        AddCardHandler(validation, cardsPoolRepository, eventBus)
    }()
}