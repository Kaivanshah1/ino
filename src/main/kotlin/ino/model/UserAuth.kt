package ino.model

import org.jetbrains.exposed.sql.Table

object UserAuths : Table("user_auth") {
    val id = varchar("id", 255)
    val userId = text("user_id")
    val username = text("username")
    val hashPassword = text("hash_password").nullable()
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
    
    override val primaryKey = PrimaryKey(id)
}

data class UserAuth(
    val id: String,
    val userId: String,
    val username: String,
    val email: String,
    val hashPassword: String?,
    val createdAt: Long,
    val updatedAt: Long
)
