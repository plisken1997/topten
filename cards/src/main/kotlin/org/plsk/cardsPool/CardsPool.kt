package org.plsk.cardsPool

import arrow.core.getOption
import arrow.syntax.collections.flatten
import org.plsk.cards.Card
import org.plsk.cardsPool.promoteCard.CardNotFound
import java.util.*

enum class DisplayType { ASC, DESC }

data class CardsPool(
    val id: UUID,
    val name: String,
    val description: String?,
    val slots: Int?,
    val cards: Map<UUID, Card> = emptyMap(),
    val display: DisplayType?,
    val createdAt: Long,
    val createdBy: String,
    val stock: Set<UUID> = emptySet(),
    val topCards: Set<UUID> = emptySet()) {

  fun addCard(card: Card, position: Int): CardsPool {
    if (cards.size <= position) {
      return copy(cards = cards.plus(Pair(card.id, card)), stock = stock.plus(card.id))
    }
    return copy(
      cards = cards.plus(Pair(card.id, card)),
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
          cards = cards.filterKeys { it != cardId },
          stock = stock.filter { it != cardId }.toSet(),
          topCards = topCards.filter { it != cardId }.toSet()
      )

  fun changeCardContent(cardId: UUID, field: String, content: String): CardsPool =
    find(cardId)?.let {
      card ->
        val updated = card.changeContent(field, content)
      val cards = cards.filterKeys { it != cardId }.plus(Pair(cardId, updated))
      copy(cards = cards)
    } ?: throw CardNotFound("card [${cardId}] not found")

  fun contains(cardId: UUID): Boolean = cards.containsKey(cardId)

  fun find(cardId: UUID): Card? = cards.get(cardId)

  fun getHighlighted(): Set<Card> = topCards.map{ cards.getOption(it) }.flatten().toSet()

  fun getPool(): Set<Card> = stock.map{ cards.getOption(it) }.flatten().toSet()
}
