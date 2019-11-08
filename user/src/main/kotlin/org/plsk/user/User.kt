package org.plsk.user

interface User {
    val id: String
    val name: String
}

data class UnknownUser(override val id: String): User{
  override val name = "unknown"
}

data class AppUser(override val id: String, override val name: String): User

// @todo move to user-test
object FakeUser : User {
    override val id: String = "3c5c2d0f-5fd3-4852-9493-a5025d6fd7d9"
    override val name: String = "topten-u1"
}