package org.plsk.cardsPool.promoteCard

import org.plsk.cardsPool.CardsPool
import org.plsk.core.command.CommandHandler
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.validation.Validation
import java.util.*

interface PromoteType{
  val cardId: UUID
  val cardsPoolId: UUID
  val userId: String
}

data class PromoteCard(override val cardId: UUID, val position: Int, override val cardsPoolId: UUID, override val userId: String): PromoteType
data class UpdateCardPosition(override val cardId: UUID, val position: Int, override val cardsPoolId: UUID, override val userId: String): PromoteType
data class UnpromoteCard(override val cardId: UUID, override val cardsPoolId: UUID, override val userId: String): PromoteType

sealed class PromotedEvent: Event {
  abstract val cardsPool: CardsPool
}

data class CardPromoted(val cardId: UUID, val position: Int, override val cardsPool: CardsPool): PromotedEvent()
data class CardUnpromoted(val cardId: UUID, override val cardsPool: CardsPool): PromotedEvent()
data class CardPositionUpdated(val cardId: UUID, val position: Int,  override val cardsPool: CardsPool): PromotedEvent()

class PromoteCardAction(
    private val validation: Validation<PromoteType, PromoteCardValidated>,
    private val eventBus: EventBus
): CommandHandler<PromoteType, Set<UUID>> {

  override suspend fun handle(command: PromoteType): Set<UUID> {
    val validated: PromoteCardValidated = validation.validate(command)

    val promoteEvent: PromotedEvent = when(command) {
      is PromoteCard -> {
        val updatedCardsPool = validated.cardsPool.promote(command.cardId, command.position)
        CardPromoted(command.cardId, command.position, updatedCardsPool)
      }
      is UnpromoteCard -> {
        val updatedCardsPool = validated.cardsPool.unpromote(command.cardId)
        CardUnpromoted(command.cardId, updatedCardsPool)
      }
      is UpdateCardPosition -> {
        val updatedCardsPool = validated.cardsPool.moveCard(command.cardId, command.position)
        CardPositionUpdated(command.cardId, command.position, updatedCardsPool)
      }
      else -> throw Exception("invalid PromoteType")
    }
    eventBus.dispatch(promoteEvent)

    return promoteEvent.cardsPool.topCards
  }
}