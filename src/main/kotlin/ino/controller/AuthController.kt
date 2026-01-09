package ino.controller

import ino.dto.AuthResponse
import ino.dto.AuthResponseWithSession
import ino.dto.LoginRequest
import ino.dto.RegisterRequest
import ino.service.AuthService
import ino.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
    private val userService: UserService
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

    @GetMapping("/get-session")
    fun getSessionInfo(request: HttpServletRequest): ResponseEntity<out Map<String, Any?>> {
        val session = request.getSession(false)
        val userId = session?.let { authService.getSessionAttribute(request, "userId") as? String }

        return if (session != null && userId != null) {
            val user = userService.getUserById(userId)
            ResponseEntity.ok(mapOf(
                "sessionId" to session.id,
                "user" to user
            ))
        } else {
            ResponseEntity.ok(null)
        }
    }
}
