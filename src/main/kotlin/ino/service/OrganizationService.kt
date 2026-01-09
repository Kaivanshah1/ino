package ino.service

import ino.dto.CreateOrganizationRequest
import ino.dto.UpdateOrganizationRequest
import ino.model.Organization
import ino.repository.OrganizationRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository
) {
    fun createOrganization(request: CreateOrganizationRequest): Organization {
        val organizationId = UUID.randomUUID().toString()
        val now = Instant.now().toEpochMilli()

        val organization = Organization(
            id = organizationId,
            name = request.name,
            phoneNumber = request.phoneNumber,
            email = request.email,
            status = request.status,
            createdAt = now,
            updatedAt = now
        )

        organizationRepository.save(organization)
        return organization
    }

    fun getOrganizationById(id: String): Organization {
        val organization = organizationRepository.findById(id)
            ?: throw RuntimeException("Organization with id $id not found")
        return organization
    }

    fun listOrganizations(
        search: String?,
        getAll: Boolean,
        page: Int,
        size: Int
    ): List<Organization> {
        return organizationRepository.listOrganizations(search, getAll, page, size)
    }

    fun updateOrganization(id: String, request: UpdateOrganizationRequest): Organization {
        val existing = organizationRepository.findById(id)
            ?: throw RuntimeException("Organization with id $id not found")

        val updated = Organization(
            id = existing.id,
            name = request.name ?: existing.name,
            phoneNumber = request.phoneNumber ?: existing.phoneNumber,
            email = request.email ?: existing.email,
            status = request.status ?: existing.status,
            createdAt = existing.createdAt,
            updatedAt = Instant.now().toEpochMilli()
        )

        organizationRepository.update(updated)
        return updated
    }

    fun getOrganizationByEmail(email: String): Organization? {
        val organization = organizationRepository.findByEmail(email)
        return organization
    }
}
