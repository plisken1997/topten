package org.plsk.cardsPool.create

import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.DisplayType
import org.plsk.core.clock.Clock
import org.plsk.core.id.IdGen
import org.plsk.core.validation.Validation
import java.util.*

class CardsPoolValidation(val idGen: IdGen<UUID>, val clock: Clock) : Validation<CreateCardsPool, CardsPool> {

    companion object {
        fun idParts(command: CreateCardsPool): String = command.name + command.description.orEmpty() + command.user.id

        fun createdCardsPool(id: UUID, command: CreateCardsPool, clock: Clock): CardsPool =
            CardsPool(
                id = id,
                name = command.name,
                description = command.description,
                slots = command.slots,
                display = command.display?.let{ DisplayType.valueOf(it.toUpperCase()) },
                createdAt = clock.now().timestamp(),
                createdBy = command.user.id
            )
    }

    override suspend fun validate(command: CreateCardsPool): CardsPool {
        // @todo validate data...
        return createdCardsPool(
                idGen.fromString(idParts(command)),
                command,
                clock
        )
    }

}