package org.plsk.web.cards

import org.plsk.cardsPool.addCard.AddCard
import org.plsk.cardsPool.create.CreateCardsPool
import org.plsk.cardsPool.promoteCard.PromoteCard
import org.plsk.cardsPool.promoteCard.PromoteType
import org.plsk.cardsPool.promoteCard.UnpromoteCard
import org.plsk.cardsPool.promoteCard.UpdateCardPosition
import org.plsk.cardsPool.removeCard.RemoveCard
import org.plsk.core.command.CommandHandler
import org.plsk.user.UnknownUser
import org.plsk.user.User
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.InetAddress
import java.util.*

data class CreateCardsPoolPayload(val name: String, val description: String?, val slots: Int?) {
  fun toCommand(user: User): CreateCardsPool = CreateCardsPool(name, description, slots, user)

  override fun toString() = """{"name": "$name", "description": "$description"}"""
}

data class AddCardPayload(val title: String, val description: String?, val position: Int) {
  fun toCommand(cardsPoolId: UUID): AddCard = AddCard(title, description, position, cardsPoolId)
}

data class PromoteCardPayload(val cardId: UUID, val position: Int) {
  fun toCommand(cardsPoolId: UUID): PromoteCard = PromoteCard(cardId, position, cardsPoolId)
}

data class UpdateCardPositionPayload(val cardId: UUID, val position: Int) {
  fun toCommand(cardsPoolId: UUID): UpdateCardPosition = UpdateCardPosition(cardId, position, cardsPoolId)
}

data class RemoveCardPayload(val cardId: String) {
  fun toCommand(cardsPoolId: UUID): RemoveCard = RemoveCard(UUID.fromString(cardId), cardsPoolId)
}

data class UnPromoteCardPayload(val cardId: String) {
  fun toCommand(cardsPoolId: UUID): UnpromoteCard = UnpromoteCard(UUID.fromString(cardId), cardsPoolId)
}

data class CreateResourceResult(val id: UUID)

data class TopCardsResult(val toCards: Set<UUID>)

@Component
class CardsPoolHandler(
    private val createPoolHandler: CommandHandler<CreateCardsPool, UUID>,
    private val addCardHandler: CommandHandler<AddCard, UUID>,
    private val promoteCardHander: CommandHandler<PromoteType, Set<UUID>>,
    private val removeCard: CommandHandler<RemoveCard, Unit>
) {

  fun createCardPool(request: ServerRequest): Mono<ServerResponse> =
    request.bodyToMono(CreateCardsPoolPayload::class.java)
      .flatMap { payload ->
        val result = request.remoteAddress().map { addr ->
          val created =
            createPoolHandler.handle(
                payload.toCommand(UnknownUser(extractRemoteAddress(addr.address))
              )
            )
          CreateResourceResult(created)
        }

        ServerResponse.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromObject(result))
      }

  fun addCard(request: ServerRequest): Mono<ServerResponse> =
      request.bodyToMono(AddCardPayload::class.java)
          .flatMap { payload ->
            val cardpoolId = request.pathVariable("cardpoolId")
            val created =
                  addCardHandler.handle(
                    payload.toCommand(UUID.fromString(cardpoolId))
                  )

            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(CreateResourceResult(created)))
          }

  fun promoteCard(request: ServerRequest): Mono<ServerResponse> =
      request.bodyToMono(PromoteCardPayload::class.java)
          .flatMap { payload ->
            val cardpoolId = request.pathVariable("cardpoolId")
            val promoted =
                promoteCardHander.handle(
                    payload.toCommand(UUID.fromString(cardpoolId))
                )

            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(TopCardsResult(promoted)))
          }

  fun deleteCard(request: ServerRequest): Mono<ServerResponse> {
      val cardpoolId = request.pathVariable("cardpoolId")
      val cardId = request.pathVariable("cardId")
      val payload = RemoveCardPayload(cardId)

      val removed = removeCard.handle(payload.toCommand(UUID.fromString(cardpoolId)))

      return ServerResponse.noContent()
          .build()
    }

  fun unPromoteCard(request: ServerRequest): Mono<ServerResponse> =
      request.bodyToMono(UnPromoteCardPayload::class.java)
          .flatMap { payload ->
            val cardpoolId = request.pathVariable("cardpoolId")
            val promoted =
                promoteCardHander.handle(
                    payload.toCommand(UUID.fromString(cardpoolId))
                )

            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(TopCardsResult(promoted)))
          }

  fun updateTop(request: ServerRequest): Mono<ServerResponse> =
      request.bodyToMono(UpdateCardPositionPayload::class.java)
          .flatMap { payload ->
            val cardpoolId = request.pathVariable("cardpoolId")

            val updated = promoteCardHander.handle(payload.toCommand(UUID.fromString(cardpoolId)))

            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(TopCardsResult(updated)))
          }

  private fun extractRemoteAddress(addr: InetAddress): String = addr.hostAddress
}