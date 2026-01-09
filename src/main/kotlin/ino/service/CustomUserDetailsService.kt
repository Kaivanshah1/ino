package ino.service

import ino.repository.AuthRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val authRepository: AuthRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userAuth = try {
            authRepository.findByUserName(username)
        } catch (e: Exception) {
            throw UsernameNotFoundException("User not found with username: $username", e)
        }

        val authorities: List<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))

        return User.builder()
            .username(userAuth.username)
            .password(userAuth.hashPassword ?: "")
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .build()
    }

}
