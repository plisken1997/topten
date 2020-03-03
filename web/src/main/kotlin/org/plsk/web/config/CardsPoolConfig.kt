package org.plsk.web.config

import com.mongodb.reactivestreams.client.MongoDatabase
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.addCard.AddCard
import org.plsk.cardsPool.addCard.AddCardAction
import org.plsk.cardsPool.addCard.AddCardValidation
import org.plsk.cardsPool.create.CardsPoolValidation
import org.plsk.cardsPool.create.CreateCardsPool
import org.plsk.cardsPool.create.CreateCardsPoolAction
import org.plsk.cardsPool.getCards.GetCardsQueryHandler
import org.plsk.cardsPool.promoteCard.*
import org.plsk.cardsPool.removeCard.RemoveCard
import org.plsk.cardsPool.removeCard.RemoveCardAction
import org.plsk.cardsPool.removeCard.RemoveCardValidated
import org.plsk.cardsPool.removeCard.RemoveCardValidation
import org.plsk.core.clock.Clock
import org.plsk.core.clock.UTCDatetimeClock
import org.plsk.core.event.EventBus
import org.plsk.core.id.IdGen
import org.plsk.core.validation.Validation
import org.plsk.mongo.cardsPool.DocumentCardsPoolRepository
import org.springframework.context.annotation.*;
import java.util.*

@Configuration
class CardsPoolConfig {

  @Bean
  fun provideCardsPoolRepository(db: MongoDatabase): CardsPoolRepository =
      DocumentCardsPoolRepository(db)

  @Bean
  fun provideCardsPoolValidator(uuidGen: IdGen<UUID>): Validation<CreateCardsPool, CardsPool> =
    CardsPoolValidation(
      uuidGen,
      UTCDatetimeClock
    )

  @Bean
  fun provideCreateCardsPoolHandler(cardsPoolValidator: Validation<CreateCardsPool, CardsPool>, eventBus: EventBus): CreateCardsPoolAction =
      CreateCardsPoolAction(cardsPoolValidator, eventBus)

  @Bean
  fun provideAddCardValidation(cardsPoolRepository: CardsPoolRepository, clock: Clock, idGen: IdGen<UUID>): Validation<AddCard, Card> =
      AddCardValidation(cardsPoolRepository, clock, idGen)

  @Bean
  fun provideAddCardHandler(validation: Validation<AddCard, Card>, cardsPoolRepository: CardsPoolRepository, eventBus: EventBus): AddCardAction =
      AddCardAction(validation, cardsPoolRepository, eventBus)

  @Bean
  fun providePromoteCardValidation(cardsPoolRepository: CardsPoolRepository): Validation<PromoteType, PromoteCardValidated> =
      PromoteCardValidation(cardsPoolRepository)

  @Bean
  fun providePromoteCardHander(validation: Validation<PromoteType, PromoteCardValidated>, eventBus: EventBus): PromoteCardAction =
      PromoteCardAction(validation, eventBus)

  @Bean
  fun provideRemoveCardValidation(cardsPoolRepository: CardsPoolRepository): Validation<RemoveCard, RemoveCardValidated> =
      RemoveCardValidation(cardsPoolRepository)

  @Bean
  fun provideRemoveCard(validation: Validation<RemoveCard, RemoveCardValidated>, eventBus: EventBus): RemoveCardAction =
      RemoveCardAction(validation, eventBus)

  @Bean
  fun provideGetCardsQueryHandler(cardsPoolRepository: CardsPoolRepository): GetCardsQueryHandler = GetCardsQueryHandler(cardsPoolRepository)
}
