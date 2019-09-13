package org.plsk.core.dao

interface DataReader<T, ID> {
  fun find(id: ID): T?
}