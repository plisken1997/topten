package org.plsk.security.accessToken

import org.plsk.core.clock.Clock
import org.plsk.core.clock.Datetime
import org.plsk.core.dao.DataReader
import org.plsk.core.dao.DataWriter

data class UserAccessToken(val userId: String, val token: AccessToken, val expireAt: Datetime) {
  fun isValid(clock: Clock): Boolean = clock.now().isBefore(expireAt)
}

@Deprecated("AccessTokenRepository must be a designed according to it's usage instead of extends genreric interfaces")
interface AccessTokenRepository: DataReader<UserAccessToken, String>, DataWriter<UserAccessToken, String>
