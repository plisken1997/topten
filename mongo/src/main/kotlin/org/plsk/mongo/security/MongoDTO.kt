package org.plsk.mongo.security

import org.plsk.security.accessToken.AccessToken
import java.time.Instant

data class MongoAccessToken(val token: String, val timestamp: Long)

fun AccessToken.toDTO(timestamp: Long = Instant.now().toEpochMilli()): MongoAccessToken = MongoAccessToken(this.token, timestamp)
