package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(val email: String, val password: String)