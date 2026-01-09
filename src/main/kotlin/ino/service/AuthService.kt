package ino.service

import ino.dto.AuthResponse
import ino.dto.AuthResponseWithSession
import ino.dto.CreateUserRequest
import ino.dto.LoginRequest
import ino.dto.RegisterRequest
import ino.model.UserAuth
import ino.repository.AuthRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Instant
import java.util.UUID

@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val securityContextRepository: SecurityContextRepository,
    private val userService: UserService
) {
    fun register(registerRequest: RegisterRequest): AuthResponse {
        // Check if user already exists
        try {
            authRepository.findByEmail(registerRequest.email)
            throw RuntimeException("User with email ${registerRequest.email} already exists")
        } catch (e: Exception) {
            // User doesn't exist, proceed with registration
        }

        // Generate random password
        val randomPassword = generateRandomPassword()
        val userId = UUID.randomUUID().toString()
        val now = Instant.now().toEpochMilli()
        val hashedPassword = passwordEncoder.encode(randomPassword)

        // Create UserAuth record
        val userAuth = UserAuth(
            id = UUID.randomUUID().toString(),
            userId = userId,
            username = registerRequest.userName,
            email = registerRequest.email,
            hashPassword = hashedPassword,
            createdAt = now,
            updatedAt = now
        )
        authRepository.save(userAuth)

        // Create User record using UserService with the same userId
        val createUserRequest = CreateUserRequest(
            name = registerRequest.name,
            phoneNumber = registerRequest.phoneNumber,
            email = registerRequest.email,
            role = registerRequest.role,
            organizationId = registerRequest.organizationId,
            status = registerRequest.status
        )
        userService.createUser(createUserRequest, userId)

        return AuthResponse(
            userId = userId,
            email = userAuth.email,
            password = randomPassword,
            message = "Registration successful"
        )
    }

    private fun generateRandomPassword(length: Int = 12): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*"
        val random = SecureRandom()
        return (1..length)
            .map { chars[random.nextInt(chars.length)] }
            .joinToString("")
    }

    fun login(loginRequest: LoginRequest, request: HttpServletRequest, response: HttpServletResponse): AuthResponseWithSession {
        // Authenticate using Spring Security's AuthenticationManager
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.userName,
                loginRequest.password
            )
        )

        // Get user details
        val user = authRepository.findByEmail(loginRequest.userName)

        // Create SecurityContext and set authentication
        val securityContext: SecurityContext = SecurityContextHolder.createEmptyContext()
        securityContext.authentication = authentication
        SecurityContextHolder.setContext(securityContext)

        // Save SecurityContext to session (REQUIRED for custom login endpoints)
        securityContextRepository.saveContext(securityContext, request, response)

        // Store custom data in session
        val session = request.getSession(false)
        session?.setAttribute("userId", user.id)
        session?.setAttribute("userEmail", user.email)
        session?.setAttribute("loginTime", System.currentTimeMillis())
        // Add any other custom attributes you need

        return AuthResponseWithSession(
            userId = user.id,
            email = user.email,
            sessionId = session?.id!!,
            message = "Login successful"
        )

    }

    fun logout() {
        // Invalidate the current session
        SecurityContextHolder.clearContext()
    }

    fun getCurrentUser(): UserAuth {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null || !authentication.isAuthenticated) {
            throw RuntimeException("User not authenticated")
        }
        return authRepository.findByEmail(authentication.name)
    }

    fun getSessionAttribute(request: HttpServletRequest, attributeName: String): Any? {
        val session = request.getSession(false)
        return session?.getAttribute(attributeName)
    }
}
