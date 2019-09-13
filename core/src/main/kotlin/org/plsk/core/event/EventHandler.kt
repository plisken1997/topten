package org.plsk.core.event

interface EventHandler {
  fun handle(event: Event): Event
}