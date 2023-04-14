package org.plsk.core.clock

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import java.time.ZonedDateTime

class DatetimeTest: WordSpec() {

  init {

    "datetime" should {

      "be created from a timestamp" {
        Datetime.fromTimestamp(expectedTimestamp) shouldBe now
      }

      "should occured before the other date" {
        val tomorrow = Datetime(ZonedDateTime.parse("2013-12-28T15:35:00.000Z"))
        now.isBefore(tomorrow) shouldBe true
      }

      "should occurs after the other date" {
        val yesterday = Datetime(ZonedDateTime.parse("2013-12-26T15:35:00.000Z"))
        now.isBefore(yesterday) shouldBe false
      }
    }

  }

  val now = Datetime(ZonedDateTime.parse("2013-12-27T15:35:00.000Z"))
  val expectedTimestamp = 1388158500L
}
