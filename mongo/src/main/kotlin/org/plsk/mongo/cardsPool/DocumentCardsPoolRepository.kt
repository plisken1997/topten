package org.plsk.mongo.cardsPool

import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import java.util.*
import org.litote.kmongo.*
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.rxjava2.findOne
import org.litote.kmongo.rxjava2.single
import org.litote.kmongo.rxjava2.updateOne
import org.plsk.cardsPool.*
import org.plsk.core.dao.QueryFilter
import org.plsk.mongo.MongoClient

class DocumentCardsPoolRepository(db: MongoDatabase) : CardsPoolRepository, MongoClient<MongoCardsPool> {
  override fun findAll(filter: Iterable<QueryFilter>): List<CardsPool> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override val coll: MongoCollection<MongoCardsPool> = db.getCollection<MongoCardsPool>()

  override fun store(data: CardsPool): WriteResult =
      coll.insertOne(data.toDTO()).single()
          .map { WriteSuccess(data.id) }
          .blockingGet()

  override fun update(data: CardsPool): WriteResult =
      coll.updateOne(
          MongoCardsPool::id eq data.id.toString(),
          data.toDTO()
      )
          .map { WriteSuccess(data.id) }
          .blockingGet()

  override fun find(id: UUID): CardsPool? = coll.findOne(MongoCardsPool::id eq id.toString()).map { it.toModel() }.blockingGet()

}
