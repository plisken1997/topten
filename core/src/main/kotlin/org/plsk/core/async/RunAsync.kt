package org.plsk.core.async

import kotlinx.coroutines.*

interface RunAsync {
  suspend fun <T>runAsync(io: () -> T): T = coroutineScope { io() }
}
