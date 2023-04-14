package org.plsk.mongo.security

import org.plsk.core.clock.Datetime
import org.plsk.security.accessToken.AccessToken
import org.plsk.security.accessToken.UserAccessToken

data class MongoAccessToken(val userId: String, val token: String, val timestamp: Long) {
  fun toModel(): UserAccessToken = UserAccessToken(userId, AccessToken(token), Datetime.fromTimestamp(timestamp))
}

fun UserAccessToken.toDTO(): MongoAccessToken = MongoAccessToken(this.userId, this.token.token, this.expireAt.timestamp())
