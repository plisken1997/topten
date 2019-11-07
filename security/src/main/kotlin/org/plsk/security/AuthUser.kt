package org.plsk.security

data class AuthUser(
    val id: String,
    val name: String,
    val password: String,
    val grants: List<String>
) {
  companion object{
    fun TmpAuthUser(id: String): AuthUser = AuthUser(id, "", "", listOf("topten"))
  }
}
