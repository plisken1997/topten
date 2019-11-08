package org.plsk.mongo.security

import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.rxjava2.single
import org.litote.kmongo.util.idValue
import org.plsk.core.dao.QueryFilter
import org.plsk.mongo.MongoClient
import org.plsk.security.accessToken.AccessToken
import org.plsk.security.accessToken.AccessTokenRepository

class DocumentAccessTokenRepository(db: MongoDatabase): AccessTokenRepository, MongoClient<MongoAccessToken> {

  override val coll: MongoCollection<MongoAccessToken> = db.getCollection<MongoAccessToken>()

  override fun find(id: String): AccessToken? = TODO("not implemented")
  override fun findAll(filter: Iterable<QueryFilter>): List<AccessToken> = TODO("not implemented")
  override fun update(data: AccessToken): String =  TODO("not implemented")

  override fun store(data: AccessToken): String =
      coll.insertOne(data.toDTO()).single()
          .map{ it.idValue.toString() }
          .blockingGet()
}
