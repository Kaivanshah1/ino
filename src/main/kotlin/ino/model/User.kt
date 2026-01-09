package ino.model

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val id = varchar("id", 255)
    val name = text("name").nullable()
    val role = text("role").nullable()
    val phoneNumber = varchar("phone_number", 20).nullable()
    val email = varchar("email", 100).nullable()
    val organizationId = varchar("organization_id", 255).nullable()
    val status = varchar("status", 20).nullable()
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
    
    override val primaryKey = PrimaryKey(id)
}

data class User(
    val id: String,
    val name: String?,
    val phoneNumber: String?,
    val email: String?,
    val organizationId: String?,
    val status: String?,
    val role: String?,
    val createdAt: Long,
    val updatedAt: Long
)
