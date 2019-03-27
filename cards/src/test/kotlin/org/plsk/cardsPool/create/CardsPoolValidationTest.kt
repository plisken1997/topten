package org.plsk.cardsPool.create

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.cardsPool.CardsPool
import org.plsk.core.clock.FakeClock

class CardsPoolValidationTest : WordSpec() {

    init {

        "cards pool validation" should {

            "produce a CardsPool object when validation succeed" {
                val createCardPool = CreateCardsPool("test-name", "description !")
                val cardsPool = cardsPoolValidator.validate(createCardPool)

                val expected = CardsPool(
                        CardsPoolValidation.genereateId(createCardPool.name, createCardPool.description),
                        createCardPool.name,
                        createCardPool.description,
                        createdAt = clock.now().timestamp()
                )

                cardsPool shouldBe expected
            }

        }

    }

    val clock = FakeClock()
    val cardsPoolValidator = CardsPoolValidation(clock)
}