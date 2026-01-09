package ino.dto

data class FindAllOrganizationsRequest(
    val search: String? = null,
    val getAll: Boolean = false,
    val page: Int = 0,
    val size: Int = 10
)
