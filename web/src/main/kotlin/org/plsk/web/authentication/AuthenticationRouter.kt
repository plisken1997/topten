package org.plsk.web.authentication

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class AuthenticationRouter {

  @Bean
  fun initSession(authenticationHandler: AuthenticationHandler): RouterFunction<ServerResponse> =
      RouterFunctions
          .route(
              RequestPredicates.GET("/authentication/init-session"),
              HandlerFunction<ServerResponse>(authenticationHandler::initGuestSession)
          )
}