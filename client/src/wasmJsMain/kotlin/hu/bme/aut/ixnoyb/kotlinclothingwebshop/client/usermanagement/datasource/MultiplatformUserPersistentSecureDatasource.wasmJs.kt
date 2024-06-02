package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserPersistentSecureDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.AuthenticationToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Dummy implementation, because on wasmJS client the authentication token is stored as an
// HTTP-Only Cookie
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual class MultiplatformUserPersistentSecureDatasource : UserPersistentSecureDatasource {

    private val settings: Settings = StorageSettings()

    override val authenticationToken: StateFlow<AuthenticationToken?> = MutableStateFlow(null)

    private val _isAuthenticated: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(settings.getBooleanOrNull(IS_AUTHENTICATED_STORAGE_KEY) ?: false)
    }

    override val isAuthenticated: StateFlow<Boolean> by lazy { _isAuthenticated.asStateFlow() }

    override suspend fun deleteAuthenticationToken() {
        settings.putBoolean(IS_AUTHENTICATED_STORAGE_KEY, false)

        _isAuthenticated.update { false }
    }

    override suspend fun saveAuthenticationToken(token: AuthenticationToken) {
        settings.putBoolean(IS_AUTHENTICATED_STORAGE_KEY, true)

        _isAuthenticated.update { true }
    }

    companion object {
        private const val IS_AUTHENTICATED_STORAGE_KEY = "IS_AUTHENTICATED_STORAGE_KEY"
    }
}