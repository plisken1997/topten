package org.plsk.web.cards

import org.plsk.cardsPool.getCards.GetCardsQuery
import org.plsk.cardsPool.getCards.GetCardsQueryHandler
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.InetAddress
import java.util.*
import org.plsk.cardsPool.addCard.AddCardAction
import org.plsk.cardsPool.create.CreateCardsPoolAction
import org.plsk.cardsPool.promoteCard.*
import org.plsk.cardsPool.removeCard.RemoveCardAction
import kotlinx.coroutines.reactor.mono
import org.plsk.cardsPool.getCards.GetCardsPoolsQuery
import org.plsk.security.Session

@Component
class CardsPoolHandler(
    private val createPoolHandler: CreateCardsPoolAction,
    private val addCardHandler: AddCardAction,
    private val promoteCardHander: PromoteCardAction,
    private val removeCard: RemoveCardAction,
    private val getCardsHandler: GetCardsQueryHandler
) {

  fun createCardPool(request: ServerRequest): Mono<ServerResponse> =
      request.bodyToMono(CreateCardsPoolPayload::class.java)
          .flatMap { payload ->
            val session = request.exchange().getAttribute<Session>("session")
            mono {
              createPoolHandler.handle(payload.toCommand(session!!.user))
            }.map { CreateResourceResult(it) }
          }
          .flatMap {
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(it))
          }

  fun addCard(request: ServerRequest): Mono<ServerResponse> =
      request.bodyToMono(AddCardPayload::class.java)
          .flatMap { payload ->
            val session = request.exchange().getAttribute<Session>("session")
            val cardpoolId = request.pathVariable("cardpoolId")
            mono {
              addCardHandler.handle(
                  payload.toCommand(UUID.fromString(cardpoolId), session!!.user.id)
              )
            }
          }.flatMap {
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(CreateResourceResult(it)))
          }

  fun promoteCard(request: ServerRequest): Mono<ServerResponse> =
      request.bodyToMono(PromoteCardPayload::class.java)
          .flatMap { payload ->
            val cardpoolId = request.pathVariable("cardpoolId")
            mono {
              promoteCardHander.handle(
                  payload.toCommand(UUID.fromString(cardpoolId))
              )
            }
          }.flatMap {
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(TopCardsResult(it)))
          }

  fun deleteCard(request: ServerRequest): Mono<ServerResponse> {
    val cardpoolId = request.pathVariable("cardpoolId")
    val cardId = request.pathVariable("cardId")
    val payload = RemoveCardPayload(cardId)
    return mono {
      removeCard.handle(payload.toCommand(UUID.fromString(cardpoolId)))
    }.flatMap {
      ServerResponse.noContent().build()
    }
  }

  fun unPromoteCard(request: ServerRequest): Mono<ServerResponse> =
      request.bodyToMono(UnPromoteCardPayload::class.java)
          .flatMap { payload ->
            val cardpoolId = request.pathVariable("cardpoolId")
            mono {
              promoteCardHander.handle(
                  payload.toCommand(UUID.fromString(cardpoolId))
              )
            }
          }.flatMap {
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(TopCardsResult(it)))
          }

  fun updateTop(request: ServerRequest): Mono<ServerResponse> =
      request.bodyToMono(UpdateCardPositionPayload::class.java)
          .flatMap { payload ->
            val cardpoolId = request.pathVariable("cardpoolId")
            mono {
              promoteCardHander.handle(payload.toCommand(UUID.fromString(cardpoolId)))
            }
          }.flatMap {
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(TopCardsResult(it)))
          }

  fun getCards(request: ServerRequest): Mono<ServerResponse> =
      mono {
        val cardpoolId = request.pathVariable("cardpoolId")
        getCardsHandler.handle(GetCardsQuery(UUID.fromString(cardpoolId)))
      }.flatMap {
        ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromObject(GetCards.from(it.content.first())))
      }

  fun getCardPools(request: ServerRequest): Mono<ServerResponse> {
    val session = request.exchange().getAttribute<Session>("session")
    return mono {
      getCardsHandler.handle(GetCardsPoolsQuery(session!!.user.id))
    }.flatMap {
      ServerResponse.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromObject(it.content.map { GetCardsPool.from(it) }))
    }
  }
}