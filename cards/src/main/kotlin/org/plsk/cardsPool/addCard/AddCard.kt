package org.plsk.cardsPool.addCard

import org.plsk.cardsPool.CardsPool
import org.plsk.core.command.CommandHandler
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.validation.Validation
import java.util.*

data class AddCard(val label: String, val position: Int, val cardsPoolId: UUID)

data class CardAddedEvent(val cardsPool: CardsPool): Event

class AddCardHandler(private val cardValidator: Validation<AddCard, CardsPool>, private val eventBus: EventBus): CommandHandler<AddCard> {

    override fun handle(command: AddCard) {
        val cardsPool = cardValidator.validate(command)
        eventBus.publish(CardAddedEvent(cardsPool))
    }

}