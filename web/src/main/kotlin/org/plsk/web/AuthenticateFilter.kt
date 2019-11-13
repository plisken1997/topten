package org.plsk.web

import org.plsk.security.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class AuthenticateFilter(private val authentication: Authentication<AuthenticationFailure>): WebFilter {

  private val logger = LoggerFactory.getLogger(javaClass.name)

  override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
    val request: ServerHttpRequest = exchange.request

    if (isWrite(request)) {
      val authUser: AuthenticationRequest =
          if (request.headers.containsKey("Authorization")) {
            fromAccessToken(request.headers)
          }
          else UnknownUserRequest(request.getRemoteAddress()?.toString() ?: "")

      authentication.authenticate(authUser)
          .bimap(
              this::handleError,
              { session -> handleSession(session, request)}
          )
    }

    return chain.filter(exchange)
  }

  private fun isWrite(request: ServerHttpRequest): Boolean =
      request.method?.matches("POST") ?: false || request.method?.matches("PUT") ?: false || request.method?.matches("PATH") ?: false

  private fun handleError(error: AuthenticationFailure){
    logger.error(error.error)
  }

  private fun handleSession(session: Session, request: ServerHttpRequest){
    logger.info("access token [${session.accessToken.token}]")
  }

  private fun fromAccessToken(headers: HttpHeaders): AuthenticationRequest {
    val accessToken = headers.getFirst("Authorization")?.replaceFirst("Bearer", "")?.trim()
    if(accessToken == null) {
      throw Exception("could not extract access token")
    }
    return AccessTokenRequest(token = accessToken)
  }
}