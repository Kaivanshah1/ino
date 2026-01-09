package ino.dto

data class UserResponse(
    val id: String,
    val name: String?,
    val phoneNumber: String?,
    val email: String?,
    val organizationId: String?,
    val status: String?,
    val createdAt: Long,
    val updatedAt: Long
)
