package org.plsk.mongo.security

import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.rxjava2.findOne
import org.litote.kmongo.rxjava2.single
import org.litote.kmongo.util.idValue
import org.plsk.core.dao.QueryFilter
import org.plsk.mongo.MongoClient
import org.plsk.security.accessToken.AccessTokenRepository
import org.plsk.security.accessToken.UserAccessToken

class DocumentAccessTokenRepository(db: MongoDatabase): AccessTokenRepository, MongoClient<MongoAccessToken> {

  override val coll: MongoCollection<MongoAccessToken> = db.getCollection<MongoAccessToken>()

  override fun findAll(filter: Iterable<QueryFilter>): List<UserAccessToken> = throw Exception("insuported operation")
  override fun update(data: UserAccessToken): String =  throw Exception("insuported operation")

  override fun find(id: String): UserAccessToken? =
      coll.findOne(MongoAccessToken::token eq id).map{ it.toModel() }.blockingGet()

  override fun store(data: UserAccessToken): String =
      coll.insertOne(data.toDTO()).single()
          .map{ it.idValue.toString() }
          .blockingGet()
}
