package org.plsk.web.cards

import org.plsk.cardsPool.addCard.AddCard
import org.plsk.cardsPool.create.CreateCardsPool
import org.plsk.cardsPool.getCards.CardsPoolContent
import org.plsk.cardsPool.promoteCard.PromoteCard
import org.plsk.cardsPool.promoteCard.UnpromoteCard
import org.plsk.cardsPool.promoteCard.UpdateCardPosition
import org.plsk.cardsPool.removeCard.RemoveCard
import org.plsk.user.User
import java.util.*

/****** payload ******/

data class CreateCardsPoolPayload(val name: String, val description: String?, val slots: Int?, val display: String?) {
  fun toCommand(user: User): CreateCardsPool = CreateCardsPool(name, description, slots, display, user)

  override fun toString() = """{"name": "$name", "description": "$description"}"""
}

data class AddCardPayload(val title: String, val description: String?, val position: Int) {
  fun toCommand(cardsPoolId: UUID, userId: String): AddCard = AddCard(title, description, position, cardsPoolId, userId)
}

data class PromoteCardPayload(val cardId: UUID, val position: Int) {
  fun toCommand(cardsPoolId: UUID, userId: String): PromoteCard = PromoteCard(cardId, position, cardsPoolId, userId)
}

data class UpdateCardPositionPayload(val cardId: UUID, val position: Int) {
  fun toCommand(cardsPoolId: UUID, userId: String): UpdateCardPosition = UpdateCardPosition(cardId, position, cardsPoolId, userId)
}

data class RemoveCardPayload(val cardId: String) {
  fun toCommand(cardsPoolId: UUID, userId: String): RemoveCard = RemoveCard(UUID.fromString(cardId), cardsPoolId, userId)
}

data class UnPromoteCardPayload(val cardId: String) {
  fun toCommand(cardsPoolId: UUID, userId: String): UnpromoteCard = UnpromoteCard(UUID.fromString(cardId), cardsPoolId, userId)
}

data class UpdateCardPayload(val cardId: String, val field: String, val value: String)

/******* results *******/

data class CreateResourceResult(val id: UUID)

data class TopCardsResult(val toCards: Set<UUID>)

data class CardResult(val id: UUID, val title: String, val description: String?)

data class GetCards(val highlighted: Set<CardResult>, val cardsPool: Set<CardResult>) {
  companion object {
    fun from(cardsPoolContent: CardsPoolContent): GetCards =
        GetCards(
            cardsPoolContent.getOrderedHighlighted().map { c -> CardResult(c.id, c.label, c.description) }.toSet(),
            cardsPoolContent.cardsPool.map { c -> CardResult(c.id, c.label, c.description) }.toSet()
        )
  }
}

data class GetCardsPool(val id: UUID, val name: String, val description: String?, val slots: Int?, val cards: GetCards){
  companion object{
    fun from(cardsPoolContent: CardsPoolContent): GetCardsPool =
        GetCardsPool(
            cardsPoolContent.id,
            cardsPoolContent.name,
            cardsPoolContent.description,
            cardsPoolContent.slots,
            GetCards.from(cardsPoolContent)
        )
  }
}

data class UpdateCardsResult(val id: UUID)
