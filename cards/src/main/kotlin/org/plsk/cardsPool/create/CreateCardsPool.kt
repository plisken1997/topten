package org.plsk.cardsPool.create

import org.plsk.cardsPool.CardsPool
import org.plsk.core.event.EventBus
import org.plsk.core.command.CommandHandler
import org.plsk.core.event.Event
import org.plsk.core.validation.Validation

data class CreateCardsPool(val name: String, val description: String?)

data class CardsPoolCreated(val cardsPool: CardsPool): Event

class CreateCardsPoolHandler(val cardsPoolValidator: Validation<CreateCardsPool, CardsPool>, val eventBus: EventBus): CommandHandler<CreateCardsPool> {

    override fun handle(command: CreateCardsPool) {
        val cardsPool = cardsPoolValidator.validate(command)
        eventBus.publish(CardsPoolCreated(cardsPool))
    }

}