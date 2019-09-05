package org.plsk.core.clock

import java.time.ZonedDateTime

data class Datetime(val date: ZonedDateTime) {
    fun timestamp(): Long = date.toInstant().epochSecond
}

interface Clock {
    fun now(): Datetime
}

class FakeClock: Clock {
    val fakeNow = Datetime(ZonedDateTime.parse("2013-12-27T15:35:00.000Z"))
    override fun now(): Datetime = fakeNow
}