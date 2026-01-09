package ino.service

import ino.dto.CreateOrganizationRequest
import ino.dto.OrganizationResponse
import ino.dto.UpdateOrganizationRequest
import ino.repository.Organization
import ino.repository.OrganizationRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository
) {
    fun createOrganization(request: CreateOrganizationRequest): OrganizationResponse {
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
        return toResponse(organization)
    }

    fun getOrganizationById(id: String): OrganizationResponse {
        val organization = organizationRepository.findById(id)
            ?: throw RuntimeException("Organization with id $id not found")
        return toResponse(organization)
    }

    fun listOrganizations(
        search: String?,
        getAll: Boolean,
        page: Int,
        size: Int
    ): List<OrganizationResponse> {
        return organizationRepository.listOrganizations(search, getAll, page, size).map { toResponse(it) }
    }

    fun updateOrganization(id: String, request: UpdateOrganizationRequest): OrganizationResponse {
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
        return toResponse(updated)
    }

    fun getOrganizationByEmail(email: String): OrganizationResponse? {
        val organization = organizationRepository.findByEmail(email)
        return organization?.let { toResponse(it) }
    }

    private fun toResponse(organization: Organization): OrganizationResponse {
        return OrganizationResponse(
            id = organization.id,
            name = organization.name,
            phoneNumber = organization.phoneNumber,
            email = organization.email,
            status = organization.status,
            createdAt = organization.createdAt,
            updatedAt = organization.updatedAt
        )
    }
}
