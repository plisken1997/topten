package org.plsk.web.cards

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*

@Configuration
class CardsPoolRouter {

  @Bean
  fun createCardsPool(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
    RouterFunctions
        .route(
            RequestPredicates.POST("/cardspool")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            HandlerFunction<ServerResponse> {
              cardsPoolHandler.createCardPool(it)
            }
        )

  @Bean
  fun addCard(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
    RouterFunctions
        .route(
            RequestPredicates.POST("/cardspool/{cardpoolId:[\\w-]+}/addCard")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            HandlerFunction<ServerResponse> {
              cardsPoolHandler.addCard(it)
            }
        )

  @Bean
  fun promoteCard(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
    RouterFunctions
        .route(
            RequestPredicates.POST("/cardspool/{cardpoolId:[\\w-]+}/promote")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            HandlerFunction<ServerResponse> {
              cardsPoolHandler.promoteCard(it)
            }
        )

  @Bean
  fun removeCard(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
      RouterFunctions
          .route(
              RequestPredicates.DELETE("/cardspool/{cardpoolId:[\\w-]+}/delete")
                  .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
              HandlerFunction<ServerResponse> {
                cardsPoolHandler.deleteCard(it)
              }
          )

  @Bean
  fun unPromoteCard(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
      RouterFunctions
          .route(
              RequestPredicates.POST("/cardspool/{cardpoolId:[\\w-]+}/unPromote")
                  .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
              HandlerFunction<ServerResponse> {
                cardsPoolHandler.unPromoteCard(it)
              }
          )

  @Bean
  fun updateTop(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
      RouterFunctions
          .route(
              RequestPredicates.POST("/cardspool/{cardpoolId:[\\w-]+}/updateTop")
                  .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
              HandlerFunction<ServerResponse> {
                cardsPoolHandler.updateTop(it)
              }
          )
}