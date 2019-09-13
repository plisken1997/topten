package org.plsk.web.config

import com.mongodb.reactivestreams.client.MongoDatabase
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.addCard.AddCard
import org.plsk.cardsPool.addCard.AddCardHandler
import org.plsk.cardsPool.addCard.AddCardValidation
import org.plsk.cardsPool.create.CardsPoolValidation
import org.plsk.cardsPool.create.CreateCardsPool
import org.plsk.cardsPool.create.CreateCardsPoolHandler
import org.plsk.cardsPool.promoteCard.PromoteCard
import org.plsk.cardsPool.promoteCard.PromoteCardHander
import org.plsk.cardsPool.promoteCard.PromoteCardValidated
import org.plsk.cardsPool.promoteCard.PromoteCardValidation
import org.plsk.core.clock.Clock
import org.plsk.core.clock.UTCDatetimeClock
import org.plsk.core.command.CommandHandler
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
  fun provideCreateCardsPoolHandler(cardsPoolValidator: Validation<CreateCardsPool, CardsPool>, eventBus: EventBus): CommandHandler<CreateCardsPool, UUID> =
      CreateCardsPoolHandler(cardsPoolValidator, eventBus)

  @Bean
  fun provideAddCardValidation(cardsPoolRepository: CardsPoolRepository, clock: Clock, idGen: IdGen<UUID>): Validation<AddCard, Card> =
      AddCardValidation(cardsPoolRepository, clock, idGen)

  @Bean
  fun provideAddCardHandler(validation: Validation<AddCard, Card>, cardsPoolRepository: CardsPoolRepository, eventBus: EventBus): CommandHandler<AddCard, UUID> =
      AddCardHandler(validation, cardsPoolRepository, eventBus)

  @Bean
  fun providePromoteCardValidation(cardsPoolRepository: CardsPoolRepository): Validation<PromoteCard, PromoteCardValidated> =
      PromoteCardValidation(cardsPoolRepository)

  @Bean
  fun providePromoteCardHander(validation: Validation<PromoteCard, PromoteCardValidated>, eventBus: EventBus): CommandHandler<PromoteCard, List<UUID>> =
      PromoteCardHander(validation, eventBus)
}