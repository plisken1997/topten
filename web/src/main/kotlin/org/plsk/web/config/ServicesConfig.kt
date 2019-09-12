package org.plsk.web.config

import org.plsk.cardsPool.create.CardsPoolCreated
import org.plsk.core.event.Event
import org.plsk.core.event.EventBus
import org.plsk.core.id.IdGen
import org.plsk.core.id.UUIDGen
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class ServicesConfig {

  @Bean
  fun provideUUIDGen(): IdGen<UUID> = UUIDGen()

  @Bean
  fun eventBus(): EventBus = object : EventBus {
    private val logger = LoggerFactory.getLogger("EventBusLogger")

    override fun publish(event: Event) = when (event) {
      is CardsPoolCreated -> logger.info("store $event")
      else -> Unit
    }
  }

}