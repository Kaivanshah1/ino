package ino.model

import org.jetbrains.exposed.sql.Table

object Organizations : Table("organization") {
    val id = varchar("id", 255)
    val name = text("name").nullable()
    val phoneNumber = varchar("phone_number", 20).nullable()
    val email = varchar("email", 100).nullable()
    val status = varchar("status", 20).nullable()
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
    
    override val primaryKey = PrimaryKey(id)
}

data class Organization(
    val id: String,
    val name: String?,
    val phoneNumber: String?,
    val email: String?,
    val status: String?,
    val createdAt: Long,
    val updatedAt: Long
)
