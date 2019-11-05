package org.plsk.cards

import java.util.*

data class Card(val id: UUID, val label: String, val description: String?, val createdAt: Long) {
    override fun toString() = label
}
