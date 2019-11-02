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
                val createCardPool = CreateCardsPool("test-name", "description !", 10, FakeUser)
                val expectedId = UUID.fromString("9ff495f9-7e47-31fb-ab60-dc513af657d2")

                val expectedCreatedCardPoolEvent = CardsPoolCreated(
                    CardsPool(
                        expectedId,
                        createCardPool.name,
                        createCardPool.description,
                        10,
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
        override fun dispatch(event: Event) = when (event) {
            is CardsPoolCreated -> events = events.plusElement(event)
            else -> Unit
        }
    }

    val createCardsPoolHandler = CreateCardsPoolHandler(cardsPoolValidator, eventBus)
}