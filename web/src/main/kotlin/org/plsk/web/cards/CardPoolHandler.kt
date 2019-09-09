package org.plsk.web.cards

import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.create.CardsPoolCreated
import org.plsk.cardsPool.create.CardsPoolValidation
import org.plsk.cardsPool.create.CreateCardsPool
import org.plsk.cardsPool.create.CreateCardsPoolHandler
import org.plsk.core.clock.UTCDatetimeClock
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.id.UUIDGen
import org.plsk.core.validation.Validation
import org.plsk.user.UnknownUser
import org.plsk.user.User
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

data class CreateCardsPoolPayload(val name: String, val description: String?) {
    fun toCommand(user: User): CreateCardsPool = CreateCardsPool(name, description, user)
}

@Component
class CardPoolHandler {

    private val logger = LoggerFactory.getLogger("test")

    private val cardsPoolValidator: Validation<CreateCardsPool, CardsPool> =
            CardsPoolValidation(
                    UUIDGen(),
                    UTCDatetimeClock
            )

    private val eventBus: EventBus = object : EventBus {
        override fun publish(event: Event) = when (event) {
            is CardsPoolCreated -> logger.info("store $event")
            else -> Unit
        }
    }

    private val createPoolHandler: CreateCardsPoolHandler = CreateCardsPoolHandler(cardsPoolValidator, eventBus)

    fun createCardPool(request: ServerRequest): Mono<ServerResponse> {
        return request.remoteAddress().map { addr ->
            val created = createPoolHandler.handle(CreateCardsPoolPayload("title", "description...").toCommand(UnknownUser(addr.address.hostAddress)))
            logger.info(created.toString())

            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(
                    """{
                    |"id": "${created}"
                    |}""".trimMargin())
                )
        }.get()
    }
}