package org.plsk.core.dao

@Deprecated("filtering must be handled in the adapter service")
sealed class QueryFilter() {
  abstract val name: String
}

@Deprecated("filtering must be handled in the adapter service")
data class EqFilter<T>(override val name: String, val value: T): QueryFilter()

@Deprecated("filtering must be handled in the adapter service")
data class InFilter<T>(override val name: String, val value: Set<T>): QueryFilter()
