package org.plsk.web

import arrow.core.Either
import arrow.core.Left
import org.plsk.security.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import kotlinx.coroutines.reactor.mono
import org.springframework.http.server.RequestPath

@Component
class AuthenticateFilter(private val authentication: Authentication<AuthenticationFailure>): WebFilter {

  private val logger = LoggerFactory.getLogger(javaClass.name)

  override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
    val request: ServerHttpRequest = exchange.request

    if (shouldAuthenticate(request)) {
      return mono {
        authentitcate(request)
      }.flatMap {
        it.bimap(
          this::handleError,
          { initSession(it, exchange)}
        )
        chain.filter(exchange)
      }
    }

    return chain.filter(exchange)
  }

  private suspend fun authentitcate(request: ServerHttpRequest): Either<AuthenticationFailure, Session> =
    if (request.headers.containsKey("Authorization")) {
      authentication.authenticate(fromAccessToken(request.headers))
    }
    else Left(AccessTokenNotFound)

  private fun shouldAuthenticate(request: ServerHttpRequest): Boolean = request.path.toString().contains("cardspool")

  private fun handleError(error: AuthenticationFailure) {
    logger.error("authenticate error : ${error.error}")
    throw Exception(error.error)
  }

  private fun initSession(session: Session, exchange: ServerWebExchange){
    exchange.attributes.put("session", session)
    logger.info("access token [${session.accessToken.token}]")
    exchange.response.headers.set("x-authorisation", session.accessToken.token)
  }

  private fun fromAccessToken(headers: HttpHeaders): AccessTokenRequest {
    val accessToken = headers.getFirst("Authorization")?.replaceFirst("Bearer", "")?.trim()
    if(accessToken == null) {
      throw Exception("could not extract access token")
    }
    return AccessTokenRequest(token = accessToken)
  }
}
