package org.plsk.security

import org.plsk.user.User

data class AuthUser(
    val id: String,
    val name: String,
    val password: String,
    val grants: List<String>
) {
  companion object{
    fun TmpAuthUser(user: User): AuthUser = AuthUser(user.id, user.name, "", listOf("topten"))
  }
}
