package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UpdatableUserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.AuthenticationToken
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.TokenID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID

internal interface UserDatasource {

    suspend fun authenticateUser(email: Email, password: Password): UserID

    suspend fun createAuthenticationToken(userID: UserID): AuthenticationToken

    suspend fun saveNewUser(userCandidate: UserCandidate): UserID

    suspend fun invalidateAuthenticationToken(tokenID: TokenID)

    suspend fun getUser(userID: UserID): User

    suspend fun updateUser(updatableUserCandidate: UpdatableUserCandidate): User

    suspend fun deleteUser(userID: UserID)

    suspend fun checkAuthenticationTokenIsBlacklisted(tokenID: TokenID): Boolean

    companion object {

        const val ERROR_MESSAGE_USER_NOT_FOUND = "User not found!"
        const val ERROR_MESSAGE_USER_INVALID_PASSWORD = "Given password was invalid!"
        const val ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN = "Username already taken!"
        const val ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN = "Email already taken!"

        const val ERROR_MESSAGE_TOKEN_NOT_FOUND = "Authentication token with ID not found!"
    }
}