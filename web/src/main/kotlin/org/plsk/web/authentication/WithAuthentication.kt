package org.plsk.web.authentication

import org.plsk.security.Session
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

interface WithAuthentication {

  fun withSession(request: ServerRequest, fn: (Session) -> Mono<ServerResponse>): Mono<ServerResponse> {
    val session = request.exchange().getAttribute<Session>("session") ?: throw Exception("unauthorized")
    return fn(session)
  }
}