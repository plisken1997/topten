package org.plsk.cardsPool

import org.plsk.core.dao.DataReader
import org.plsk.core.dao.DataWriter
import org.plsk.core.dao.EqFilter
import java.util.*

sealed class WriteResult

data class WriteSuccess(val id: UUID): WriteResult()

interface CardsPoolRepository: DataReader<CardsPool, UUID>, DataWriter<CardsPool, WriteResult> {
  suspend fun findByUser(userId: String) = findAll(listOf(EqFilter("createdBy", userId)))
}