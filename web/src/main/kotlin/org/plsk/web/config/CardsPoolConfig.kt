package org.plsk.web.config

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
import org.plsk.core.clock.FakeClock
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

    val clock = FakeClock()

    val card1 = Card(UUID.fromString("9458c08d-0cfc-3682-9fb7-d3fac5abace2"), "test-card 1", clock.now().timestamp())
    val card2 = Card(UUID.fromString("cc434558-7e99-37cf-88cd-b90223293d3d"), "test-card 2", clock.now().timestamp())
    val card3 = Card(UUID.fromString("d716397b-4751-30d8-be01-14c2148999ca"), "test-card 3", clock.now().timestamp())

    val cards = listOf<Card>(
        card1,
        card2,
        card3)

    override fun find(id: UUID): CardsPool? =
        CardsPool(
          id = UUID.fromString("993eb0ff-128e-3f82-840c-a1124e6256ec"),
          name = "test2",
          description = "un test",
          createdAt = 1568299228416,
          createdBy = UnknownUser("113eb0ff-128e-3f82-840c-a1124e6256ec"),
          cards = cards,
          stock = listOf(card1.id, card2.id, card3.id),
          topCards = listOf(card1.id)
        )
  }

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