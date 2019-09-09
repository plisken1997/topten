package org.plsk.web.cards

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*

@Configuration
class CardsPoolRouter {

    @Bean
    fun route(cardPoolHandler: CardPoolHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
                .route(
                        RequestPredicates.POST("/cardspool")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        HandlerFunction<ServerResponse> {
                            cardPoolHandler.createCardPool(it)
                        }
                )
    }

}