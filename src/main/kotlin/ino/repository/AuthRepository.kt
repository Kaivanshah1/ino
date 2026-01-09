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
                    it[userId] = userAuth.userId
                    it[username] = userAuth.username  // Mapping email to username
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
                it[username] = userAuth.username
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

    fun findByUserName(userName: String): UserAuth {
        return transaction {
            UserAuths.select { UserAuths.username eq userName }
                .map { rowToUserAuth(it) }
                .firstOrNull()
        } ?: throw RuntimeException("User not found with email: $userName")
    }

    fun existsByUserName(userName: String): Boolean {
        return transaction {
            UserAuths.select { UserAuths.username eq userName }.count() > 0
        }
    }

    private fun rowToUserAuth(row: ResultRow): UserAuth {
        return UserAuth(
            id = row[UserAuths.id],
            userId = row[UserAuths.userId],
            username = row[UserAuths.username],
            hashPassword = row[UserAuths.hashPassword],
            createdAt = row[UserAuths.createdAt],
            updatedAt = row[UserAuths.updatedAt]
        )
    }
}
