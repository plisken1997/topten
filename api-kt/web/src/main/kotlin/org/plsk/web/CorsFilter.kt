package org.plsk.web

import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CorsFilter: WebFilter {

  // @todo inject this value !
  private val host = "http://localhost:3000"

  override fun filter(ctx: ServerWebExchange?, chain: WebFilterChain?): Mono<Void> {
    if (ctx != null) {
      ctx.response.headers.add("Access-Control-Allow-Origin", host)
      ctx.response.headers.add("Access-Control-Allow-Methods", "GET, PUT, POST, PATCH, DELETE, OPTIONS")
      ctx.response.headers.add("Access-Control-Allow-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,Authorization")

      if (ctx.request.method == HttpMethod.OPTIONS) {
        ctx.response.headers.add("Access-Control-Max-Age", "1728000")
        ctx.response.statusCode = HttpStatus.NO_CONTENT
        return Mono.empty()
      }

      ctx.response.headers.add("Access-Control-Expose-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range")
      return chain?.filter(ctx) ?: Mono.empty()
    }

    return Mono.empty()
  }
}
