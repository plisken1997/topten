package org.plsk.core.dao

import arrow.fx.IO

interface DataWriter<T, WriteResult> {
  fun store(data: T): WriteResult
  fun update(data: T): WriteResult
  fun updateAsync(data: T): IO<WriteResult> = TODO()
}