package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource

import com.microsoft.credentialstorage.StorageProvider
import com.microsoft.credentialstorage.model.StoredToken
import com.microsoft.credentialstorage.model.StoredTokenType
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserPersistentSecureDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.AuthenticationToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual class MultiplatformUserPersistentSecureDatasource(
    coroutineScope: CoroutineScope,
) : UserPersistentSecureDatasource {

    private val secretStore = StorageProvider.getTokenStorage(
        true,
        StorageProvider.SecureOption.REQUIRED,
    )

    private val _authenticationToken: MutableStateFlow<AuthenticationToken?> by lazy {
        MutableStateFlow(
            secretStore.get(AUTHENTICATION_TOKEN_PREFERENCES_KEY)?.value?.toString()?.let {
                AuthenticationToken(it)
            }
        )
    }

    override val authenticationToken: StateFlow<AuthenticationToken?>
        get() = _authenticationToken.asStateFlow()

    override val isAuthenticated: StateFlow<Boolean> = authenticationToken
        .map { it != null }
        .stateIn(
            initialValue = authenticationToken.value != null,
            scope = coroutineScope,
            started = SharingStarted.Lazily,
        )

    override suspend fun deleteAuthenticationToken() {
        withContext(Dispatchers.IO) {
            secretStore.delete(AUTHENTICATION_TOKEN_PREFERENCES_KEY)
        }

        _authenticationToken.update { null }
    }

    override suspend fun saveAuthenticationToken(token: AuthenticationToken) {
        withContext(Dispatchers.IO) {
            secretStore.add(
                AUTHENTICATION_TOKEN_PREFERENCES_KEY, StoredToken(
                    token.value.toCharArray(),
                    StoredTokenType.ACCESS
                )
            )
        }

        _authenticationToken.update { token }
    }

    companion object {
        private const val AUTHENTICATION_TOKEN_PREFERENCES_KEY = "auth_token"
    }
}