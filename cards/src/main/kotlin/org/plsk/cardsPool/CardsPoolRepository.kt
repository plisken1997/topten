package org.plsk.cardsPool

import java.util.*

interface CardsPoolRepository {

    fun find(id: UUID): CardsPool?

}