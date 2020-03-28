package org.plsk.web.cards

import org.plsk.cardsPool.getCards.GetCardsQuery
import org.plsk.cardsPool.getCards.GetCardsQueryHandler
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*
import org.plsk.cardsPool.addCard.AddCardAction
import org.plsk.cardsPool.create.CreateCardsPoolAction
import org.plsk.cardsPool.promoteCard.*
import org.plsk.cardsPool.removeCard.RemoveCardAction
import kotlinx.coroutines.reactor.mono
import org.plsk.cardsPool.getCards.GetCardsPoolsQuery
import org.plsk.web.authentication.WithAuthentication
import org.plsk.web.util.HttpResponse

@Component
class CardsPoolHandler(
    private val createPoolHandler: CreateCardsPoolAction,
    private val addCardHandler: AddCardAction,
    private val promoteCardHander: PromoteCardAction,
    private val removeCard: RemoveCardAction,
    private val getCardsHandler: GetCardsQueryHandler
) : WithAuthentication, HttpResponse {

  fun createCardPool(request: ServerRequest): Mono<ServerResponse> = withSession(request) { session ->
    request.bodyToMono(CreateCardsPoolPayload::class.java)
        .flatMap { payload ->
          mono {
            createPoolHandler.handle(payload.toCommand(session.user))
          }
        }
        .flatMap { jsonResponse(CreateResourceResult(it)) }
  }

  fun addCard(request: ServerRequest): Mono<ServerResponse> = withSession(request) { session ->
    request.bodyToMono(AddCardPayload::class.java)
        .flatMap { payload ->
          val cardpoolId = request.pathVariable("cardpoolId")
          mono {
            addCardHandler.handle(
                payload.toCommand(UUID.fromString(cardpoolId), session.user.id)
            )
          }
        }.flatMap { jsonResponse(CreateResourceResult(it)) }
  }

  fun promoteCard(request: ServerRequest): Mono<ServerResponse> = withSession(request) { session ->
    request.bodyToMono(PromoteCardPayload::class.java)
        .flatMap { payload ->
          val cardpoolId = request.pathVariable("cardpoolId")
          mono {
            promoteCardHander.handle(
                payload.toCommand(UUID.fromString(cardpoolId), session.user.id)
            )
          }
        }.flatMap { jsonResponse(TopCardsResult(it)) }
  }

  fun deleteCard(request: ServerRequest): Mono<ServerResponse> = withSession(request) { session ->
    val cardpoolId = request.pathVariable("cardpoolId")
    val cardId = request.pathVariable("cardId")
    val payload = RemoveCardPayload(cardId)
    mono {
      removeCard.handle(payload.toCommand(UUID.fromString(cardpoolId), session.user.id))
    }.flatMap {
      ServerResponse.noContent().build()
    }
  }

  fun unPromoteCard(request: ServerRequest): Mono<ServerResponse> = withSession(request) { session ->
    request.bodyToMono(UnPromoteCardPayload::class.java)
        .flatMap { payload ->
          val cardpoolId = request.pathVariable("cardpoolId")
          mono {
              promoteCardHander.handle(
                payload.toCommand(UUID.fromString(cardpoolId), session.user.id)
            )
          }
        }.flatMap { jsonResponse(TopCardsResult(it)) }
  }

  fun updateTop(request: ServerRequest): Mono<ServerResponse> = withSession(request) { session ->
    request.bodyToMono(UpdateCardPositionPayload::class.java)
        .flatMap { payload ->
          val cardpoolId = request.pathVariable("cardpoolId")
          mono {
            promoteCardHander.handle(payload.toCommand(UUID.fromString(cardpoolId), session.user.id))
          }
        }.flatMap { jsonResponse(TopCardsResult(it)) }
  }

  fun getCards(request: ServerRequest): Mono<ServerResponse> = withSession(request) { session ->
    mono {
      val cardpoolId = request.pathVariable("cardpoolId")
      getCardsHandler.handle(GetCardsQuery(UUID.fromString(cardpoolId), session.user.id))
    }.flatMap { jsonResponse(GetCards.from(it.content.first())) }
  }

  fun getCardPools(request: ServerRequest): Mono<ServerResponse> = withSession(request) { session ->
    mono {
      getCardsHandler.handle(GetCardsPoolsQuery(session.user.id))
    }.flatMap { jsonResponse(it.content.map { GetCardsPool.from(it) }) }
  }
}