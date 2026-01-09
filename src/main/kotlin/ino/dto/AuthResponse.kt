package ino.dto

data class AuthResponse(
    val userId: String,
    val userName: String,
    val password: String? = null,
)

data class AuthResponseWithSession(
    val userId: String,
    val userName: String,
    val sessionId: String,
)