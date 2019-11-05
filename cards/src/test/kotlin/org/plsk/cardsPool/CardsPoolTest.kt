package org.plsk.cardsPool

import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.plsk.cards.Card
import org.plsk.core.clock.FakeClock
import org.plsk.user.FakeUser
import java.util.*

class CardsPoolTest  : WordSpec() {

  init {

    "cards pool" should {

      "add a new card at the end of the list" {
        val inserted = baseCardsPool.addCard(card, 10)
        inserted.cards shouldBe mapOf(
          Pair(card1.id, card1),
          Pair(card2.id, card2),
          Pair(card3.id, card3),
          Pair(card4.id, card4),
          Pair(card5.id, card5),
          Pair(card.id, card)
        )

        inserted.stock shouldBe setOf(
            card4.id,
            card5.id,
            card.id
        )
      }

      "add a new card at the beginning of the list" {
        val inserted = baseCardsPool.addCard(card, 0)
        inserted.cards shouldBe mapOf(
            Pair(card1.id, card1),
            Pair(card2.id, card2),
            Pair(card3.id, card3),
            Pair(card4.id, card4),
            Pair(card5.id, card5),
            Pair(card.id, card)
        )

        inserted.stock shouldBe setOf(
            card.id,
            card4.id,
            card5.id
        )
      }

      "add a new card in the middle of the list" {
        val inserted = baseCardsPool.addCard(card, 1)
        inserted.cards shouldBe mapOf(
          Pair(card1.id, card1),
          Pair(card2.id, card2),
          Pair(card3.id, card3),
          Pair(card4.id, card4),
          Pair(card5.id, card5),
          Pair(card.id, card)
        )
        inserted.stock shouldBe setOf(
            card4.id,
            card.id,
            card5.id
        )
      }

      "promote a card to the end of the top cards list" {
        val promoted = baseCardsPool.promote(card4.id, 3)
        promoted.topCards shouldContainExactly setOf(card1.id, card2.id, card3.id, card4.id)
      }

      "promote a card to the beginning of the top cards list" {
        val promoted = baseCardsPool.promote(card4.id, 0)
        promoted.topCards shouldContainExactly setOf(card4.id, card1.id, card2.id, card3.id)
      }

      "promote a card as a second element of the top cards list" {
        val promoted = baseCardsPool.promote(card4.id, 1)
        val expected = CardsPool(
            baseCardsPool.id,
            "test cards pool",
            "desc",
            10,
            cards,
            clock.now().timestamp(),
            FakeUser,
            setOf(card5.id),
            setOf(card1.id, card4.id, card2.id, card3.id)
        )
        promoted shouldBe expected
        promoted.topCards shouldContainExactly expected.topCards
      }

      "remove a card" {
        val removed = baseCardsPool.remove(card1.id)
        val expected =  CardsPool(
            baseCardsPool.id,
            "test cards pool",
            "desc",
            10,
            mapOf(Pair(card2.id, card2), Pair(card3.id, card3), Pair(card4.id, card4), Pair(card5.id, card5)),
            clock.now().timestamp(),
            FakeUser,
            setOf(card4.id, card5.id),
            setOf(card2.id, card3.id)
        )
        removed shouldBe expected
      }

      "unpromote a card" {
        val removed = baseCardsPool.unpromote(card1.id)
        val expected =  CardsPool(
            baseCardsPool.id,
            "test cards pool",
            "desc",
            10,
            cards,
            clock.now().timestamp(),
            FakeUser,
            setOf(card1.id, card4.id, card5.id),
            setOf(card2.id, card3.id)
        )
        removed shouldBe expected
      }

      "move a card down" {
        val moved = baseCardsPool.promote(card4.id, 3).promote(card5.id, 4).moveCard(card1.id, 2)
        val expected =  CardsPool(
            baseCardsPool.id,
            "test cards pool",
            "desc",
            10,
            cards,
            clock.now().timestamp(),
            FakeUser,
            emptySet(),
            setOf(card2.id, card3.id, card1.id, card4.id, card5.id)
        )
        moved shouldBe expected
        moved.topCards shouldContainExactly expected.topCards
      }

      "move a card up" {
        val moved = baseCardsPool.promote(card4.id, 3).promote(card5.id, 4).moveCard(card4.id, 1)
        val expected =  CardsPool(
            baseCardsPool.id,
            "test cards pool",
            "desc",
            10,
            cards,
            clock.now().timestamp(),
            FakeUser,
            emptySet(),
            setOf(card1.id, card4.id, card2.id, card3.id, card5.id)
        )
        moved shouldBe expected
        moved.topCards shouldContainExactly expected.topCards
      }

      "yield the highlighted cards with the expected sort" {
        val newOrderCardPool = baseCardsPool.copy(topCards = setOf(card3.id, card2.id, card1.id))
        val highlighted = newOrderCardPool.getHighlighted()
        val expected = setOf(card3, card2, card1)
        highlighted shouldContainExactly expected
      }

      "yield the pools cards with the expected sort" {
        val newOrderCardPool = baseCardsPool.copy(stock = setOf(card1.id, card5.id, card4.id, card2.id))
        val pool = newOrderCardPool.getPool()
        val expected = setOf(card1, card5, card4, card2)
        pool shouldContainExactly expected
      }

    }
  }

  val clock = FakeClock()

  val card = Card(UUID.randomUUID(), "test-card", "desc", clock.now().timestamp())

  val card1 = Card(UUID.randomUUID(), "test-card 1", "desc", clock.now().timestamp())
  val card2 = Card(UUID.randomUUID(), "test-card 2", "desc", clock.now().timestamp())
  val card3 = Card(UUID.randomUUID(), "test-card 3", "desc", clock.now().timestamp())
  val card4 = Card(UUID.randomUUID(), "test-card 4", "desc", clock.now().timestamp())
  val card5 = Card(UUID.randomUUID(), "test-card 5", "desc", clock.now().timestamp())

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
      clock.now().timestamp(),
      FakeUser,
      setOf(card4.id, card5.id),
      setOf(card1.id, card2.id, card3.id)
  )
}
