package org.plsk.cardsPool.removeCard

import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.ValidateUser
import org.plsk.cardsPool.promoteCard.CardNotFound
import org.plsk.cardsPool.promoteCard.CardsPoolNotFound
import org.plsk.cardsPool.promoteCard.Unauthorized
import org.plsk.core.validation.Validation

class RemoveCardValidation(val cardsPoolRepository: CardsPoolRepository): Validation<RemoveCard, RemoveCardValidated>, ValidateUser {

  override suspend fun validate(command: RemoveCard): RemoveCardValidated {
    val cardsPool = cardsPoolRepository.find(command.cardsPoolId) ?: throw CardsPoolNotFound("cards pool [${command.cardsPoolId}] not found")

    if (unauthorized(command.userId, cardsPool)) {
      throw Unauthorized
    }

    if (!cardsPool.cards.containsKey(command.cardId)) {
      throw CardNotFound("card [${command.cardId}] not found")
    }

    return RemoveCardValidated(command.cardId, cardsPool)
  }

}