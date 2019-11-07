package org.plsk.core.dao

sealed class QueryFilter() {
  abstract val name: String
}

data class EqFilter<T>(override val name: String, val value: T): QueryFilter()
data class InFilter<T>(override val name: String, val value: Set<T>): QueryFilter()
