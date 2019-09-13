package org.plsk.cardsPool.promoteCard

import org.plsk.cardsPool.CardsPool
import org.plsk.core.command.CommandHandler
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.validation.Validation
import java.util.*

data class PromoteCard(val cardId: UUID, val position: Int, val cardsPoolId: UUID)

data class CardPromoted(val cardId: UUID, val position: Int, val cardPool: CardsPool): Event

class PromoteCardHander(
    private val validation: Validation<PromoteCard, PromoteCardValidated>,
    private val eventBus: EventBus
): CommandHandler<PromoteCard, List<UUID>> {

  override fun handle(command: PromoteCard): List<UUID> {
    val validated = validation.validate(command)
    val promotedCardsPool = validated.cardsPool.promote(command.cardId, command.position)

    eventBus.dispatch(CardPromoted(command.cardId, command.position, promotedCardsPool))

    return promotedCardsPool.topCards
  }
}