package org.plsk.core.dao

@Deprecated("use repositories instead")
interface DataWriter<T, WriteResult> {
  suspend fun store(data: T): WriteResult
  suspend fun update(data: T): WriteResult
}