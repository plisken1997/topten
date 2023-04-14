package org.plsk.cards

import java.util.*

data class Card(val id: UUID, val label: String, val description: String?, val createdAt: Long) {
    override fun toString() = label

  fun changeContent(field: String, content: String): Card =
    when(field) {
      "label" -> copy(label = content)
      else -> throw Exception("invalid field [$field]")
    }
}
