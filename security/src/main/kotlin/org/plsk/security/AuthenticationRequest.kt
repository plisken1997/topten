package org.plsk.security

sealed class AuthenticationRequest {
  abstract val token: String
}

data class UnknownUserRequest(override val token: String): AuthenticationRequest()
data class AccessTokenRequest(override val token: String): AuthenticationRequest()
