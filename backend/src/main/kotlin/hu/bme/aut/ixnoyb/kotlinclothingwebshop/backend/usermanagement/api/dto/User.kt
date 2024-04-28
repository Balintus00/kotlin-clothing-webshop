package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
)

@Serializable
data class AuthenticatedUser(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
)