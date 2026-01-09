package ino.controller

import ino.dto.AuthResponse
import ino.dto.CreateUserRequest
import ino.dto.ListRequest
import ino.dto.UpdateUserRequest
import ino.model.User
import ino.service.AuthService
import ino.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/create")
    fun createUser(
        @RequestBody request: CreateUserRequest,
    ): ResponseEntity<AuthResponse> {
        val response = userService.createUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/get/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<User> {
        val response = userService.getUserById(id)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/list")
    fun getAllUsers(
        @RequestBody request: ListRequest
    ): ResponseEntity<List<User>> {
        val users = userService.getAllUsers(
            request.search,
            request.getAll,
            request.page,
            request.size
        )
        return ResponseEntity.ok(users)
    }

    @PostMapping("/update")
    fun updateUser(
        @RequestBody request: UpdateUserRequest
    ): ResponseEntity<User> {
        val response = userService.updateUser(request.id, request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<User> {
        val user = userService.getUserByEmail(email)
        return ResponseEntity.ok(user)
    }

    @GetMapping("/{organizationId}")
    fun getUsersByOrganizationId(
        @PathVariable organizationId: String
    ): ResponseEntity<List<User>> {
        val users = userService.getUsersByOrganizationId(organizationId)
        return ResponseEntity.ok(users)
    }

}
