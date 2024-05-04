package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.entity

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.AuthenticationToken as DomainAuthenticationToken
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperTable

@KomapperEntity
@KomapperTable("authentication_tokens")
data class AuthenticationToken(@KomapperId val id: String, val userID: String, val isActive: Boolean)

fun DomainAuthenticationToken.toKomapperEntity(isActive: Boolean): AuthenticationToken = AuthenticationToken(
    id = id.value,
    userID = ownerUserID.value,
    isActive = isActive,
)