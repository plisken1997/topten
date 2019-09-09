package org.plsk.cardsPool

import org.plsk.cards.Card
import org.plsk.user.User
import java.util.*

data class CardsPool(
        val id: UUID,
        val name: String,
        val description: String?,
        val cards: List<Card> = emptyList(),
        val createdAt: Long,
        val createdBy: User) {

    fun addCard(card: Card, position: Int): CardsPool {
        if (cards.size <= position) {
            return this.copy(cards = cards.plus(card))
        }
        return copy(cards = cards.take(position).plus(card).plus(cards.drop(position)))
    }

}
