package org.plsk.cardsPool.create

import org.plsk.cardsPool.CardsPool
import org.plsk.core.clock.Clock
import org.plsk.core.id.IdGen
import org.plsk.core.validation.Validation
import java.util.*

class CardsPoolValidation(val idGen: IdGen<UUID>, val clock: Clock) : Validation<CreateCardsPool, CardsPool> {

    companion object {
        fun idParts(command: CreateCardsPool): String = command.name + command.description.orEmpty() + command.user.toString()

        fun createdCardsPool(id: UUID, command: CreateCardsPool, clock: Clock): CardsPool =
            CardsPool(
                id,
                command.name,
                command.description,
                createdAt = clock.now().timestamp(),
                createdBy = command.user
            )
    }

    override fun validate(command: CreateCardsPool): CardsPool {
        // @todo validate data...
        return createdCardsPool(
                idGen.fromString(idParts(command)),
                command,
                clock
        )
    }

}