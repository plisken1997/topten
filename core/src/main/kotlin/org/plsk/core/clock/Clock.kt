package org.plsk.core.clock

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

data class Datetime(val date: ZonedDateTime) {
  fun timestamp(): Long = date.toInstant().epochSecond
  fun isBefore(d: Datetime): Boolean = timestamp() < d.timestamp()

  companion object {
    fun fromTimestamp(timestamp: Long): Datetime = Datetime(ZonedDateTime.from(Instant.ofEpochSecond(timestamp).atZone(ZoneId.of("Z"))))
  }
}

interface Clock {
  fun now(): Datetime
  fun plusDays(days: Long) = Datetime(now().date.plusDays(days))
}

object FakeClock: Clock {
    val fakeNow = Datetime(ZonedDateTime.parse("2013-12-27T15:35:00.000Z"))
    override fun now(): Datetime = fakeNow
}

object UTCDatetimeClock : Clock {
    override fun now(): Datetime = Datetime(ZonedDateTime.now(ZoneId.of("UTC")))
}
