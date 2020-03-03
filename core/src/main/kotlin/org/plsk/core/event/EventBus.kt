package org.plsk.core.event

// @todo add timestamp
interface Event

interface EventBus {
  suspend fun dispatch(event: Event): Unit
}

class SyncEventBus(private val handlers: List<EventHandler>): EventBus {
  override suspend fun dispatch(event: Event): Unit = handlers.forEach { it.handle(event) }
}