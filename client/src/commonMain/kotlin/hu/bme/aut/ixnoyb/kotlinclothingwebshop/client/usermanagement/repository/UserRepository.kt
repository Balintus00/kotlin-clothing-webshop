package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserPersistentDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserPersistentSecureDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserRemoteDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.UpdatedAccount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.UserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

internal interface UserRepository {

    val isAuthenticated: StateFlow<Boolean>

    val user: StateFlow<User?>

    suspend fun deleteAccount(password: Password)

    suspend fun loadAccount()

    suspend fun logIn(email: Email, password: Password)

    suspend fun logOut()

    suspend fun register(newUser: UserCandidate)

    suspend fun updateAccount(currentPassword: Password, updatedAccount: UpdatedAccount)

    companion object {
        const val ERROR_MESSAGE_EMAIL_ALREADY_TAKEN = "ERROR_MESSAGE_EMAIL_ALREADY_TAKEN"
        const val ERROR_MESSAGE_INVALID_CREDENTIALS = "ERROR_MESSAGE_INVALID_CREDENTIALS"
        const val ERROR_MESSAGE_GENERAL_ERROR = "ERROR_MESSAGE_GENERAL_ERROR"
        const val ERROR_MESSAGE_USERNAME_ALREADY_TAKEN = "ERROR_MESSAGE_USERNAME_ALREADY_TAKEN"
    }
}

internal class DefaultUserRepository(
    private val userPersistentDatasource: UserPersistentDatasource,
    private val userRemoteDatasource: UserRemoteDatasource,
    private val userSecurePersistentDatasource: UserPersistentSecureDatasource,
) : UserRepository {

    override val user: StateFlow<User?> = userPersistentDatasource.user

    override val isAuthenticated: StateFlow<Boolean> =
        userSecurePersistentDatasource.isAuthenticated

    override suspend fun deleteAccount(password: Password) {
        try {
            userRemoteDatasource.deleteAccount(
                userSecurePersistentDatasource.authenticationToken.value,
                password,
            )

            userSecurePersistentDatasource.deleteAuthenticationToken()
            userPersistentDatasource.clear()
        } catch (t: Throwable) {
            if (t.message == UserRemoteDatasource.ERROR_MESSAGE_INVALID_CREDENTIALS) {
                throw IllegalStateException(
                    UserRepository.ERROR_MESSAGE_INVALID_CREDENTIALS
                )
            } else {
                throw IllegalStateException(UserRepository.ERROR_MESSAGE_GENERAL_ERROR)
            }
        }
    }

    override suspend fun loadAccount() {
        withContext(Dispatchers.Default) {
            userPersistentDatasource.saveUser(
                userRemoteDatasource.getAccount(
                    userSecurePersistentDatasource.authenticationToken.value
                )
            )
        }
    }

    override suspend fun logIn(email: Email, password: Password) {
        withContext(Dispatchers.Default) {
            try {
                val authenticationToken = userRemoteDatasource.logIn(email, password)

                authenticationToken?.let {
                    userSecurePersistentDatasource.saveAuthenticationToken(it)
                }
            } catch (t: Throwable) {
                if (t.message == UserRemoteDatasource.ERROR_MESSAGE_INVALID_CREDENTIALS) {
                    throw IllegalStateException(
                        UserRepository.ERROR_MESSAGE_INVALID_CREDENTIALS
                    )
                } else {
                    throw IllegalStateException(UserRepository.ERROR_MESSAGE_GENERAL_ERROR)
                }
            }
        }
    }

    override suspend fun logOut() {
        withContext(Dispatchers.Default) {
            userRemoteDatasource.logOut(userSecurePersistentDatasource.authenticationToken.value)

            userSecurePersistentDatasource.deleteAuthenticationToken()
            userPersistentDatasource.clear()
        }
    }

    override suspend fun register(newUser: UserCandidate) {
        withContext(Dispatchers.Default) {
            try {
                val authenticationToken = userRemoteDatasource.register(newUser)

                authenticationToken?.let {
                    userSecurePersistentDatasource.saveAuthenticationToken(it)
                }
            } catch (t: Throwable) {
                when (t.message) {
                    UserRemoteDatasource.ERROR_MESSAGE_EMAIL_IS_ALREADY_TAKEN -> {
                        throw IllegalStateException(
                            UserRepository.ERROR_MESSAGE_EMAIL_ALREADY_TAKEN
                        )
                    }

                    UserRemoteDatasource.ERROR_MESSAGE_USERNAME_IS_ALREADY_TAKEN -> {
                        throw IllegalStateException(
                            UserRepository.ERROR_MESSAGE_USERNAME_ALREADY_TAKEN
                        )
                    }

                    else -> throw IllegalStateException(UserRepository.ERROR_MESSAGE_GENERAL_ERROR)
                }
            }
        }
    }

    override suspend fun updateAccount(currentPassword: Password, updatedAccount: UpdatedAccount) {
        withContext(Dispatchers.Default) {
            val updatedUser = userRemoteDatasource.updateAccount(
                authenticationToken = userSecurePersistentDatasource.authenticationToken.value,
                currentPassword = currentPassword,
                updatedUser = updatedAccount,
            )

            userPersistentDatasource.saveUser(updatedUser)
        }
    }
}