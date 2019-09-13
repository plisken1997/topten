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
    val createdBy: User,
    val stock: List<UUID> = emptyList(),
    val topCards: List<UUID> = emptyList()) {

  fun addCard(card: Card, position: Int): CardsPool {
    if (cards.size <= position) {
      return copy(cards = cards.plus(card), stock = stock.plus(card.id))
    }
    return copy(
      cards = cards.plus(card),
      stock = stock.take(position).plus(card.id).plus(stock.drop(position))
    )
  }

  fun promote(cardId: UUID, position: Int): CardsPool {
    if (topCards.size <= position || position < 0) {
      return copy(topCards = topCards.plus(cardId))
    }
    return copy(
        topCards = topCards.take(position).plus(cardId).plus(topCards.drop(position))
    )
  }

}
