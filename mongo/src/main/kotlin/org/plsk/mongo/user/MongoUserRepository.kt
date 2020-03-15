package org.plsk.mongo.user

import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.BsonDocument
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.rxjava2.findOne
import org.litote.kmongo.rxjava2.single
import org.litote.kmongo.rxjava2.toObservable
import org.litote.kmongo.util.idValue
import org.plsk.core.dao.QueryFilter
import org.plsk.mongo.MongoClient
import org.plsk.mongo.util.toBson
import org.plsk.user.User
import org.plsk.user.dao.UserRepository

class MongoUserRepository(db: MongoDatabase): UserRepository, MongoClient<MongoUser> {

  override val coll: MongoCollection<MongoUser> = db.getCollection<MongoUser>()

  override fun find(id: String): User? = coll.findOne( MongoUser::id eq id).map{ it.toModel() }.blockingGet()
  override fun update(data: User): String = TODO("not implemented")

  override fun findAll(filter: Iterable<QueryFilter>): List<User> =
      coll.find(BsonDocument(filter.map{it.toBson()})).toObservable().map{ it.toModel() }.blockingIterable().toList()

  override fun store(data: User): String =
      coll.insertOne(data.toDTO()).single()
          .map{ it.idValue.toString() }
          .blockingGet()
}
