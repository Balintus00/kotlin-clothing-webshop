package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserPersistentSecureDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.AuthenticationToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual class MultiplatformUserPersistentSecureDatasource(
    coroutineScope: CoroutineScope,
) : UserPersistentSecureDatasource {

    @OptIn(ExperimentalSettingsImplementation::class)
    private val settings: Settings = KeychainSettings()

    private val _authenticationToken: MutableStateFlow<AuthenticationToken?> by lazy {
        MutableStateFlow(
            settings.getStringOrNull(AUTHENTICATION_TOKEN_PREFERENCES_KEY)?.let {
                AuthenticationToken(it)
            }
        )
    }

    override val authenticationToken: StateFlow<AuthenticationToken?> = _authenticationToken

    override val isAuthenticated: StateFlow<Boolean> = authenticationToken
        .map { it != null }
        .stateIn(
            initialValue = authenticationToken.value != null,
            scope = coroutineScope,
            started = SharingStarted.Lazily,
        )

    override suspend fun deleteAuthenticationToken() {
        withContext(Dispatchers.IO) {
            settings.remove(AUTHENTICATION_TOKEN_PREFERENCES_KEY)
        }

        _authenticationToken.update { null }
    }

    override suspend fun saveAuthenticationToken(token: AuthenticationToken) {
        withContext(Dispatchers.IO) {
            settings.putString(AUTHENTICATION_TOKEN_PREFERENCES_KEY, token.value)
        }

        _authenticationToken.update { token }
    }

    companion object {

        private const val AUTHENTICATION_TOKEN_PREFERENCES_KEY = "auth_token"
    }
}