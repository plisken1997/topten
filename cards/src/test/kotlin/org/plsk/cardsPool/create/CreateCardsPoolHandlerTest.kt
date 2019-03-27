package org.plsk.cardsPool.create

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.specs.WordSpec
import org.plsk.cardsPool.CardsPool
import org.plsk.core.clock.FakeClock
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus

class CreateCardsPoolHandlerTest : WordSpec () {

    init {

        "create cards pool handler" should {

            "publish the created cards pool" {
                val createCardPool = CreateCardsPool("test-name", "description !")
                val expectedCreatedCardPoolEvent = CardsPoolCreated(
                        CardsPool(
                                CardsPoolValidation.genereateId(createCardPool.name, createCardPool.description),
                                createCardPool.name,
                                createCardPool.description,
                                createdAt = clock.now().timestamp()
                        )
                )

                createCardsPoolHandler.handle(createCardPool)

                events shouldContain(expectedCreatedCardPoolEvent)
            }
        }

    }

    val clock = FakeClock()
    val cardsPoolValidator = CardsPoolValidation(clock)

    var events = listOf<CardsPoolCreated>()

    val eventBus: EventBus = object : EventBus {
        override fun publish(event: Event) = when (event) {
            is CardsPoolCreated -> events = events.plusElement(event)
            else -> Unit
        }
    }

    val createCardsPoolHandler = CreateCardsPoolHandler(cardsPoolValidator, eventBus)
}