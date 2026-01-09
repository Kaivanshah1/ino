package ino.dto

data class CreateUserRequest(
    val name: String,
    val phoneNumber: String?,
    val email: String?,
    val role: String?,
    val organizationId: String?,
    val status: String
)
