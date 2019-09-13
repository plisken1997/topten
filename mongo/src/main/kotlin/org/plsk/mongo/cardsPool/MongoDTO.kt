package org.plsk.mongo.cardsPool

import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.user.UnknownUser
import java.util.*

data class MongoCard(val id: String, val label: String, val createdAt: Long) {
  fun toModel(): Card = Card(UUID.fromString(id), label, createdAt)
}

data class MongoCardsPool(
    val id: String,
    val name: String,
    val description: String?,
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
          cards.map{ it.toModel() },
          createdAt,
          UnknownUser(createdBy),
          stock.map{ UUID.fromString(it)},
          topCards.map{ UUID.fromString(it)}
      )
}

fun Card.toDTO(): MongoCard = MongoCard(this.id.toString(), this.label, this.createdAt)

fun CardsPool.toDTO(): MongoCardsPool =
    MongoCardsPool(
        this.id.toString(),
        this.name,
        this.description,
        this.cards.map{ it.toDTO() },
        this.createdAt,
        this.createdBy.id,
        this.stock.map { it.toString() },
        this.topCards.map { it.toString() }
    )