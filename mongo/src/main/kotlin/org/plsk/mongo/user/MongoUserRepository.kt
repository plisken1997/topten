package org.plsk.mongo.user

import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.rxjava2.single
import org.litote.kmongo.util.idValue
import org.plsk.core.dao.QueryFilter
import org.plsk.mongo.MongoClient
import org.plsk.user.User
import org.plsk.user.dao.UserRepository

class MongoUserRepository(db: MongoDatabase): UserRepository, MongoClient<MongoUser> {

  override val coll: MongoCollection<MongoUser> = db.getCollection<MongoUser>()

  override fun find(id: String): User? = TODO("not implemented")
  override fun update(data: User): String = TODO("not implemented")

  override fun findAll(filter: Iterable<QueryFilter>): List<User> {
    TODO("not implemented")
  }

  override fun store(data: User): String =
      coll.insertOne(data.toDTO()).single()
          .map{ it.idValue.toString() }
          .blockingGet()
}