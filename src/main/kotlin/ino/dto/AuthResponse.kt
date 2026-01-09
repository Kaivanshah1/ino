package ino.dto

data class AuthResponse(
    val userId: String,
    val email: String,
    val password: String? = null,
    val message: String = "Authentication successful"
)

data class AuthResponseWithSession(
    val userId: String,
    val email: String,
    val sessionId: String,
    val password: String? = null,
    val message: String = "Authentication successful"
)

