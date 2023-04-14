package org.plsk.core.id

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import java.util.*

class FakeClockTest : WordSpec() {

    init {

        val generator = UUIDGen()

        "UUID gen" should {

            "generate a uuid from a string" {
                generator.fromString("id1") shouldBe UUID.fromString("fafe1b60-c241-37cc-98f4-562213e44849")
            }

        }

    }
}