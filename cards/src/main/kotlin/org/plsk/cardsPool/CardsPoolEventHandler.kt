package org.plsk.cardsPool

import arrow.unsafe
import arrow.fx.IO
import arrow.fx.extensions.io.unsafeRun.runBlocking
import org.plsk.cardsPool.addCard.CardAddedEvent
import org.plsk.cardsPool.create.CardsPoolCreated
import org.plsk.cardsPool.promoteCard.CardPositionUpdated
import org.plsk.cardsPool.promoteCard.PromotedEvent
import org.plsk.cardsPool.removeCard.CardRemoved
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
        runBlocking(writer.updateAsync(event.cardsPool))
        event
      }
      is PromotedEvent -> {
        runBlocking(writer.updateAsync(event.cardsPool))
        event
      }
      is CardRemoved -> {
        runBlocking(writer.updateAsync(event.cardsPool))
        event
      }
      is CardPositionUpdated -> {
        runBlocking(writer.updateAsync(event.cardsPool))
        event
      }
      else -> event
    }

  fun <T>runBlocking(io: IO<T>): T = unsafe {
    runBlocking {
      io
    }
  }

}