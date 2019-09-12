package org.plsk.web.cards

import org.plsk.cardsPool.create.CreateCardsPool
import org.plsk.cardsPool.create.CreateCardsPoolHandler
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

data class CreateCardsPoolPayload(val name: String, val description: String?) {
  fun toCommand(user: User): CreateCardsPool = CreateCardsPool(name, description, user)

  override fun toString() = """{"name": "$name", "description": "$description"}"""
}

data class CreateCardsPoolResult(val id: UUID)

@Component
class CardsPoolHandler(private val createPoolHandler: CreateCardsPoolHandler) {

  fun createCardPool(request: ServerRequest): Mono<ServerResponse> =
    request.bodyToMono(CreateCardsPoolPayload::class.java)
      .flatMap { payload ->
        val result = request.remoteAddress().map { addr ->
          val created =
            createPoolHandler.handle(
              CreateCardsPoolPayload(
                  payload.name,
                  payload.description
              ).toCommand(UnknownUser(extractRemoteAddress(addr.address)))
            )
          CreateCardsPoolResult(created)
        }

        ServerResponse.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromObject(result))
      }

  private fun extractRemoteAddress(addr: InetAddress): String = addr.hostAddress
}