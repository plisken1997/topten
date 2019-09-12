package org.plsk.web.config

import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.create.CardsPoolValidation
import org.plsk.cardsPool.create.CreateCardsPool
import org.plsk.cardsPool.create.CreateCardsPoolHandler
import org.plsk.core.clock.UTCDatetimeClock
import org.plsk.core.event.EventBus
import org.plsk.core.id.IdGen
import org.plsk.core.validation.Validation
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
  fun provideCreateCardsPoolHandler(cardsPoolValidator: Validation<CreateCardsPool, CardsPool>, eventBus: EventBus): CreateCardsPoolHandler =
      CreateCardsPoolHandler(cardsPoolValidator, eventBus)

}