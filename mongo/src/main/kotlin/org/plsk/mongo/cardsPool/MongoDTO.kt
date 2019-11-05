package org.plsk.mongo.cardsPool

import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.user.UnknownUser
import java.util.*

data class MongoCard(val id: String, val label: String, val description: String?, val createdAt: Long) {
  fun toModel(): Card = Card(UUID.fromString(id), label, description, createdAt)
}

data class MongoCardsPool(
    val id: String,
    val name: String,
    val description: String?,
    val slots: Int?,
    val cards: List<MongoCard> = emptyList(),
    val createdAt: Long,
    val createdBy: String,
    val stock: List<String> = emptyList(),
    val topCards: List<String> = emptyList()) {

  fun toModel(): CardsPool =
      CardsPool(
          UUID.fromString(id),
          name,
          description,
          slots,
          cards.map{ card -> Pair(UUID.fromString(card.id), card.toModel()) }.toMap(),
          createdAt,
          UnknownUser(createdBy),
          stock.map{ UUID.fromString(it)}.toSet(),
          topCards.map{ UUID.fromString(it)}.toSet()
      )
}

fun Card.toDTO(): MongoCard = MongoCard(this.id.toString(), this.label, this.description, this.createdAt)

fun CardsPool.toDTO(): MongoCardsPool =
    MongoCardsPool(
        this.id.toString(),
        this.name,
        this.description,
        this.slots,
        this.cards.map{ it.value.toDTO() },
        this.createdAt,
        this.createdBy.id,
        this.stock.map { it.toString() },
        this.topCards.map { it.toString() }
    )