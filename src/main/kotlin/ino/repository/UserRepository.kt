package ino.repository

import ino.model.User
import ino.model.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class UserRepository {
    
    fun save(user: User) {
        transaction {
            Users.insert {
                it[id] = user.id
                it[name] = user.name
                it[phoneNumber] = user.phoneNumber
                it[email] = user.email
                it[role] = user.role
                it[organizationId] = user.organizationId
                it[status] = user.status
                it[createdAt] = user.createdAt
                it[updatedAt] = user.updatedAt
            }
        }
    }
    
    fun update(user: User) {
        transaction {
            Users.update({ Users.id eq user.id }) {
                it[name] = user.name
                it[phoneNumber] = user.phoneNumber
                it[email] = user.email
                it[role] = user.role
                it[organizationId] = user.organizationId
                it[status] = user.status
                it[updatedAt] = user.updatedAt
            }
        }
    }
    
    fun findById(id: String): User? {
        return transaction {
            Users.select { Users.id eq id }
                .map { rowToUser(it) }
                .firstOrNull()
        }
    }
    
    fun findAll(): List<User> {
        return transaction {
            Users.selectAll()
                .map { rowToUser(it) }
        }
    }
    
    fun findAll(search: String?, getAll: Boolean, page: Int, size: Int): List<User> {
        return transaction {
            val searchPattern = if (!search.isNullOrBlank()) "%$search%" else null
            
            val query = if (searchPattern != null) {
                Users.select {
                    (Users.name like searchPattern) or
                    (Users.email like searchPattern) or
                    (Users.phoneNumber like searchPattern) or
                    (Users.status like searchPattern) or
                    (Users.role like searchPattern) or
                    (Users.organizationId like searchPattern)
                }
            } else {
                Users.selectAll()
            }
            
            // Apply pagination only if getAll is false
            val finalQuery = if (!getAll) {
                val offset = page * size
                query.limit(size, offset = offset.toLong())
            } else {
                query
            }
            
            finalQuery.map { rowToUser(it) }
        }
    }
    
    fun findByEmail(email: String): User? {
        return transaction {
            Users.select { Users.email eq email }
                .map { rowToUser(it) }
                .firstOrNull()
        }
    }
    
    fun findByOrganizationId(organizationId: String): List<User> {
        return transaction {
            Users.select { Users.organizationId eq organizationId }
                .map { rowToUser(it) }
        }
    }

    private fun rowToUser(row: ResultRow): User {
        return User(
            id = row[Users.id],
            name = row[Users.name],
            role = row[Users.role],
            phoneNumber = row[Users.phoneNumber],
            email = row[Users.email],
            organizationId = row[Users.organizationId],
            status = row[Users.status],
            createdAt = row[Users.createdAt],
            updatedAt = row[Users.updatedAt]
        )
    }
}