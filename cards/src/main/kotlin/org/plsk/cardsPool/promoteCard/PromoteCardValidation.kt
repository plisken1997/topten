package org.plsk.cardsPool.promoteCard

import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.ValidateUser
import org.plsk.core.validation.Validation
import java.util.*

data class PromoteCardValidated(val cardId: UUID, val cardsPool: CardsPool)

abstract class PromoteCardError(msg: String?, cause: Throwable?): Throwable(msg, cause)

data class CardsPoolNotFound(val msg: String): PromoteCardError(msg, null)
data class CardNotFound(val msg: String): PromoteCardError(msg, null)

object Unauthorized: PromoteCardError("Unauthorized", null)

class PromoteCardValidation(val cardsPoolRepository: CardsPoolRepository): Validation<PromoteType, PromoteCardValidated>, ValidateUser {

  override suspend fun validate(command: PromoteType): PromoteCardValidated {
    val cardsPool = cardsPoolRepository.find(command.cardsPoolId) ?: throw CardsPoolNotFound("cards pool [${command.cardsPoolId}] not found")

    if (unauthorized(command.userId, cardsPool)) {
      throw org.plsk.cardsPool.promoteCard.Unauthorized
    }

    if (!cardsPool.cards.containsKey(command.cardId)) {
      throw CardNotFound("card [${command.cardId}] not found")
    }

    return PromoteCardValidated(command.cardId, cardsPool)
  }

}