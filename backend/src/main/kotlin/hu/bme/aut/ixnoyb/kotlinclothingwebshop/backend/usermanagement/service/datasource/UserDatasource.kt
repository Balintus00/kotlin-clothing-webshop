package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UpdatableUserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.model.AuthenticationToken
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.model.TokenID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID

interface UserDatasource {

    suspend fun authenticateUser(email: Email, password: Password): UserID

    suspend fun createAuthenticationToken(userID: UserID): AuthenticationToken

    suspend fun saveNewUser(userCandidate: UserCandidate): UserID

    suspend fun invalidateAuthenticationToken(tokenID: TokenID)

    suspend fun getUser(userID: UserID): User

    suspend fun updateUser(updatableUserCandidate: UpdatableUserCandidate): User

    suspend fun deleteUser(password: Password, userID: UserID)

    suspend fun checkAuthenticationTokenIsBlacklisted(tokenID: TokenID): Boolean

    suspend fun getUserRecommendationIndexAndBirthDate(userID: UserID?): Pair<Int, DateOfBirth>

    companion object {

        const val ERROR_MESSAGE_USER_NOT_FOUND = "User not found!"
        const val ERROR_MESSAGE_USER_INVALID_PASSWORD = "Given password was invalid!"
        const val ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN = "Username already taken!"
        const val ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN = "Email already taken!"

        const val ERROR_MESSAGE_TOKEN_NOT_FOUND = "Authentication token with ID not found!"
    }
}