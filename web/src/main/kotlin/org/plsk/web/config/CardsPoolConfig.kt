package org.plsk.web.config

import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.addCard.AddCard
import org.plsk.cardsPool.addCard.AddCardHandler
import org.plsk.cardsPool.addCard.AddCardValidation
import org.plsk.cardsPool.create.CardsPoolValidation
import org.plsk.cardsPool.create.CreateCardsPool
import org.plsk.cardsPool.create.CreateCardsPoolHandler
import org.plsk.core.clock.Clock
import org.plsk.core.clock.UTCDatetimeClock
import org.plsk.core.command.CommandHandler
import org.plsk.core.event.EventBus
import org.plsk.core.id.IdGen
import org.plsk.core.validation.Validation
import org.plsk.user.UnknownUser
import org.springframework.context.annotation.*;
import java.util.*

@Configuration
class CardsPoolConfig {

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
  fun provideCardsPoolRepository(): CardsPoolRepository = object: CardsPoolRepository{
    override fun find(id: UUID): CardsPool? =
        CardsPool(
          id = UUID.fromString("993eb0ff-128e-3f82-840c-a1124e6256ec"),
          name = "test2",
          description = "un test",
          createdAt = 1568299228416,
          createdBy = UnknownUser("113eb0ff-128e-3f82-840c-a1124e6256ec")
        )
  }

  @Bean
  fun provideAddCardValidation(cardsPoolRepository: CardsPoolRepository, clock: Clock): Validation<AddCard, CardsPool> =
      AddCardValidation(cardsPoolRepository, clock)

  @Bean
  fun provideAddCardHandler(validation: Validation<AddCard, CardsPool>, eventBus: EventBus): CommandHandler<AddCard, UUID> =
      AddCardHandler(validation, eventBus)
}