package org.plsk.cardsPool.addCard

import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.core.command.CommandHandler
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.validation.Validation
import java.util.*

data class AddCard(val title: String, val description: String?, val position: Int, val cardsPoolId: UUID)

data class CardAddedEvent(val cardsPool: CardsPool): Event

class AddCardAction(
    private val cardValidator: Validation<AddCard, Card>,
    private val cardsPoolRepository: CardsPoolRepository,
    private val eventBus: EventBus
): CommandHandler<AddCard, UUID> {

  override suspend fun handle(command: AddCard): UUID {
    val card = cardValidator.validate(command)

    val cardsPool = cardsPoolRepository.find(command.cardsPoolId)
    if (cardsPool != null) {
      val updatedCardsPool = cardsPool.addCard(
          card,
          command.position
      )
      eventBus.dispatch(CardAddedEvent(updatedCardsPool))
      return card.id
    } else {
      throw Exception("could not find cardsPool")
    }
  }

}