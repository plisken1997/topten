package org.plsk.cardsPool

import org.plsk.cardsPool.addCard.CardAddedEvent
import org.plsk.cardsPool.create.CardsPoolCreated
import org.plsk.cardsPool.promoteCard.CardPositionUpdated
import org.plsk.cardsPool.promoteCard.PromotedEvent
import org.plsk.cardsPool.removeCard.CardRemoved
import org.plsk.core.dao.DataWriter
import org.plsk.core.event.Event
import org.plsk.core.event.EventHandler
import org.plsk.core.async.RunAsync

class CardsPoolEventHandler(private val writer: DataWriter<CardsPool, WriteResult>): EventHandler, RunAsync {

  override suspend fun handle(event: Event): Event = runAsync {
    when (event) {
      is CardsPoolCreated -> {
        writer.store(event.cardsPool)
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
      else -> event
    }
  }

}