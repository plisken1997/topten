package org.plsk.core.id

import java.util.*

class UUIDGen: IdGen<UUID> {
    override fun fromString(str: String): UUID = fromBytes(str.toByteArray())

    override fun fromBytes(b: ByteArray): UUID = UUID.nameUUIDFromBytes(b)

    override fun random(): UUID = UUID.randomUUID()
}