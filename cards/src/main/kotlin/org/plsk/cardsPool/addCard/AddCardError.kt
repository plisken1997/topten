package org.plsk.cardsPool.addCard

import java.util.*


sealed class AddCardError

data class CardsPoolNotFound(val id: UUID) : AddCardError() {
    override fun toString(): String = "cards pool not found for id ${id}"
}

data class LabelExists(val label: String) : AddCardError() {
    override fun toString(): String = "label [${label}] already exists"
}