package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource

import co.touchlab.kermit.Logger
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.datasource.getServerBaseUrl
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.dto.JwtResponse
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.dto.LoginCredentials
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.dto.toAuthenticatedUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.dto.toDomainUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.dto.toDto
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserRemoteDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserRemoteDatasource.Companion.ERROR_MESSAGE_EMAIL_IS_ALREADY_TAKEN
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserRemoteDatasource.Companion.ERROR_MESSAGE_INVALID_CREDENTIALS
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserRemoteDatasource.Companion.ERROR_MESSAGE_NETWORK_ERROR
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserRemoteDatasource.Companion.ERROR_MESSAGE_USERNAME_IS_ALREADY_TAKEN
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.AuthenticationToken
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.UpdatedAccount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.UserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.dto.User as UserDto

internal expect val isAuthenticationTokenRetrievalSupported: Boolean

internal class KtorUserRemoteDatasource(private val httpClient: HttpClient) : UserRemoteDatasource {

    private val logger = Logger.withTag(KtorUserRemoteDatasource::class.simpleName!!)

    override suspend fun deleteAccount(
        authenticationToken: AuthenticationToken?,
        password: Password,
    ) {
        val response = httpClient.delete(getServerBaseUrl() + PATH_SEGMENT_USER) {
            authenticationToken?.value?.let { bearerAuth(it) }
            contentType(ContentType.Application.Json)
            setBody(password)
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw IllegalStateException(ERROR_MESSAGE_INVALID_CREDENTIALS)
        } else {
            handleUnexpectedRemoteServiceError(response.status, response.bodyAsText())
        }
    }

    override suspend fun getAccount(token: AuthenticationToken?): User {
        val response = httpClient.get(getServerBaseUrl() + PATH_SEGMENT_USER) {
            token?.value?.let { bearerAuth(it) }
        }

        return if (response.status == HttpStatusCode.OK) {
            response.body<UserDto>().toDomainUser()
        } else {
            handleUnexpectedRemoteServiceError(response.status, response.bodyAsText())
        }
    }

    override suspend fun logIn(email: Email, password: Password): AuthenticationToken? {
        val response = httpClient.post(
            getServerBaseUrl() + PATH_SEGMENT_USER + PATH_SEGMENT_LOGIN
        ) {
            contentType(ContentType.Application.Json)
            setBody(LoginCredentials(email = email.value, password = password.value))
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                retrieveAuthenticationTokenWithUserID(response)
            }

            HttpStatusCode.Unauthorized -> {
                throw IllegalStateException(ERROR_MESSAGE_INVALID_CREDENTIALS)
            }

            else -> {
                handleUnexpectedRemoteServiceError(response.status, response.bodyAsText())
            }
        }
    }

    private suspend fun retrieveAuthenticationTokenWithUserID(
        response: HttpResponse,
    ): AuthenticationToken? {
        return if (isAuthenticationTokenRetrievalSupported) {
            AuthenticationToken(response.body<JwtResponse>().jwt)
        } else {
            null
        }
    }

    private fun handleUnexpectedRemoteServiceError(
        statusCode: HttpStatusCode,
        body: String,
    ): Nothing {
        logger.w("Unexpected backend error!\nstatusCode: $statusCode\nbody: $body")

        throw IllegalStateException(ERROR_MESSAGE_NETWORK_ERROR)
    }

    override suspend fun logOut(token: AuthenticationToken?) {
        val response = httpClient.post(
            getServerBaseUrl() + PATH_SEGMENT_USER + PATH_SEGMENT_LOGOUT
        ) {
            headers {
                token?.value?.let { bearerAuth(it) }
            }
        }

        if (response.status != HttpStatusCode.OK) {
            handleUnexpectedRemoteServiceError(response.status, response.bodyAsText())
        }

        logger.i("Successful logout!")
    }

    override suspend fun register(newUser: UserCandidate): AuthenticationToken? {
        val response = httpClient.post(
            getServerBaseUrl() + PATH_SEGMENT_USER + PATH_SEGMENT_REGISTER
        ) {
            contentType(ContentType.Application.Json)
            setBody(newUser.toAuthenticatedUser())
        }

        return when (response.status) {
            HttpStatusCode.Created -> {
                retrieveAuthenticationTokenWithUserID(response)
            }

            HttpStatusCode.Conflict -> {
                handleUniqueUserFieldConflict(response.bodyAsText())
            }

            else -> {
                handleUnexpectedRemoteServiceError(response.status, response.bodyAsText())
            }
        }
    }

    private fun handleUniqueUserFieldConflict(errorResponse: String): Nothing {
        when (errorResponse) {
            ERROR_RESPONSE_EMAIL_ALREADY_TAKEN -> {
                logger.i("Email is already taken: ")
                throw IllegalStateException(ERROR_MESSAGE_EMAIL_IS_ALREADY_TAKEN)
            }

            ERROR_RESPONSE_USERNAME_ALREADY_TAKEN -> {
                logger.i("Username is already taken: ")
                throw IllegalStateException(ERROR_MESSAGE_USERNAME_IS_ALREADY_TAKEN)
            }

            else -> {
                handleUnexpectedRemoteServiceError(HttpStatusCode.Conflict, errorResponse)
            }
        }
    }

    override suspend fun updateAccount(
        authenticationToken: AuthenticationToken?,
        currentPassword: Password,
        updatedUser: UpdatedAccount,
    ): User {
        val response = httpClient.post(
            getServerBaseUrl() + PATH_SEGMENT_USER + PATH_SEGMENT_REGISTER
        ) {
            authenticationToken?.let { bearerAuth(authenticationToken.value) }
            contentType(ContentType.Application.Json)
            setBody(updatedUser.toDto(currentPassword))
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                response.body<UserDto>().toDomainUser()
            }

            HttpStatusCode.Unauthorized -> {
                throw IllegalStateException(ERROR_MESSAGE_INVALID_CREDENTIALS)
            }

            HttpStatusCode.Conflict -> {
                handleUniqueUserFieldConflict(response.bodyAsText())
            }

            else -> {
                handleUnexpectedRemoteServiceError(response.status, response.bodyAsText())
            }
        }
    }

    companion object {
        private const val ERROR_RESPONSE_EMAIL_ALREADY_TAKEN = "Email is already taken"
        private const val ERROR_RESPONSE_USERNAME_ALREADY_TAKEN = "Username is already taken"

        private const val PATH_SEGMENT_LOGIN = "/login"
        private const val PATH_SEGMENT_LOGOUT = "/logout"
        private const val PATH_SEGMENT_REGISTER = "/register"
        private const val PATH_SEGMENT_USER = "/users"
    }
}