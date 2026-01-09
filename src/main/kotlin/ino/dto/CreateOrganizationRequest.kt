package ino.dto

data class CreateOrganizationRequest(
    val name: String?,
    val phoneNumber: String?,
    val email: String?,
    val status: String?
)
