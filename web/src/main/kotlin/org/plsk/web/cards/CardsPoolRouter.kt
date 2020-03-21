package org.plsk.web.cards

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*

@Configuration
class CardsPoolRouter {

  @Bean
  fun getCardsPools(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
      RouterFunctions
          .route(
              RequestPredicates.GET("/cardspool"),
              HandlerFunction<ServerResponse>(cardsPoolHandler::getCardPools)
          )

  @Bean
  fun createCardsPool(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
    RouterFunctions
        .route(
            RequestPredicates.POST("/cardspool")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            HandlerFunction<ServerResponse>(cardsPoolHandler::createCardPool)
        )

  @Bean
  fun addCard(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
    RouterFunctions
        .route(
            RequestPredicates.POST("/cardspool/{cardpoolId:[\\w-]+}/addCard")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            HandlerFunction<ServerResponse>(cardsPoolHandler::addCard)
        )

  @Bean
  fun promoteCard(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
    RouterFunctions
        .route(
            RequestPredicates.POST("/cardspool/{cardpoolId:[\\w-]+}/promote")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            HandlerFunction<ServerResponse>(cardsPoolHandler::promoteCard)
        )

  @Bean
  fun removeCard(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
      RouterFunctions
          .route(
              RequestPredicates.DELETE("/cardspool/{cardpoolId:[\\w-]+}/delete/{cardId:[\\w-]+}"),
              HandlerFunction<ServerResponse>(cardsPoolHandler::deleteCard)
          )

  @Bean
  fun unPromoteCard(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
      RouterFunctions
          .route(
              RequestPredicates.POST("/cardspool/{cardpoolId:[\\w-]+}/unPromote")
                  .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
              HandlerFunction<ServerResponse>(cardsPoolHandler::unPromoteCard)
          )

  @Bean
  fun updateTop(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
      RouterFunctions
          .route(
              RequestPredicates.POST("/cardspool/{cardpoolId:[\\w-]+}/updateTop")
                  .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
              HandlerFunction<ServerResponse>(cardsPoolHandler::updateTop)
          )

  @Bean
  fun getCards(cardsPoolHandler: CardsPoolHandler): RouterFunction<ServerResponse> =
      RouterFunctions
          .route(
              RequestPredicates.GET("/cardspool/{cardpoolId:[\\w-]+}"),
              HandlerFunction<ServerResponse>(cardsPoolHandler::getCards)
          )
}