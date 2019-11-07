package org.plsk.security

sealed class AuthenticationRequest {
  abstract val token: String
}

data class UnknownUser(override val token: String): AuthenticationRequest()
data class AccessToken(override val token: String): AuthenticationRequest()
