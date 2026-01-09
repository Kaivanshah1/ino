package ino.service

import ino.dto.CreateUserRequest
import ino.dto.UpdateUserRequest
import ino.dto.UserResponse
import ino.model.User
import ino.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun createUser(request: CreateUserRequest, userId: String? = null): UserResponse {
        val finalUserId = userId ?: UUID.randomUUID().toString()
        val now = Instant.now().toEpochMilli()

        val user = User(
            id = finalUserId,
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
        return toResponse(user)
    }

    fun getUserById(id: String): UserResponse {
        val user = userRepository.findById(id)
            ?: throw RuntimeException("User with id $id not found")
        return toResponse(user)
    }

    fun getAllUsers(): List<UserResponse> {
        return userRepository.findAll().map { toResponse(it) }
    }

    fun getAllUsers(
        search: String?,
        getAll: Boolean,
        page: Int,
        size: Int
    ): List<UserResponse> {
        return userRepository.findAll(search, getAll, page, size).map { toResponse(it) }
    }

    fun updateUser(id: String, request: UpdateUserRequest): UserResponse {
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
        return toResponse(updated)
    }

    fun getUserByEmail(email: String): UserResponse? {
        val user = userRepository.findByEmail(email)
        return user?.let { toResponse(it) }
    }

    fun getUsersByOrganizationId(organizationId: String): List<UserResponse> {
        return userRepository.findByOrganizationId(organizationId).map { toResponse(it) }
    }

    private fun toResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            name = user.name,
            phoneNumber = user.phoneNumber,
            email = user.email,
            organizationId = user.organizationId,
            status = user.status,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}
