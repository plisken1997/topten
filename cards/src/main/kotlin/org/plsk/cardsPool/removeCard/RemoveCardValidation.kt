package org.plsk.cardsPool.removeCard

import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.promoteCard.CardNotFound
import org.plsk.cardsPool.promoteCard.CardsPoolNotFound
import org.plsk.core.validation.Validation

class RemoveCardValidation(val cardsPoolRepository: CardsPoolRepository): Validation<RemoveCard, RemoveCardValidated> {

  override fun validate(command: RemoveCard): RemoveCardValidated {
    val cardsPool = cardsPoolRepository.find(command.cardsPoolId)

    if(cardsPool == null) {
      throw CardsPoolNotFound("cards pool [${command.cardsPoolId}] not found")
    } else if (!cardsPool.cards.containsKey(command.cardId)) {
      throw CardNotFound("card [${command.cardId}] not found")
    }

    return RemoveCardValidated(command.cardId, cardsPool)
  }

}