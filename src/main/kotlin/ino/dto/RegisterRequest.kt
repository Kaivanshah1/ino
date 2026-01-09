package ino.dto

data class RegisterRequest(
    val email: String,
    val name: String,
    val userName: String,
    val phoneNumber: String? = null,
    val role: String? = null,
    val organizationId: String? = null,
    val status: String
)
