package org.plsk.cardsPool.addCard

import arrow.core.Option
import arrow.core.getOrElse
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

class AddCardHandler(
    private val cardValidator: Validation<AddCard, Card>,
    private val cardsPoolRepository: CardsPoolRepository,
    private val eventBus: EventBus
): CommandHandler<AddCard, UUID> {

  override fun handle(command: AddCard): UUID {
    val card = cardValidator.validate(command)

    return Option.fromNullable(cardsPoolRepository.find(command.cardsPoolId))
      .map { cardsPool ->
        val updatedCardsPool = cardsPool.addCard(
            card,
            command.position
        )
        eventBus.dispatch(CardAddedEvent(updatedCardsPool))
        card.id
      }.getOrElse{
        throw Exception("could not find cardsPool")
      }
  }

}