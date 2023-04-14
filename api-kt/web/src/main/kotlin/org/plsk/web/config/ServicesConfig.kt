package org.plsk.web.config

import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.CardsPoolEventHandler
import org.plsk.core.clock.Clock
import org.plsk.core.clock.UTCDatetimeClock
import org.plsk.core.event.EventBus
import org.plsk.core.event.EventHandler
import org.plsk.core.event.SyncEventBus
import org.plsk.core.id.IdGen
import org.plsk.core.id.UUIDGen
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class ServicesConfig {

  @Bean
  fun provideUUIDGen(): IdGen<UUID> = UUIDGen()

  @Bean
  fun provideClock(): Clock = UTCDatetimeClock

  @Bean
  fun eventBus(cardsPoolRepository: CardsPoolRepository): EventBus {
    val handlers =
        listOf<EventHandler>(
            CardsPoolEventHandler(cardsPoolRepository)
        )
    return SyncEventBus(handlers)
  }

}