package org.plsk.cardsPool.removeCard

import org.plsk.cardsPool.CardsPool
import org.plsk.core.command.CommandHandler
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.validation.Validation
import java.util.*

data class RemoveCard(val cardId: UUID, val cardsPoolId: UUID, val userId: String)

data class RemoveCardValidated(val cardId: UUID, val cardsPool: CardsPool)

data class CardRemoved(val cardsPool: CardsPool, val cardId: UUID): Event

class RemoveCardAction(
  val validation: Validation<RemoveCard, RemoveCardValidated>,
  private val eventBus: EventBus
): CommandHandler<RemoveCard, Unit> {

  override suspend fun handle(command: RemoveCard) {
    val validated = validation.validate(command)
    val removed = validated.cardsPool.remove(validated.cardId)
    eventBus.dispatch(CardRemoved(removed, validated.cardId))
    return Unit
  }
}