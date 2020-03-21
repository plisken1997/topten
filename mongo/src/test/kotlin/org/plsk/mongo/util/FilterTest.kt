package org.plsk.mongo.util

import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.bson.BsonArray
import org.bson.BsonDocument
import org.bson.BsonElement
import org.bson.BsonString
import org.plsk.core.dao.EqFilter
import org.plsk.core.dao.InFilter

class FilterTest: WordSpec() {

  init {

    "queryfilter to bson" should {

      "convert a equality filter to a BsonDocument" {
        EqFilter("id", "1234").toBson() shouldBe BsonElement("id", BsonString("1234"))
      }

      "convert a in filter to a BsonArray" {
        InFilter("username", setOf("James", "Kelly", "Alice", "Bob")).toBson() shouldBe
            BsonElement("username", BsonDocument("\$in", BsonArray(listOf(BsonString("James"), BsonString("Kelly"), BsonString("Alice"), BsonString("Bob")))))
      }

    }

  }

}
