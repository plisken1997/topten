package org.plsk.cardsPool.create

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.cardsPool.CardsPool
import org.plsk.core.clock.FakeClock
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.id.UUIDGen
import org.plsk.user.FakeUser
import java.util.*

class CreateCardsPoolHandlerTest : WordSpec () {

    init {

        "create cards pool handler" should {

            "publish the created cards pool" {
                val createCardPool = CreateCardsPool("test-name", "description !", FakeUser)
                val expectedId = UUID.fromString("604a6b1d-2315-3db5-8922-c53a3986c668")

                val expectedCreatedCardPoolEvent = CardsPoolCreated(
                    CardsPool(
                        expectedId,
                        createCardPool.name,
                        createCardPool.description,
                        createdAt = clock.now().timestamp(),
                        createdBy = FakeUser
                    )
                )

                val createdId = createCardsPoolHandler.handle(createCardPool)

                events shouldContain(expectedCreatedCardPoolEvent)

                createdId shouldBe expectedId
            }
        }

    }

    val idGen = UUIDGen()
    val clock = FakeClock()
    val cardsPoolValidator = CardsPoolValidation(idGen, clock)

    var events = emptyList<CardsPoolCreated>()

    val eventBus: EventBus = object : EventBus {
        override fun publish(event: Event) = when (event) {
            is CardsPoolCreated -> events = events.plusElement(event)
            else -> Unit
        }
    }

    val createCardsPoolHandler = CreateCardsPoolHandler(cardsPoolValidator, eventBus)
}