package org.plsk.cardsPool

import org.plsk.cards.Card
import org.plsk.user.User
import java.util.*

data class CardsPool(
    val id: UUID,
    val name: String,
    val description: String?,
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
    if (topCards.size <= position || position < 0) {
      return copy(
        topCards = topCards.plus(cardId),
          stock = stock.filter{ it != cardId }.toSet()
      )
    }
    return copy(
        topCards = topCards.take(position).toSet().plus(cardId).plus(topCards.drop(position)),
        stock = stock.filter{ it != cardId }.toSet()
    )
  }

  fun unpromote(cardId: UUID): CardsPool =
      copy(
          id,
          name,
          description,
          cards,
          createdAt,
          createdBy,
          stock.plus(cardId),
          topCards.filter { it != cardId }.toSet()
      )

  fun remove(cardId: UUID): CardsPool =
      copy(
          id,
          name,
          description,
          cards.filter { it.id != cardId }.toSet(),
          createdAt,
          createdBy,
          stock.filter { it != cardId }.toSet(),
          topCards.filter { it != cardId }.toSet()
      )

}
