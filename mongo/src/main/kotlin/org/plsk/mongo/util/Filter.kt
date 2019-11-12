package org.plsk.mongo.util

import org.bson.BsonArray
import org.bson.BsonDocument
import org.bson.BsonElement
import org.bson.BsonString
import org.plsk.core.dao.EqFilter
import org.plsk.core.dao.InFilter
import org.plsk.core.dao.QueryFilter

fun QueryFilter.toBson(): BsonElement = when(this) {
  is EqFilter<*> -> BsonElement(this.name, BsonString(this.value.toString()))
  is InFilter<*> -> BsonElement(this.name, BsonDocument("\$in", BsonArray(this.value.map{ BsonString(it.toString()) })))
}
