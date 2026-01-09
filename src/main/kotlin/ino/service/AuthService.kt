package ino.service

import ino.dto.AuthResponse
import ino.dto.AuthResponseWithSession
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
    private val securityContextRepository: SecurityContextRepository
) {
    fun register(registerRequest: RegisterRequest, existingUserId: String? = null): AuthResponse {
        // Generate random password
        val randomPassword = generateRandomPassword()
        val userId = existingUserId ?: UUID.randomUUID().toString()
        val now = Instant.now().toEpochMilli()
        val hashedPassword = passwordEncoder.encode(randomPassword)

        // Create UserAuth record
        val userAuth = UserAuth(
            id = UUID.randomUUID().toString(),
            userId = userId,
            username = registerRequest.userName,
            hashPassword = hashedPassword,
            createdAt = now,
            updatedAt = now
        )
        authRepository.save(userAuth)

        return AuthResponse(
            userId = userId,
            userName = registerRequest.userName,
            password = randomPassword,
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
        try {
            val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.userName,
                    loginRequest.password
                )
            )

        // Get user details
        val user = authRepository.findByUserName(loginRequest.userName)

        // Create SecurityContext and set authentication
        val securityContext: SecurityContext = SecurityContextHolder.createEmptyContext()
        securityContext.authentication = authentication
        SecurityContextHolder.setContext(securityContext)


        // Save SecurityContext to session (REQUIRED for custom login endpoints)
        securityContextRepository.saveContext(securityContext, request, response)

        // Store custom data in session
        val session = request.getSession(false)
        session?.setAttribute("userId", user.userId)
        session?.setAttribute("userEmail", user.username)
        session?.setAttribute("loginTime", System.currentTimeMillis())
        // Add any other custom attributes you need

        return AuthResponseWithSession(
            userId = user.userId,
            userName = user.username,
            sessionId = session?.id!!,
        )
        }catch (e: Exception) {
            throw e;
        }
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
        return authRepository.findByUserName(authentication.name)
    }

    fun getSessionAttribute(request: HttpServletRequest, attributeName: String): Any? {
        val session = request.getSession(false)
        return session?.getAttribute(attributeName)
    }

    fun isUserExists(userName: String): Boolean {
        return authRepository.existsByUserName(userName)
    }
}
