package org.plsk.mongo.user

import org.plsk.user.AppUser
import org.plsk.user.TmpUser
import org.plsk.user.UnknownUser
import org.plsk.user.User

data class MongoUser(
    val id: String,
    val type: String,
    val name: String? = null,
    val ipAddress: String? = null,
    val password: String = "",
    val grants: List<String> = emptyList()) {

  companion object {
      val TYPE_UNKNOWN = "unknown"
      val TYPE_TMPUSER = "tmpUser"
      val TYPE_APPUSER = "appUser"
  }

  fun toModel() =
      when(type) {
        TYPE_UNKNOWN -> UnknownUser(id)
        TYPE_TMPUSER -> TmpUser(id, name!!, ipAddress!!, password, grants)
        else -> AppUser(id, name!!)
      }
}

fun User.toDTO() =
    when(this) {
      is AppUser -> MongoUser(this.id, MongoUser.TYPE_APPUSER, this.name)
      is TmpUser -> MongoUser(this.id, MongoUser.TYPE_TMPUSER, this.name, this.ipAddress, this.password, this.grants)
      else -> MongoUser(this.id, MongoUser.TYPE_UNKNOWN)
    }
