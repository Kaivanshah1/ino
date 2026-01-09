package ino.controller

import ino.dto.CreateOrganizationRequest
import ino.dto.ListRequest
import ino.dto.UpdateOrganizationRequest
import ino.model.Organization
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
    ): ResponseEntity<Organization> {
        val response = organizationService.createOrganization(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/get/{id}")
    fun getOrganizationById(@PathVariable id: String): ResponseEntity<Organization> {
        val response = organizationService.getOrganizationById(id)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/list")
    fun getAllOrganizations(
        @RequestBody request: ListRequest
    ): ResponseEntity<List<Organization>> {
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
    ): ResponseEntity<Organization> {
        val response = organizationService.updateOrganization(request.id, request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/email/{email}")
    fun getOrganizationByEmail(@PathVariable email: String): ResponseEntity<Organization> {
        val organization = organizationService.getOrganizationByEmail(email)
        return ResponseEntity.ok(organization)
    }
}
