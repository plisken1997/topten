package org.plsk.core.async

import kotlinx.coroutines.*

@Deprecated("services method should be async by default")
interface RunAsync {
  suspend fun <T>runAsync(io: () -> T): T = coroutineScope { io() }
}
