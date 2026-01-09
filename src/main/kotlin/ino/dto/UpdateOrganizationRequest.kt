package ino.dto

data class UpdateOrganizationRequest(
    val id: String,
    val name: String?,
    val phoneNumber: String?,
    val email: String?,
    val status: String?
)
