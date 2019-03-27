package org.plsk.cardsPool.create

import org.plsk.cards.Card
import java.util.*

data class CardsPool(val id: UUID, val name: String, val description: String?, val cards: List<Card> = listOf(), val createdAt: Long)
