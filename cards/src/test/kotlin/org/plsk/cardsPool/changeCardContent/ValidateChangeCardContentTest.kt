package org.plsk.cardsPool.changeCardContent

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.WordSpec
import kotlinx.coroutines.runBlocking
import org.plsk.cards.Card
import org.plsk.cardsPool.CardsPool
import org.plsk.cardsPool.CardsPoolRepository
import org.plsk.cardsPool.DisplayType
import org.plsk.cardsPool.WriteResult
import org.plsk.cardsPool.promoteCard.CardNotFound
import org.plsk.core.clock.FakeClock
import org.plsk.core.dao.QueryFilter
import org.plsk.user.FakeUser
import java.util.*

class ValidateChangeCardContentTest: WordSpec() {

  init {

    "validate change card content" should {

      "fail when the user is not granted" {
        runBlocking {
          shouldThrowExactly<Exception> {
            validator.validate(validCommand.copy(userId = UUID.randomUUID().toString()))
          }
        }
      }

      "fail when the cardspool is not found" {
        runBlocking {
          shouldThrowExactly<Exception> {
            validator.validate(validCommand.copy(cardsPoolId = UUID.randomUUID()))
          }
        }
      }

      "fail when the card is not found" {
        runBlocking {
          shouldThrowExactly<CardNotFound> {
            validator.validate(validCommand.copy(cardId = UUID.randomUUID()))
          }
        }
      }

      "fail when the field does not exists" {
        runBlocking {
          shouldThrowExactly<Exception> {
            validator.validate(validCommand.copy(field = "some field"))
          }
        }
      }

      "succeed when the command is valid" {
        runBlocking {
          val result = validator.validate(validCommand)
          val expected =
              ChangeCardContentValidated(
                  card1.id,
                  baseCardsPool.copy(cards = mapOf(Pair(card1.id, card1.copy(label = "update name"))))
              )
          result shouldBe expected
        }
      }

    }

  }

  val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", FakeClock.now().timestamp())

  val baseCardsPool = CardsPool(
      UUID.randomUUID(),
      "test cards pool",
      "desc",
      10,
      mapOf(Pair(card1.id, card1)),
      DisplayType.ASC,
      FakeClock.now().timestamp(),
      FakeUser.id
  )

  val validCommand =
      ChangeCardContent(card1.id, baseCardsPool.id, "label", "update name", FakeUser.id)

  val cardsPoolRepository: CardsPoolRepository = object: CardsPoolRepository {
    override suspend fun find(id: UUID): CardsPool? =
        if(id == baseCardsPool.id) baseCardsPool
        else null
    override suspend fun findAll(filter: Iterable<QueryFilter>): List<CardsPool> = TODO("Not yet implemented")
    override suspend fun store(data: CardsPool): WriteResult = TODO("Not yet implemented")
    override suspend fun update(data: CardsPool): WriteResult = TODO("Not yet implemented")
  }
  val validator = ValidateChangeCardContent(cardsPoolRepository)
}