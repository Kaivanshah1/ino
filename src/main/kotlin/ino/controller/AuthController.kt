package ino.controller

import ino.dto.AuthResponse
import ino.dto.AuthResponseWithSession
import ino.dto.LoginRequest
import ino.dto.RegisterRequest
import ino.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<AuthResponse> {
        val response = authService.register(registerRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponseWithSession> {
        val authResponse = authService.login(loginRequest, request, response)
        return ResponseEntity.ok(authResponse)
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Map<String, String>> {
        authService.logout()
        // Invalidate the session
        val session: HttpSession? = request.getSession(false)
        session?.invalidate()
        return ResponseEntity.ok(mapOf("message" to "Logout successful"))
    }

    @GetMapping("/me")
    fun getCurrentUser(): ResponseEntity<AuthResponse> {
        return try {
            val user = authService.getCurrentUser()
            ResponseEntity.ok(
                AuthResponse(
                    userId = user.id,
                    email = user.email,
                    message = "Current user information"
                )
            )
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AuthResponse("", "", "Not authenticated"))
        }
    }

    @GetMapping("/session-info")
    fun getSessionInfo(request: HttpServletRequest): ResponseEntity<Map<String, Any?>> {
        val session = request.getSession(false)
        return if (session != null) {
            ResponseEntity.ok(mapOf(
                "userId" to authService.getSessionAttribute(request, "userId"),
                "userEmail" to authService.getSessionAttribute(request, "userEmail"),
                "loginTime" to authService.getSessionAttribute(request, "loginTime"),
                "sessionId" to session.id
            ))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapOf("error" to "No active session"))
        }
    }
}
