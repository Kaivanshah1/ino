package ino.service

import ino.dto.AuthResponse
import ino.dto.CreateUserRequest
import ino.dto.RegisterRequest
import ino.dto.UpdateUserRequest
import ino.model.User
import ino.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val authService: AuthService
) {
    fun createUser(
        request: CreateUserRequest,
    ): AuthResponse {
        try{
            if (request.email != null && userRepository.findByEmail(request.email) != null) {
                throw RuntimeException("User with email ${request.email} already exists")
            }
            if (authService.isUserExists(request.userName)) {
                throw RuntimeException("User with username ${request.userName} already exists")
            }
        }catch (e: Exception){
            throw e
        }

        val userId = UUID.randomUUID().toString()
        val now = Instant.now().toEpochMilli()

        val user = User(
            id = userId,
            name = request.name,
            phoneNumber = request.phoneNumber,
            email = request.email,
            organizationId = request.organizationId,
            status = request.status,
            role = request.role,
            createdAt = now,
            updatedAt = now
        )

        userRepository.save(user)

        val email = request.email ?: throw RuntimeException("Email is required for user registration")

        val registerRequest = RegisterRequest(
            email = email,
            name = request.name,
            userName = request.userName,
            phoneNumber = request.phoneNumber,
            role = request.role,
            organizationId = request.organizationId,
            status = request.status
        )

        val authResponse = authService.register(registerRequest, userId)

        return authResponse
    }

    fun getUserById(id: String): User {
        val user = userRepository.findById(id)
            ?: throw RuntimeException("User with id $id not found")
        return user
    }

    fun getAllUsers(
        search: String?,
        getAll: Boolean,
        page: Int,
        size: Int
    ): List<User> {
        return userRepository.findAll(search, getAll, page, size)
    }

    fun updateUser(id: String, request: UpdateUserRequest): User {
        val existing = userRepository.findById(id)
            ?: throw RuntimeException("User with id $id not found")

        val updated = User(
            id = existing.id,
            name = request.name,
            phoneNumber = request.phoneNumber,
            email = request.email ?: existing.email,
            role = request.role,
            organizationId = request.organizationId,
            status = request.status,
            createdAt = existing.createdAt,
            updatedAt = Instant.now().toEpochMilli()
        )

        userRepository.update(updated)
        return updated
    }

    fun getUserByEmail(email: String): User? {
        val user = userRepository.findByEmail(email)
        return user
    }

    fun getUsersByOrganizationId(organizationId: String): List<User> {
        return userRepository.findByOrganizationId(organizationId)
    }
}
