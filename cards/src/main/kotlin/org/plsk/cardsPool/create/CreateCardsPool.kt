package org.plsk.cardsPool.create

import org.plsk.cardsPool.CardsPool
import org.plsk.core.event.EventBus
import org.plsk.core.command.CommandHandler
import org.plsk.core.event.Event
import org.plsk.core.validation.Validation
import org.plsk.user.User
import java.util.*

data class CreateCardsPool(val name: String, val description: String?, val slots: Int?, val user: User)

data class CardsPoolCreated(val cardsPool: CardsPool): Event

class CreateCardsPoolAction(
        private val cardsPoolValidator: Validation<CreateCardsPool, CardsPool>,
        private val eventBus: EventBus): CommandHandler<CreateCardsPool, UUID> {

    override suspend fun handle(command: CreateCardsPool): UUID {
        val cardsPool = cardsPoolValidator.validate(command)
        eventBus.dispatch(CardsPoolCreated(cardsPool))
        return cardsPool.id
    }

}