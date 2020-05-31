package org.plsk.cardsPool

import io.kotlintest.specs.WordSpec
import org.plsk.cards.Card
import org.plsk.core.clock.FakeClock
import org.plsk.core.id.UUIDGen
import java.util.*

abstract class BaseCardsActionTest: WordSpec(), CardsPollRepositoryFactory {

  val idGen = UUIDGen()

  val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", FakeClock.now().timestamp())
  val card2 = Card(UUID.randomUUID(), "test-card 2", "desc", FakeClock.now().timestamp())
  val card3 = Card(UUID.randomUUID(), "test-card 3", "desc", FakeClock.now().timestamp())
  val card4 = Card(UUID.randomUUID(), "test-card 4", "desc", FakeClock.now().timestamp())
  val card5 = Card(UUID.randomUUID(), "test-card 5", "desc", FakeClock.now().timestamp())
}
