package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource

import com.russhwolf.settings.Settings
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.coroutines.multiplatformIODispatcher
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.serialization.toSerializableUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserPersistentDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class MultiplatformSettingsUserPersistentDatasource(
    private val settings: Settings,
) : UserPersistentDatasource {

    private val _user: MutableStateFlow<User?> by lazy {
        MutableStateFlow(
            settings.getStringOrNull(SETTINGS_KEY_USER)?.let { Json.decodeFromString(it) }
        )
    }

    override val user: StateFlow<User?> = _user.asStateFlow()

    override suspend fun clear() {
        withContext(multiplatformIODispatcher) {
            settings.remove(SETTINGS_KEY_USER)
        }

        _user.update { null }
    }

    override suspend fun saveUser(user: User) {
        val deserializedUser = Json.encodeToString(user.toSerializableUser())

        withContext(multiplatformIODispatcher) {
            settings.putString(SETTINGS_KEY_USER, deserializedUser)
        }

        _user.update { user }
    }

    companion object {
        private const val SETTINGS_KEY_USER = "user"
    }
}