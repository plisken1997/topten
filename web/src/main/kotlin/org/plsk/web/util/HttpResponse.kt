package org.plsk.web.util

import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse

interface HttpResponse {
  fun <T>jsonResponse(result: T) = ServerResponse.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromObject(result))
}