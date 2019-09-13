package org.plsk.core.event

// @todo add timestamp
interface Event

interface EventBus {
  fun dispatch(event: Event)
}

class SyncEventBus(private val handlers: List<EventHandler>): EventBus {
  override fun dispatch(event: Event) = handlers.forEach { it.handle(event) }
}