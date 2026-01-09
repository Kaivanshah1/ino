package ino.controller

import ino.dto.CreateUserRequest
import ino.dto.FindAllUsersRequest
import ino.dto.UpdateUserRequest
import ino.dto.UserResponse
import ino.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

//    @PostMapping("/create")
//    fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
//        val response = userService.createUser(request)
//        return ResponseEntity.status(HttpStatus.CREATED).body(response)
//    }

    @GetMapping("/get/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<UserResponse> {
        return try {
            val response = userService.getUserById(id)
            ResponseEntity.ok(response)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(UserResponse("", null, null, null, null, null, 0, 0))
        }
    }

    @PostMapping("/list")
    fun getAllUsers(
        @RequestBody request: FindAllUsersRequest
    ): ResponseEntity<List<UserResponse>> {
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
    ): ResponseEntity<UserResponse> {
        return try {
            val response = userService.updateUser(request.id, request)
            ResponseEntity.ok(response)
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(UserResponse("", null, null, null, null, null, 0, 0))
        }
    }

    @GetMapping("/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<UserResponse> {
        val user = userService.getUserByEmail(email)
        return if (user != null) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(UserResponse("", null, null, null, null, null, 0, 0))
        }
    }

    @GetMapping("/{organizationId}")
    fun getUsersByOrganizationId(
        @PathVariable organizationId: String
    ): ResponseEntity<List<UserResponse>> {
        val users = userService.getUsersByOrganizationId(organizationId)
        return ResponseEntity.ok(users)
    }
}
