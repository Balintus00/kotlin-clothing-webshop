package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserService.Companion.ERROR_MESSAGE_UNEXPECTED_ERROR
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserService.Companion.ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserService.Companion.ERROR_MESSAGE_USER_INVALID_CREDENTIALS
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserService.Companion.ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.datasource.UserDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.AuthenticationToken
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.TokenID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID
import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal interface UserService {

    suspend fun login(email: Email, password: Password): AuthenticationToken

    suspend fun register(userCandidate: UserCandidate): AuthenticationToken

    suspend fun logout(tokenID: TokenID)

    suspend fun getUser(userId: UserID): User

    suspend fun updateUser(updatableUserCandidate: UpdatableUserCandidate): User

    suspend fun deleteUser(userID: UserID)

    suspend fun checkAuthenticationTokenIsBlackListed(tokenID: TokenID): Boolean

    companion object {
        const val JWT_VALID_DURATION_IN_DAYS = 7

        const val ERROR_MESSAGE_USER_INVALID_CREDENTIALS = "Invalid user credentials!"
        const val ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN = "Username already taken"
        const val ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN = "Email already taken"

        const val ERROR_MESSAGE_UNEXPECTED_ERROR = "Unexpected error"
    }
}

internal class DefaultUserService(private val datasource: UserDatasource) : UserService {

    override suspend fun login(email: Email, password: Password): AuthenticationToken {
        val userID = try {
            datasource.authenticateUser(
                email = email,
                password = password,
            )

        } catch (e: IllegalArgumentException) {
            if (
                e.message in setOf(
                    UserDatasource.ERROR_MESSAGE_USER_NOT_FOUND,
                    UserDatasource.ERROR_MESSAGE_USER_INVALID_PASSWORD,
                )
            ) {
                logger.info(e.message)
                throw IllegalArgumentException(ERROR_MESSAGE_USER_INVALID_CREDENTIALS)

            } else {
                e.handleUnexpectedCatch()
            }
        } catch (t: Throwable) {
            t.handleUnexpectedCatch()
        }

        return try {
            datasource.createAuthenticationToken(userID)
        } catch (t: Throwable) {
            t.handleUnexpectedCatch()
        }
    }

    private fun Throwable.handleUnexpectedCatch(): Nothing {
        logger.error("Unexpected exception: ${message}\n${stackTraceToString()}")
        throw IllegalStateException(ERROR_MESSAGE_UNEXPECTED_ERROR)
    }

    override suspend fun register(userCandidate: UserCandidate): AuthenticationToken {

        val userID = try {
            datasource.saveNewUser(userCandidate)
        } catch (e: IllegalArgumentException) {
            when (e.message) {
                UserDatasource.ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN -> {
                    logger.info(e.message)

                    throw IllegalArgumentException(ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN)
                }

                UserDatasource.ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN -> {
                    logger.info(e.message)

                    throw IllegalArgumentException(ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN)
                }

                else -> {
                    e.handleUnexpectedCatch()
                }
            }
        } catch (t: Throwable) {
            t.handleUnexpectedCatch()
        }

        return try {
            datasource.createAuthenticationToken(userID)
        } catch (t: Throwable) {
            t.handleUnexpectedCatch()
        }
    }

    override suspend fun logout(tokenID: TokenID) {
        try {
            datasource.invalidateAuthenticationToken(tokenID)
        } catch (t: Throwable) {
            t.handleUnexpectedCatch()
        }
    }

    override suspend fun getUser(userId: UserID): User = try {
        datasource.getUser(userId)
    } catch (e: IllegalArgumentException) {
        logger.error("User not found for data retrieval with ID: ${userId.value}")
        throw IllegalStateException(ERROR_MESSAGE_UNEXPECTED_ERROR)
    } catch (t: Throwable) {
        t.handleUnexpectedCatch()
    }

    override suspend fun updateUser(updatableUserCandidate: UpdatableUserCandidate): User =
        try {
            datasource.updateUser(updatableUserCandidate)
        } catch (e: IllegalArgumentException) {
            when (e.message) {
                UserDatasource.ERROR_MESSAGE_USER_NOT_FOUND -> {
                    logger.error("User not found for update with ID: ${updatableUserCandidate.id.value}\n${e.stackTraceToString()}")

                    throw IllegalStateException(ERROR_MESSAGE_UNEXPECTED_ERROR)
                }

                UserDatasource.ERROR_MESSAGE_USER_INVALID_PASSWORD -> {
                    logger.info(
                        "Invalid current password was given for user update operation for user ID: " +
                                updatableUserCandidate.id.value
                    )

                    throw IllegalArgumentException(ERROR_MESSAGE_USER_INVALID_CREDENTIALS)
                }

                UserDatasource.ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN -> {
                    logger.info(e.message)

                    throw IllegalArgumentException(ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN)
                }

                UserDatasource.ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN -> {
                    logger.info(e.message)

                    throw IllegalArgumentException(ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN)
                }

                else -> {
                    e.handleUnexpectedCatch()
                }
            }
        } catch (t: Throwable) {
            t.handleUnexpectedCatch()
        }

    override suspend fun deleteUser(userID: UserID) {
        try {
            datasource.deleteUser(userID)
        } catch (t: Throwable) {
            t.handleUnexpectedCatch()
        }
    }

    override suspend fun checkAuthenticationTokenIsBlackListed(tokenID: TokenID): Boolean =
        try {
            datasource.checkAuthenticationTokenIsBlacklisted(tokenID)
        } catch (e: IllegalArgumentException) {
            logger.error("Authentication token not found with ID: ${tokenID.value}")

            throw IllegalStateException(ERROR_MESSAGE_UNEXPECTED_ERROR)
        } catch (t: Throwable) {
            t.handleUnexpectedCatch()
        }

    companion object {
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(UserService::class.simpleName!!)
    }
}