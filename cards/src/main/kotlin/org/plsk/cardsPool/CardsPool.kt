package org.plsk.cardsPool

import org.plsk.cards.Card
import org.plsk.user.User
import java.util.*

data class CardsPool(
    val id: UUID,
    val name: String,
    val description: String?,
    val slots: Int?,
    val cards: Set<Card> = emptySet(),
    val createdAt: Long,
    val createdBy: User,
    val stock: Set<UUID> = emptySet(),
    val topCards: Set<UUID> = emptySet()) {

  fun addCard(card: Card, position: Int): CardsPool {
    if (cards.size <= position) {
      return copy(cards = cards.plus(card), stock = stock.plus(card.id))
    }
    return copy(
      cards = cards.plus(card),
      stock = stock.take(position).toSet().plus(card.id).plus(stock.drop(position))
    )
  }

  // /!\ warning /!\ this version should not be used to change the sort of a card already presents in the topCards list
  fun promote(cardId: UUID, position: Int): CardsPool {
    val cleanedTopCards = topCards.filter { it != cardId }

    if (topCards.size <= position || position < 0) {
      return copy(
        topCards = cleanedTopCards.plus(cardId).toSet(),
        stock = stock.filter{ it != cardId }.toSet()
      )
    }
    return copy(
        topCards = cleanedTopCards.take(position).toSet().plus(cardId).plus(cleanedTopCards.drop(position)),
        stock = stock.filter{ it != cardId }.toSet()
    )
  }

  fun unpromote(cardId: UUID): CardsPool =
      copy(
          stock = stock.plus(cardId),
          topCards = topCards.filter { it != cardId }.toSet()
      )

  fun moveCard(cardId: UUID, position: Int): CardsPool {
    if (!topCards.contains(cardId)) {
      // code smell ! validation must be performed outside of this function
      return this
    }
    val cleanedTopCards = topCards.filter { it != cardId }
    return copy(
      topCards = cleanedTopCards.take(position).plus(cardId).plus(cleanedTopCards.drop(position)).toSet()
    )
  }

  fun remove(cardId: UUID): CardsPool =
      copy(
          cards = cards.filter { it.id != cardId }.toSet(),
          stock = stock.filter { it != cardId }.toSet(),
          topCards = topCards.filter { it != cardId }.toSet()
      )

}
