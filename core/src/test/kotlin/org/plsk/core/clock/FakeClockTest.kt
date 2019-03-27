package org.plsk.core.clock

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import java.time.ZonedDateTime

class FakeClockTest : WordSpec() {

    init {
        "fake clock" should {

            "use a fixed date as the current date" {
                val expectedDate =  ZonedDateTime.parse("2013-12-27T15:35:00.000Z")
                FakeClock().now() shouldBe Datetime(expectedDate)
            }

        }
    }
}