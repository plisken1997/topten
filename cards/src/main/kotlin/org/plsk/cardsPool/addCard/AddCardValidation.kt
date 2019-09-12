package org.plsk.cardsPool.addCard

import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.CardsPool
import org.plsk.core.clock.Clock
import org.plsk.core.validation.Validation
import java.util.*

class AddCardValidation(val cardsPoolRepository: CardsPoolRepository, val clock: Clock) : Validation<AddCard, CardsPool> {

    companion object {
        // @todo find a better way to generate (real) deterministic id :-)
        fun genereateId(label: String, cardsPool: CardsPool): UUID = UUID.nameUUIDFromBytes((label + cardsPool.id.toString()).toByteArray())

        fun createCard(command: AddCard, cardsPool: CardsPool, clock: Clock): Card =
            Card(
                genereateId(command.title, cardsPool),
                command.title,
                clock.now().timestamp()
            )
    }


    override fun validate(command: AddCard): CardsPool {
        var errors = emptyList<AddCardError>()
        val cardsPool = cardsPoolRepository.find(command.cardsPoolId)

        // will be refactored using a `Either` data class
        if (cardsPool == null) {
            errors = errors.plus(CardsPoolNotFound(command.cardsPoolId))
        } else if (cardsPool.cards.any{c -> c.label == command.title}) {
            errors = errors.plus(LabelExists(command.title))
        }

        if (errors.isNotEmpty()) {
            throw Exception(errors.map{it.toString()}.joinToString(", "))
        }

        return cardsPool!!.addCard(
            createCard(command, cardsPool, clock),
            command.position
        )
    }

}