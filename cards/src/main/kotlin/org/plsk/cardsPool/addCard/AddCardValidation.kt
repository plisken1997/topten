package org.plsk.cardsPool.addCard

import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.CardsPool
import org.plsk.core.clock.Clock
import org.plsk.core.id.IdGen
import org.plsk.core.validation.Validation
import java.util.*

class AddCardValidation(val cardsPoolRepository: CardsPoolRepository, val clock: Clock, val idGen: IdGen<UUID>) : Validation<AddCard, Card> {

    private fun genereateId(label: String, cardsPool: CardsPool): UUID = idGen.fromString(label + cardsPool.id.toString())

    companion object {

        fun createCard(command: AddCard, clock: Clock, id: UUID): Card =
            Card(
                id,
                command.title,
                command.description,
                clock.now().timestamp()
            )

      fun nameAlreadyUsed(command: AddCard, cardsPool: CardsPool): Boolean =
          cardsPool.cards.any{kv -> kv.value.label == command.title}

      fun unauthorized(command: AddCard, cardsPool: CardsPool): Boolean = cardsPool.createdBy != command.userId
    }

    override suspend fun validate(command: AddCard): Card {
        var errors = emptyList<AddCardError>()
        val cardsPool: CardsPool? = cardsPoolRepository.find(command.cardsPoolId)

        // will be refactored using a `Either` data class
        if (cardsPool == null) {
            errors = errors.plus(CardsPoolNotFound(command.cardsPoolId))
        } else {
          if (unauthorized(command, cardsPool)) {
              errors = errors.plus(Unauthorized)
          } else {
            if (nameAlreadyUsed(command, cardsPool)) {
              errors = errors.plus(LabelExists(command.title))
            }
          }
        }

        if (errors.isNotEmpty()) {
            throw Exception(errors.map{it.toString()}.joinToString(", "))
        }

        return createCard(command, clock, genereateId(command.title, cardsPool!!))
    }

}