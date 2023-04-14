package org.plsk.mongo

import com.mongodb.reactivestreams.client.MongoCollection

interface MongoClient<T> {
  val coll: MongoCollection<T>
}