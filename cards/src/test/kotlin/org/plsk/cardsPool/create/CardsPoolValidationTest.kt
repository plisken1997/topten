package org.plsk.cardsPool.create

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.cardsPool.CardsPool
import org.plsk.core.clock.FakeClock
import org.plsk.core.id.UUIDGen
import org.plsk.user.FakeUser
import java.util.*

class CardsPoolValidationTest : WordSpec() {

    init {

        "cards pool validation" should {

            "produce a CardsPool object when validation succeed" {
                val createCardPool = CreateCardsPool("test-name", "description !", 10, FakeUser)
                val cardsPool = cardsPoolValidator.validate(createCardPool)

                val expected = CardsPool(
                        expectedId,
                        createCardPool.name,
                        createCardPool.description,
                    10,
                        createdAt = clock.now().timestamp(),
                        createdBy = FakeUser.id
                )

                cardsPool shouldBe expected
            }

        }

    }

    val expectedId = UUID.fromString("9ff495f9-7e47-31fb-ab60-dc513af657d2")
    val idGen = UUIDGen()
    val clock = FakeClock()
    val cardsPoolValidator = CardsPoolValidation(idGen, clock)
}