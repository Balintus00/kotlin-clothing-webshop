package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class JwtResponse(val jwt: String)