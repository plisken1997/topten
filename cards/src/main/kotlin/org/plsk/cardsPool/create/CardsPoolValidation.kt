package org.plsk.cardsPool.create

import org.plsk.core.clock.Clock
import org.plsk.core.validation.Validation
import java.util.*

class CardsPoolValidation(val clock: Clock) : Validation<CreateCardsPool, CardsPool> {

    companion object {
        // @todo find a better way to generate deterministic id :-)
        fun genereateId(name: String, description: String?): UUID = UUID.nameUUIDFromBytes((name + description.orEmpty()).toByteArray())
    }

    override fun validate(command: CreateCardsPool): CardsPool {
        // @todo validate data...
        return CardsPool(genereateId(command.name, command.description), command.name, command.description, createdAt = clock.now().timestamp())
    }

}