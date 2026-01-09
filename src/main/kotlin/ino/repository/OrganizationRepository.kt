package ino.repository

import ino.model.Organizations
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

data class Organization(
    val id: String,
    val name: String?,
    val phoneNumber: String?,
    val email: String?,
    val status: String?,
    val createdAt: Long,
    val updatedAt: Long
)

@Repository
class OrganizationRepository {
    
    fun save(organization: Organization) {
        transaction {
            Organizations.insert {
                it[id] = organization.id
                it[name] = organization.name
                it[phoneNumber] = organization.phoneNumber
                it[email] = organization.email
                it[status] = organization.status
                it[createdAt] = organization.createdAt
                it[updatedAt] = organization.updatedAt
            }
        }
    }
    
    fun update(organization: Organization) {
        transaction {
            Organizations.update({ Organizations.id eq organization.id }) {
                it[name] = organization.name
                it[phoneNumber] = organization.phoneNumber
                it[email] = organization.email
                it[status] = organization.status
                it[updatedAt] = organization.updatedAt
            }
        }
    }
    
    fun findById(id: String): Organization? {
        return transaction {
            Organizations.select { Organizations.id eq id }
                .map { rowToOrganization(it) }
                .firstOrNull()
        }
    }
    
    fun findAll(): List<Organization> {
        return transaction {
            Organizations.selectAll()
                .map { rowToOrganization(it) }
        }
    }
    
    fun listOrganizations(search: String?, getAll: Boolean, page: Int, size: Int): List<Organization> {
        return transaction {
            val searchPattern = if (!search.isNullOrBlank()) "%$search%" else null
            
            val query = if (searchPattern != null) {
                Organizations.select {
                    (Organizations.name like searchPattern) or
                    (Organizations.email like searchPattern) or
                    (Organizations.phoneNumber like searchPattern) or
                    (Organizations.status like searchPattern)
                }
            } else {
                Organizations.selectAll()
            }
            
            // Apply pagination only if getAll is false
            val finalQuery = if (!getAll) {
                val offset = page * size
                query.limit(size, offset = offset.toLong())
            } else {
                query
            }
            
            finalQuery.map { rowToOrganization(it) }
        }
    }
    
    fun findByEmail(email: String): Organization? {
        return transaction {
            Organizations.select { Organizations.email eq email }
                .map { rowToOrganization(it) }
                .firstOrNull()
        }
    }
    
    private fun rowToOrganization(row: ResultRow): Organization {
        return Organization(
            id = row[Organizations.id],
            name = row[Organizations.name],
            phoneNumber = row[Organizations.phoneNumber],
            email = row[Organizations.email],
            status = row[Organizations.status],
            createdAt = row[Organizations.createdAt],
            updatedAt = row[Organizations.updatedAt]
        )
    }
}