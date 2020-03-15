package org.plsk.web.authentication

import arrow.core.getOrElse
import org.plsk.security.Authentication
import org.plsk.security.AuthenticationFailure
import org.plsk.security.CreateGuestUserSession
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import kotlinx.coroutines.reactor.mono
import java.net.InetAddress

@Component
class AuthenticationHandler(private val authentication: Authentication<AuthenticationFailure>) {

  fun initGuestSession(request: ServerRequest): Mono<ServerResponse> =
      request.remoteAddress().map { addr ->
        val authUser = CreateGuestUserSession(extractRemoteAddress(addr.address))
        mono {
          val session = authentication.authenticate(authUser)
          session.getOrElse { throw Exception("could not create session") }
        }
      }.orElseThrow { throw Exception("could not read host") }
          .flatMap {
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(it.accessToken))
          }

  private fun extractRemoteAddress(addr: InetAddress): String = addr.hostAddress

}
