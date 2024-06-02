package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.User
import kotlinx.coroutines.flow.StateFlow

internal interface UserPersistentDatasource {

    val user: StateFlow<User?>

    suspend fun clear()

    suspend fun saveUser(user: User)
}