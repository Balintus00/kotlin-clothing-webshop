package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.model

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID
import kotlin.jvm.JvmInline

data class AuthenticationToken(
    val id: TokenID,
    val ownerUserID: UserID,
)

@JvmInline
value class TokenID(val value: String)