package org.plsk.core.event

interface Event

interface EventBus {

    fun publish(event: Event)

}