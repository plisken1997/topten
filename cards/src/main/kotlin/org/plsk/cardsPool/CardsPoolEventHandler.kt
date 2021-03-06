package org.plsk.cardsPool

import org.plsk.cardsPool.addCard.CardAddedEvent
import org.plsk.cardsPool.changeCardContent.ChangeCardContentValidated
import org.plsk.cardsPool.create.CardsPoolCreated
import org.plsk.cardsPool.promoteCard.CardPositionUpdated
import org.plsk.cardsPool.promoteCard.PromotedEvent
import org.plsk.cardsPool.removeCard.CardRemoved
import org.plsk.core.event.Event
import org.plsk.core.event.EventHandler

class CardsPoolEventHandler(private val writer: CardsPoolRepository): EventHandler {

  override suspend fun handle(event: Event): Event =
    when (event) {
      is CardsPoolCreated -> {
        writer.add(event.cardsPool)
        event
      }
      is CardAddedEvent -> {
        writer.update(event.cardsPool)
        event
      }
      is PromotedEvent -> {
        writer.update(event.cardsPool)
        event
      }
      is CardRemoved -> {
        writer.update(event.cardsPool)
        event
      }
      is CardPositionUpdated -> {
        writer.update(event.cardsPool)
        event
      }
      is ChangeCardContentValidated -> {
        writer.update(event.cardsPool)
        event
      }
      else -> event
    }

}