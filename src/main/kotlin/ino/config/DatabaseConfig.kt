package ino.config

import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseConfig(
    @Value("\${spring.datasource.url}") private val url: String,
    @Value("\${spring.datasource.username}") private val username: String,
    @Value("\${spring.datasource.password}") private val password: String
) {
    
    @PostConstruct
    fun initDatabase() {
        Database.connect(
            url = url,
            driver = "org.postgresql.Driver",
            user = username,
            password = password
        )
    }
}