package org.plsk.cardsPool

interface ValidateUser {
  fun unauthorized(userId: String, cardsPool: CardsPool): Boolean = cardsPool.createdBy != userId
}