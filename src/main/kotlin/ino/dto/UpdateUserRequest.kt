package ino.dto

data class UpdateUserRequest(
    val id: String,
    val name: String?,
    val phoneNumber: String?,
    val email: String?,
    val role: String?,
    val organizationId: String?,
    val status: String?
)
