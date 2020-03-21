package org.plsk.cardsPool.addCard

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.WriteResult
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.QueryFilter
import org.plsk.core.id.UUIDGen
import org.plsk.user.FakeUser
import java.util.*

class AddCardValidationTest : WordSpec() {

  init {

    "add card validation" When {

      "validate an unknown cards pool" should {

        "fail to validate" {
          runBlocking {

            val command = AddCard("insert-1", "desc", 1, UUID.nameUUIDFromBytes("test".toByteArray()))
            // @todo will be better tested after implemented the monadic effect on validation output
            shouldThrowExactly<Exception> {
              validation.validate(command)
            }
          }
        }
      }

      "validate an existing label" should {

        "fail to validate" {
          runBlocking {
            val command = AddCard("test-card 2", "desc", 1, baseCardsPool.id)
            // @todo will be better tested after implemented the monadic effect on validation output
            shouldThrowExactly<Exception> {
              validation.validate(command)
            }
          }
        }

      }

      "validate the card" should {

        "add the card to the cards pool" {
          runBlocking {
            val command = AddCard("test-card", "desc", 1, baseCardsPool.id)
            val newCardsPool = validation.validate(command)
            val expected = Card(
                idGen.fromString("test-card" + baseCardsPool.id.toString()),
                "test-card",
                "desc",
                FakeClock.now().timestamp()
            )

            newCardsPool shouldBe expected
          }
        }
      }

    }

  }

  val idGen = UUIDGen()

  val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", FakeClock.now().timestamp())
  val card2 = Card(UUID.randomUUID(), "test-card 2", "desc", FakeClock.now().timestamp())
  val card3 = Card(UUID.randomUUID(), "test-card 3", "desc", FakeClock.now().timestamp())

  val baseCardsPool = CardsPool(
      UUID.randomUUID(),
      "test cards pool",
      "desc",
      10,
      mapOf(Pair(card1.id, card1), Pair(card2.id, card2), Pair(card3.id, card3)),
      FakeClock.now().timestamp(),
      FakeUser.id
  )

  val cardsPoolRepository: CardsPoolRepository = object : CardsPoolRepository {
    override suspend fun findAll(filter: Iterable<QueryFilter>): List<CardsPool> = TODO("not implemented")
    override suspend fun update(data: CardsPool): WriteResult = TODO("not implemented")
    override suspend fun store(data: CardsPool): WriteResult = TODO("not implemented")
    override suspend fun find(id: UUID): CardsPool? = if (id == baseCardsPool.id) baseCardsPool else null
  }
  val validation = AddCardValidation(cardsPoolRepository, FakeClock, idGen)
}