package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
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
    context: Context,
    coroutineScope: CoroutineScope,
) : UserPersistentSecureDatasource {

    // TODO if backup rules will be defined, encrypted shared preferences must be excluded
    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFERENCES_FILE_NAME,
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    private val _authenticationToken by lazy {
        MutableStateFlow(
            encryptedSharedPreferences.getString(
                AUTHENTICATION_TOKEN_PREFERENCES_KEY,
                null
            )?.let { AuthenticationToken(it) }
        )
    }

    override val isAuthenticated: StateFlow<Boolean> = authenticationToken
        .map { it != null }
        .stateIn(
            initialValue = authenticationToken.value != null,
            scope = coroutineScope,
            started = SharingStarted.Lazily,
        )

    override val authenticationToken: StateFlow<AuthenticationToken?>
        get() = _authenticationToken.asStateFlow()

    override suspend fun deleteAuthenticationToken() {
        withContext(Dispatchers.IO) {
            encryptedSharedPreferences.edit().remove(AUTHENTICATION_TOKEN_PREFERENCES_KEY).apply()
        }

        _authenticationToken.update { null }
    }

    override suspend fun saveAuthenticationToken(token: AuthenticationToken) {
        withContext(Dispatchers.IO) {
            encryptedSharedPreferences
                .edit()
                .putString(AUTHENTICATION_TOKEN_PREFERENCES_KEY, token.value)
                .apply()
        }

        _authenticationToken.update { token }
    }

    companion object {

        private const val AUTHENTICATION_TOKEN_PREFERENCES_KEY = "auth_token"

        private const val PREFERENCES_FILE_NAME =
            "hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement." +
                    "MultiplatformUserPersistentSecureDatasource.PREFERENCES_FILE_KEY"
    }
}