package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.AUTHENTICATION_PROVIDER_NAME
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.CONFIG_PROPERTY_JWT_SECRET
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.AuthenticatedUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.ERROR_MESSAGE_INVALID_DATE_FORMAT
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.JwtResponse
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.LoginCredentials
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.UpdatedUserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.toDto
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UpdatableUserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserService.Companion.ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserService.Companion.ERROR_MESSAGE_USER_INVALID_CREDENTIALS
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserService.Companion.ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.AuthenticationToken
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.TokenID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
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
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaInstant
import org.koin.ktor.ext.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private const val PATH_SEGMENT_USER = "/user"
private const val PATH_SEGMENT_LOGIN = "/login"
private const val PATH_SEGMENT_REGISTER = "/register"
private const val PATH_SEGMENT_LOGOUT = "/logout"

internal fun Route.userManagementRoutes(): List<Route> {
    val service by inject<UserService>()

    val signingSecret = environment!!.config.property(CONFIG_PROPERTY_JWT_SECRET).getString()

    val logger = LoggerFactory.getLogger(Route::class.simpleName!!)

    return listOf(
        route(PATH_SEGMENT_USER + PATH_SEGMENT_LOGIN) {
            post {
                var credentials: LoginCredentials? = null
                try {
                    credentials = call.receive<LoginCredentials>()

                    val authenticationToken = service.login(Email(credentials.email), Password(credentials.password))

                    logger.info("Successful login for: ${credentials.email}")

                    call.respond(
                        OK,
                        JwtResponse(
                            createJwt(
                                authenticationToken = authenticationToken,
                                signingSecret = signingSecret,
                            )
                        )
                    )
                } catch (e: IllegalArgumentException) {
                    when (e.message) {
                        in setOf(*Email.errorMessages.toTypedArray(), Password.INVALID_LENGTH_ERROR_MESSAGE) -> {
                            logger.warn(
                                "Invalid credential input for ${credentials?.email} : ${e.message}"
                            )

                            call.respond(BadRequest, e.message!!)

                        }

                        ERROR_MESSAGE_USER_INVALID_CREDENTIALS -> {
                            logger.info("No user was found with the given credentials for login")

                            call.respond(Unauthorized)
                        }

                        else -> {
                            call.respondWithDefaultError(logger, e)
                        }
                    }
                } catch (t: Throwable) {
                    call.respondWithDefaultError(logger, t)
                }
            }
        },

        route(PATH_SEGMENT_USER + PATH_SEGMENT_REGISTER) {
            post {
                var userInput: AuthenticatedUser? = null

                try {
                    userInput = call.receive<AuthenticatedUser>()

                    val authenticationToken = service.register(userInput.toDomainModel())

                    logger.info("Successful registration for: ${userInput.email}")

                    call.respond(
                        Created,
                        JwtResponse(
                            createJwt(
                                authenticationToken = authenticationToken,
                                signingSecret = signingSecret,
                            )
                        )
                    )
                } catch (e: IllegalArgumentException) {
                    when (e.message) {
                        in setOf(
                            Username.INVALID_LENGTH_ERROR_MESSAGE,
                            Username.INVALID_CHARACTER_ERROR_MESSAGE,
                            *Email.errorMessages.toTypedArray(),
                            Password.INVALID_LENGTH_ERROR_MESSAGE,
                            FirstName.INVALID_LENGTH_ERROR_MESSAGE,
                            FirstName.INVALID_CHARACTER_ERROR_MESSAGE,
                            LastName.INVALID_LENGTH_ERROR_MESSAGE,
                            LastName.INVALID_CHARACTER_ERROR_MESSAGE,
                            DateOfBirth.INVALID_DATE_ERROR_MESSAGE,
                            ERROR_MESSAGE_INVALID_DATE_FORMAT,
                        ) -> {
                            logger.warn(
                                "Invalid user registration input for ${userInput}: ${e.message}"
                            )

                            call.respond(BadRequest, e.message!!)
                        }

                        ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN -> {
                            call.respond(Conflict, e.message!!)
                        }

                        ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN -> {
                            call.respond(Conflict, e.message!!)
                        }

                        else -> {
                            call.respondWithDefaultError(logger, e)
                        }
                    }
                }  catch (t: Throwable) {
                    call.respondWithDefaultError(logger, t)
                }
            }
        },

        authenticate(AUTHENTICATION_PROVIDER_NAME) {

            route(PATH_SEGMENT_USER + PATH_SEGMENT_LOGOUT) {
                post {
                    try {
                        val jwtID = call.principal<JWTPrincipal>()!!.jwtId!!
                        val subject = call.principal<JWTPrincipal>()!!.subject!!

                        service.logout(TokenID(jwtID))

                        logger.info("Successful logout $subject")

                        call.respond(OK)
                    } catch (t: Throwable) {
                        call.respondWithDefaultError(logger, t)
                    }
                }
            }

            route(PATH_SEGMENT_USER) {
                get {
                    try {
                        val subject = call.principal<JWTPrincipal>()!!.subject!!

                        val userDto = service.getUser(UserID(subject)).toDto()

                        logger.info("Successful user query for subject: $subject")

                        call.respond(
                            status = OK,
                            message = userDto,
                        )
                    } catch (t: Throwable) {
                        call.respondWithDefaultError(logger, t)
                    }
                }

                put {
                    var updatableUserCandidate: UpdatedUserCandidate? = null

                    try {
                        updatableUserCandidate = call.receive<UpdatedUserCandidate>()

                        val subject = call.principal<JWTPrincipal>()!!.subject!!

                        val updatedUserDto = service.updateUser(
                            UpdatableUserCandidate(
                                id = UserID(subject),
                                currentPassword = Password(updatableUserCandidate.currentPassword),
                                userCandidate = updatableUserCandidate.updatedUser.toDomainModel(),
                            )
                        ).toDto()

                        logger.info("Successful user update for subject: $subject")

                        call.respond(
                            status = OK,
                            message = updatedUserDto,
                        )
                    } catch (e: IllegalArgumentException) {
                        when (e.message) {
                            in setOf(
                                Username.INVALID_LENGTH_ERROR_MESSAGE,
                                Username.INVALID_CHARACTER_ERROR_MESSAGE,
                                *Email.errorMessages.toTypedArray(),
                                Password.INVALID_LENGTH_ERROR_MESSAGE,
                                FirstName.INVALID_LENGTH_ERROR_MESSAGE,
                                FirstName.INVALID_CHARACTER_ERROR_MESSAGE,
                                LastName.INVALID_LENGTH_ERROR_MESSAGE,
                                LastName.INVALID_CHARACTER_ERROR_MESSAGE,
                                DateOfBirth.INVALID_DATE_ERROR_MESSAGE,
                                ERROR_MESSAGE_INVALID_DATE_FORMAT,
                            ) -> {
                                logger.warn(
                                    "Invalid user update input for ${updatableUserCandidate}: ${e.message}"
                                )

                                call.respond(BadRequest, e.message!!)
                            }

                            ERROR_MESSAGE_USER_INVALID_CREDENTIALS -> {
                                logger.info("Password was incorrect for user update!")

                                call.respond(Unauthorized)
                            }

                            ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN -> {
                                logger.info("Updated username is already taken for profile update!")

                                call.respond(Conflict)
                            }

                            ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN -> {
                                logger.info("Updated email is already taken for profile update!")

                                call.respond(Conflict)
                            }

                            else -> {
                                call.respondWithDefaultError(logger, e)
                            }
                        }
                    } catch (t: Throwable) {
                        call.respondWithDefaultError(logger, t)
                    }
                }

                delete {
                    try {
                        val subject = call.principal<JWTPrincipal>()!!.subject!!

                        service.deleteUser(UserID(subject))

                        logger.info("Successful user deletion for subject: $subject")

                        call.respond(NoContent)
                    } catch (t: Throwable) {
                        call.respondWithDefaultError(logger, t)
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

private fun createJwt(authenticationToken: AuthenticationToken, signingSecret: String): String {
    val currentInstant = Clock.System.now()

    return JWT.create()
        .withJWTId(authenticationToken.id.value)
        .withSubject(authenticationToken.ownerUserID.value)
        .withIssuedAt(currentInstant.toJavaInstant())
        .withExpiresAt(
            currentInstant.plus(
                UserService.JWT_VALID_DURATION_IN_DAYS,
                DateTimeUnit.DAY,
                TimeZone.currentSystemDefault(),
            ).toJavaInstant()
        )
        .sign(Algorithm.HMAC256(signingSecret))
}

private suspend fun ApplicationCall.respondWithDefaultError(logger: Logger, t: Throwable) {
    logger.warn("Unexpected server side error: ${t.message}\n${t.stackTraceToString()}")

    respond(InternalServerError)
}