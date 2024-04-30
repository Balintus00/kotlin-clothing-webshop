package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.routing

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.AUTHENTICATION_PROVIDER_NAME
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.AuthenticatedUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.JwtResponse
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.LoginCredentials
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.UpdatedUserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.toDto
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UpdatableUserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private const val PATH_SEGMENT_USER = "/user"
private const val PATH_SEGMENT_LOGIN = "/login"
private const val PATH_SEGMENT_REGISTER = "/register"
private const val PATH_SEGMENT_LOGOUT = "/logout"

private const val CLAIM_USER_ID = "userId"

internal fun Route.userManagementRoutes(): List<Route> {
    val service by inject<UserService>()

    val logger = LoggerFactory.getLogger(Route::class.java)

    return listOf(
        route(PATH_SEGMENT_USER + PATH_SEGMENT_LOGIN) {
            post {
                try {
                    val credentials = call.receive<LoginCredentials>()

                    call.respond(
                        JwtResponse(
                            service.login(Email(credentials.email), Password(credentials.password)).toString()
                        )
                    )
                } catch (t: Throwable) {

                    call.respondWithDefaultError(logger, t.message)
                }
            }
        },
        route(PATH_SEGMENT_USER + PATH_SEGMENT_REGISTER) {
            post {
                try {
                    call.respond(
                        JwtResponse(
                            service.register(call.receive<AuthenticatedUser>().toDomainModel()).toString()
                        )
                    )
                } catch (t: Throwable) {
                    call.respondWithDefaultError(logger, t.message)
                }
            }
        },
        authenticate(AUTHENTICATION_PROVIDER_NAME) {
            route(PATH_SEGMENT_USER + PATH_SEGMENT_LOGOUT) {
                post {
                    try {
                        service.logout(call.principal<JWTPrincipal>()!!.jwtId!!)

                        call.respond(OK)

                    } catch (t: Throwable) {
                        call.respondWithDefaultError(logger, t.message)
                    }
                }
            }

            route(PATH_SEGMENT_USER) {
                get {
                    try {
                        call.respond(
                            status = OK,
                            message = service.getUser(
                                UserID(
                                    call.principal<JWTPrincipal>()!!.payload.getClaim(CLAIM_USER_ID).asString()
                                ),
                            ).toDto(),
                        )
                    } catch (t: Throwable) {
                        call.respondWithDefaultError(logger, t.message)
                    }
                }

                put {
                    try {
                        val updatableUserCandidate = call.receive<UpdatedUserCandidate>()

                        call.respond(
                            status = OK,
                            message = service.updateUser(
                                UpdatableUserCandidate(
                                    id = UserID(
                                        call.principal<JWTPrincipal>()!!.payload.getClaim(CLAIM_USER_ID).asString()
                                    ),
                                    currentPassword = Password(updatableUserCandidate.currentPassword),
                                    updatedUserCandidate = updatableUserCandidate.updatedUser.toDomainModel(),
                                )
                            ).toDto(),
                        )
                    } catch (t: Throwable) {
                        call.respondWithDefaultError(logger, t.message)
                    }
                }
            }
        },
    ).onEach {
        it.install(ContentNegotiation) {
            json()
        }
    }
}

private suspend fun ApplicationCall.respondWithDefaultError(logger: Logger, message: String?) {
    logger.warn("Unexpected server side error: $message")

    respond(InternalServerError)
}