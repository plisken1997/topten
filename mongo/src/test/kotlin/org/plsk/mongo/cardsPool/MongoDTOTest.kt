package org.plsk.mongo.cardsPool

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.core.clock.FakeClock
import org.plsk.user.FakeUser
import java.time.ZonedDateTime
import java.util.*

class MongoDTOTest: WordSpec() {

  init {

    "mongo dto" should {

      "transform a card to a mongo DTO" {
        card1.toDTO() shouldBe MongoCard(card1.id.toString(), "test-card 1", "desc", timestamp)
      }

      "transform a mon card to a domain card" {
        MongoCard(card1.id.toString(), "test-card 1", "desc", timestamp).toModel() shouldBe card1
      }

      "transform a cards pool to a mongo DTO" {
        val mongoDTO = MongoCardsPool(
            baseCardsPool.id.toString(),
            "test cards pool",
            "desc",
            10,
            listOf(
                MongoCard(card1.id.toString(), "test-card 1", "desc", timestamp),
                MongoCard(card2.id.toString(), "test-card 2", "desc", timestamp),
                MongoCard(card3.id.toString(), "test-card 3", "desc", timestamp),
                MongoCard(card4.id.toString(), "test-card 4", "desc", timestamp),
                MongoCard(card5.id.toString(), "test-card 5", "desc", timestamp)
            ),
            timestamp,
            "3c5c2d0f-5fd3-4852-9493-a5025d6fd7d9",
            listOf(card4.id.toString(), card5.id.toString()),
            listOf(card1.id.toString(), card2.id.toString(), card3.id.toString())
        )
        baseCardsPool.toDTO() shouldBe mongoDTO
      }

      "transform a mongo cards pool to a domain cards pool" {
        val mongoDTO = MongoCardsPool(
            baseCardsPool.id.toString(),
            "test cards pool",
            "desc",
            10,
            listOf(
                MongoCard(card1.id.toString(), "test-card 1", "desc", timestamp),
                MongoCard(card2.id.toString(), "test-card 2", "desc", timestamp),
                MongoCard(card3.id.toString(), "test-card 3", "desc", timestamp),
                MongoCard(card4.id.toString(), "test-card 4", "desc", timestamp),
                MongoCard(card5.id.toString(), "test-card 5", "desc", timestamp)
            ),
            timestamp,
            "3c5c2d0f-5fd3-4852-9493-a5025d6fd7d9",
            listOf(card4.id.toString(), card5.id.toString()),
            listOf(card1.id.toString(), card2.id.toString(), card3.id.toString())
        )
        mongoDTO.toModel() shouldBe baseCardsPool
      }
    }

  }

  val timestamp = ZonedDateTime.parse("2013-12-27T15:35:00.000Z").toEpochSecond()

  val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", FakeClock.now().timestamp())
  val card2 = Card(UUID.randomUUID(), "test-card 2", "desc", FakeClock.now().timestamp())
  val card3 = Card(UUID.randomUUID(), "test-card 3", "desc", FakeClock.now().timestamp())
  val card4 = Card(UUID.randomUUID(), "test-card 4", "desc", FakeClock.now().timestamp())
  val card5 = Card(UUID.randomUUID(), "test-card 5", "desc", FakeClock.now().timestamp())

  val cards = mapOf<UUID, Card>(
      Pair(card1.id, card1),
      Pair(card2.id, card2),
      Pair(card3.id, card3),
      Pair(card4.id, card4),
      Pair(card5.id, card5)
  )

  val baseCardsPool = CardsPool(
      UUID.randomUUID(),
      "test cards pool",
      "desc",
      10,
      cards,
      FakeClock.now().timestamp(),
      FakeUser.id,
      setOf(card4.id, card5.id),
      setOf(card1.id, card2.id, card3.id)
  )

}