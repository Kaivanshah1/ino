package ino.controller

import ino.dto.CreateOrganizationRequest
import ino.dto.FindAllOrganizationsRequest
import ino.dto.OrganizationResponse
import ino.dto.UpdateOrganizationRequest
import ino.service.OrganizationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/organizations")
class OrganizationController(
    private val organizationService: OrganizationService
) {
    @PostMapping("/create")
    fun createOrganization(
        @RequestBody request: CreateOrganizationRequest
    ): ResponseEntity<OrganizationResponse> {
        val response = organizationService.createOrganization(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/get/{id}")
    fun getOrganizationById(@PathVariable id: String): ResponseEntity<OrganizationResponse> {
        return try {
            val response = organizationService.getOrganizationById(id)
            ResponseEntity.ok(response)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(OrganizationResponse("", null, null, null, null, 0, 0))
        }
    }

    @PostMapping("/list")
    fun getAllOrganizations(
        @RequestBody request: FindAllOrganizationsRequest
    ): ResponseEntity<List<OrganizationResponse>> {
        val organizations = organizationService.listOrganizations(
            request.search,
            request.getAll,
            request.page,
            request.size
        )
        return ResponseEntity.ok(organizations)
    }

    @PostMapping("/update")
    fun updateOrganization(
        @RequestBody request: UpdateOrganizationRequest
    ): ResponseEntity<OrganizationResponse> {
        return try {
            val response = organizationService.updateOrganization(request.id, request)
            ResponseEntity.ok(response)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(OrganizationResponse("", null, null, null, null, 0, 0))
        }
    }

    @GetMapping("/email/{email}")
    fun getOrganizationByEmail(@PathVariable email: String): ResponseEntity<OrganizationResponse> {
        val organization = organizationService.getOrganizationByEmail(email)
        return if (organization != null) {
            ResponseEntity.ok(organization)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(OrganizationResponse("", null, null, null, null, 0, 0))
        }
    }
}
