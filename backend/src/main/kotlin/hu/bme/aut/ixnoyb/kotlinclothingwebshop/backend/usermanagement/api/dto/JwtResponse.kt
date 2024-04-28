package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class JwtResponse(val jwt: String)