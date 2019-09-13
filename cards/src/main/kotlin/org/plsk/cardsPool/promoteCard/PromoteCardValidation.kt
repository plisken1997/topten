package org.plsk.cardsPool.promoteCard

import arrow.data.extensions.list.foldable.exists
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.core.validation.Validation
import java.util.*

data class PromoteCardValidated(val cardId: UUID, val position: Int, val cardsPool: CardsPool)

abstract class PromoteCardError(msg: String?, cause: Throwable?): Throwable(msg, cause)

data class CardsPoolNotFound(val msg: String): PromoteCardError(msg, null)
data class CardNotFound(val msg: String): PromoteCardError(msg, null)

class PromoteCardValidation(val cardsPoolRepository: CardsPoolRepository): Validation<PromoteCard, PromoteCardValidated> {

  override fun validate(command: PromoteCard): PromoteCardValidated {
    val cardsPool = cardsPoolRepository.find(command.cardsPoolId)

    if(cardsPool == null) {
      throw CardsPoolNotFound("cards pool [${command.cardsPoolId}] not found")
    } else if (!cardsPool.cards.exists { it.id == command.cardId }) {
      throw CardNotFound("card [${command.cardId}] not found")
    }

    return PromoteCardValidated(command.cardId, command.position, cardsPool)
  }

}