package ino.repository

import ino.model.UserAuth
import ino.model.UserAuths
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class AuthRepository {
    
    fun save(userAuth: UserAuth) {
        try {
            transaction {
                UserAuths.insert {
                    it[id] = userAuth.id
                    it[userId] = userAuth.id  // Using id as userId since they're the same
                    it[username] = userAuth.email  // Mapping email to username
                    it[hashPassword] = userAuth.hashPassword
                    it[createdAt] = userAuth.createdAt
                    it[updatedAt] = userAuth.updatedAt
                }
            }
        }catch (e: Exception) {
            throw e;
        }
    }
    
    fun update(userAuth: UserAuth) {
        transaction {
            UserAuths.update({ UserAuths.id eq userAuth.id }) {
                it[username] = userAuth.email
                it[hashPassword] = userAuth.hashPassword
                it[updatedAt] = userAuth.updatedAt
            }
        }
    }
    
    fun findById(id: String): UserAuth? {
        return transaction {
            UserAuths.select { UserAuths.id eq id }
                .map { rowToUserAuth(it) }
                .firstOrNull()
        }
    }

    fun findByEmail(email: String): UserAuth {
        return transaction {
            UserAuths.select { UserAuths.username eq email }
                .map { rowToUserAuth(it) }
                .firstOrNull()
        } ?: throw RuntimeException("User not found with email: $email")
    }
    
    private fun rowToUserAuth(row: ResultRow): UserAuth {
        return UserAuth(
            id = row[UserAuths.id],
            userId = row[UserAuths.userId],
            username = row[UserAuths.username],
            email = row[UserAuths.username],  // Mapping username to email
            hashPassword = row[UserAuths.hashPassword],
            createdAt = row[UserAuths.createdAt],
            updatedAt = row[UserAuths.updatedAt]
        )
    }
}
