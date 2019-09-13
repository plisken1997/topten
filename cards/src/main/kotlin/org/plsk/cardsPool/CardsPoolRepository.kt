package org.plsk.cardsPool

import org.plsk.core.dao.DataReader
import org.plsk.core.dao.DataWriter
import java.util.*

sealed class WriteResult

data class WriteSuccess(val id: UUID): WriteResult()
data class WriteFailure(val error: Throwable): WriteResult()

interface CardsPoolRepository: DataReader<CardsPool, UUID>, DataWriter<CardsPool, WriteResult>