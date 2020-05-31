package org.plsk.cardsPool

interface CardsPollRepositoryFactory {

  fun getCardsPoolRepository(baseCardsPool: CardsPool): CardsPoolRepository =
      object : CardsPoolRepository {
        override suspend fun update(data: CardsPool): WriteResult = TODO("not implemented")
        override suspend fun add(data: CardsPool): WriteResult = TODO("not implemented")
        override suspend fun findByUser(query: GetCardsPoolByUser): List<CardsPool> = TODO("Not yet implemented")
        override suspend fun find(query: GetCardsQuery): CardsPool? =
            if (query.cardsPoolId == baseCardsPool.id) baseCardsPool else null
      }
}
