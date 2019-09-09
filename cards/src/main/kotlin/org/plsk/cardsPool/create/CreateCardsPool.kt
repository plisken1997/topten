package org.plsk.cardsPool.create

import org.plsk.cardsPool.CardsPool
import org.plsk.core.event.EventBus
import org.plsk.core.command.CommandHandler
import org.plsk.core.event.Event
import org.plsk.core.validation.Validation
import org.plsk.user.User
import java.util.*

data class CreateCardsPool(val name: String, val description: String?, val user: User)

data class CardsPoolCreated(val cardsPool: CardsPool): Event

class CreateCardsPoolHandler(
        private val cardsPoolValidator: Validation<CreateCardsPool, CardsPool>,
        private val eventBus: EventBus): CommandHandler<CreateCardsPool, UUID> {

    override fun handle(command: CreateCardsPool): UUID {
        val cardsPool = cardsPoolValidator.validate(command)
        eventBus.publish(CardsPoolCreated(cardsPool))
        return cardsPool.id
    }

}