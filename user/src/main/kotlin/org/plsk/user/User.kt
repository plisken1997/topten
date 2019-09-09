package org.plsk.user

interface User {
    val id: String
}

data class UnknownUser(override val id: String): User

object FakeUser : User {
    override val id: String = "3c5c2d0f-5fd3-4852-9493-a5025d6fd7d9"
}