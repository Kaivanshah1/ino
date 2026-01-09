package ino.dto

data class OrganizationResponse(
    val id: String,
    val name: String?,
    val phoneNumber: String?,
    val email: String?,
    val status: String?,
    val createdAt: Long,
    val updatedAt: Long
)
