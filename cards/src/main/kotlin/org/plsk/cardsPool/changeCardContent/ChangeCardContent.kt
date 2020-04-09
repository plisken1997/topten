package org.plsk.cardsPool.changeCardContent

import org.plsk.cardsPool.CardsPool
import org.plsk.core.command.CommandHandler
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.validation.Validation
import java.util.*

data class ChangeCardContent(val cardId: UUID, val cardsPoolId: UUID, val field: String, val value: String, val userId: String)
data class ChangeCardContentValidated(val cardId: UUID, val cardsPool: CardsPool): Event

class UpdateCardAction(
    private val validate: Validation<ChangeCardContent, ChangeCardContentValidated>,
    private val eventBus: EventBus): CommandHandler<ChangeCardContent, Unit> {

  override suspend fun handle(command: ChangeCardContent): Unit =
     validate.validate(command).let{ eventBus.dispatch(it) }
}
