package org.plsk.cardsPool

import arrow.fx.IO
import org.plsk.cardsPool.addCard.CardAddedEvent
import org.plsk.cardsPool.create.CardsPoolCreated
import org.plsk.cardsPool.promoteCard.CardPositionUpdated
import org.plsk.cardsPool.promoteCard.PromotedEvent
import org.plsk.cardsPool.removeCard.CardRemoved
import org.plsk.core.dao.DataWriter
import org.plsk.core.event.Event
import org.plsk.core.event.EventHandler
import kotlinx.coroutines.*

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

        runBlocking(
            IO.async<Unit> {
              println("hello")
              Thread.sleep(3000)
              println("end hello")
              Unit
            }
        )
        runBlocking(writer.updateAsync(event.cardsPool).map {
          r ->
          println("coucou")
          Thread.sleep(1000)
          println("end coucou")
          r
        })
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

  fun <T>runBlocking(io: IO<T>): Job = GlobalScope.launch {
    io.suspended()
  }

}