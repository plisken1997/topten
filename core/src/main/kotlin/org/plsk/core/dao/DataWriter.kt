package org.plsk.core.dao

interface DataWriter<T, WriteResult> {
  fun store(data: T): WriteResult
  fun update(data: T): WriteResult
}