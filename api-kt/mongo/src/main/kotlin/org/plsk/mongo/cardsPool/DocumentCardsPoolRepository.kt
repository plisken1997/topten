package org.plsk.mongo.cardsPool

import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.BsonDocument
import org.litote.kmongo.*
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.rxjava2.findOne
import org.litote.kmongo.rxjava2.single
import org.litote.kmongo.rxjava2.toObservable
import org.litote.kmongo.rxjava2.updateOne
import org.plsk.cardsPool.*
import org.plsk.core.dao.EqFilter
import org.plsk.core.dao.QueryFilter
import org.plsk.mongo.MongoClient
import org.plsk.mongo.util.toBson

class DocumentCardsPoolRepository(db: MongoDatabase) : CardsPoolRepository, MongoClient<MongoCardsPool> {

  override val coll: MongoCollection<MongoCardsPool> = db.getCollection<MongoCardsPool>()
  override suspend fun findByUser(query: GetCardsPoolByUser): List<CardsPool> =
      filterAll(listOf(EqFilter("createdBy", query.userId)))

  private fun filterAll(filter: Iterable<QueryFilter>): List<CardsPool> =
      coll.find(BsonDocument(filter.map{it.toBson()}))
          .toObservable().map{ it.toModel() }.blockingIterable().toList()

  override suspend fun add(data: CardsPool): WriteResult =
      coll.insertOne(data.toDTO()).single()
          .map { WriteSuccess(data.id) }
          .blockingGet()

  override suspend fun update(data: CardsPool): WriteResult =
      coll.updateOne(
          MongoCardsPool::id eq data.id.toString(),
          data.toDTO()
      )
          .map { WriteSuccess(data.id) }
          .blockingGet()

  // @todo filter on query.userId
  override suspend fun find(query: GetCardsQuery): CardsPool? =
      coll.findOne(MongoCardsPool::id eq query.cardsPoolId.toString()).map { it.toModel() }.blockingGet()

}
