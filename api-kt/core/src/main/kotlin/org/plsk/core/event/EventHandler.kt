package org.plsk.core.event

interface EventHandler {
  suspend fun handle(event: Event): Event
}