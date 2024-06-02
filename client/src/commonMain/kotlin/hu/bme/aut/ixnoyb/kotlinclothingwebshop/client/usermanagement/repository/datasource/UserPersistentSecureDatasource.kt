package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.AuthenticationToken
import kotlinx.coroutines.flow.StateFlow

internal interface UserPersistentSecureDatasource {

    val authenticationToken: StateFlow<AuthenticationToken?>

    val isAuthenticated: StateFlow<Boolean>

    suspend fun deleteAuthenticationToken()

    suspend fun saveAuthenticationToken(token: AuthenticationToken)
}