package org.plsk.cardsPool

import org.plsk.cardsPool.addCard.CardAddedEvent
import org.plsk.cardsPool.create.CardsPoolCreated
import org.plsk.cardsPool.promoteCard.CardPromoted
import org.plsk.core.dao.DataWriter
import org.plsk.core.event.Event
import org.plsk.core.event.EventHandler

class CardsPoolEventHandler(private val writer: DataWriter<CardsPool, WriteResult>): EventHandler {

  override fun handle(event: Event): Event =
    when (event) {
      is CardsPoolCreated -> {
        writer.store(event.cardsPool)
        event
      }
      is CardAddedEvent -> {
        writer.update(event.cardsPool)
        event
      }
      is CardPromoted -> {
        writer.update(event.cardPool)
        event
      }
      else -> event
    }
}