package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.AuthenticationToken
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.UpdatedAccount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.UserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password

internal interface UserRemoteDatasource {

    suspend fun deleteAccount(authenticationToken: AuthenticationToken?, password: Password)

    suspend fun getAccount(token: AuthenticationToken?): User

    suspend fun logIn(email: Email, password: Password): AuthenticationToken?

    suspend fun logOut(token: AuthenticationToken?)

    suspend fun register(newUser: UserCandidate): AuthenticationToken?

    suspend fun updateAccount(
        authenticationToken: AuthenticationToken?,
        currentPassword: Password,
        updatedUser: UpdatedAccount,
    ): User

    companion object {

        const val ERROR_MESSAGE_EMAIL_IS_ALREADY_TAKEN = "Email is already taken"
        const val ERROR_MESSAGE_INVALID_CREDENTIALS = "Invalid credentials"
        const val ERROR_MESSAGE_NETWORK_ERROR = "Network error"
        const val ERROR_MESSAGE_USERNAME_IS_ALREADY_TAKEN = "Username is already taken"
    }
}