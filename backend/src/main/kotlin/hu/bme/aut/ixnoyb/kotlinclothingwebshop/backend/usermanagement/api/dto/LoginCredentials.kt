package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(val email: String, val password: String)