package org.plsk.web.cards

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*

@Configuration
class CardsPoolRouter {

  @Bean
  fun createCardsPool(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> {
    return RouterFunctions
        .route(
            RequestPredicates.POST("/cardspool")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            HandlerFunction<ServerResponse> {
              cardsPoolHandler.createCardPool(it)
            }
        )
  }

  @Bean
  fun addCard(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> {
    return RouterFunctions
        .route(
            RequestPredicates.POST("/cardspool/{cardpoolId:[\\w-]+}/addCard")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            HandlerFunction<ServerResponse> {
              cardsPoolHandler.addCard(it)
            }
        )
  }

  @Bean
  fun prmoteCard(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> {
    return RouterFunctions
        .route(
            RequestPredicates.POST("/cardspool/{cardpoolId:[\\w-]+}/promote")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            HandlerFunction<ServerResponse> {
              cardsPoolHandler.promoteCard(it)
            }
        )
  }

}