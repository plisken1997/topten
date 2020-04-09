package org.plsk.cardsPool.changeCardContent

import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.ValidateUser
import org.plsk.cardsPool.addCard.CardsPoolNotFound
import org.plsk.cardsPool.addCard.Unauthorized
import org.plsk.core.validation.Validation

class ValidateChangeCardContent(
    private val cardsPoolRepository: CardsPoolRepository
): Validation<ChangeCardContent, ChangeCardContentValidated>, ValidateUser {

  override suspend fun validate(command: ChangeCardContent): ChangeCardContentValidated =
    cardsPoolRepository.find(command.cardsPoolId)?.let {
      cardsPool ->
        if (unauthorized(command.userId, cardsPool)) {
          throw Exception(Unauthorized.toString())
        }
        ChangeCardContentValidated(
            command.cardId,
            cardsPool.changeCardContent(command.cardId, command.field, command.value)
        )
    } ?: throw Exception(CardsPoolNotFound(command.cardsPoolId).toString())

}
