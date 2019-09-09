package org.plsk.core.id

interface IdGen<IdType> {

    fun random(): IdType

    fun fromString(str: String): IdType

    fun fromBytes(b: ByteArray): IdType

}