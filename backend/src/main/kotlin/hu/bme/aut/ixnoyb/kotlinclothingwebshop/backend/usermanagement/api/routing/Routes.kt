package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.AUTHENTICATION_COOKIE_NAME_JWT
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.AUTHENTICATION_PROVIDER_NAME
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.CONFIG_PROPERTY_JWT_SECRET
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.AuthenticatedUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.ERROR_MESSAGE_INVALID_DATE_FORMAT
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.JwtResponse
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.LoginCredentials
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.UpdatedUserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.toDto
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UpdatableUserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UserService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UserService.Companion.ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UserService.Companion.ERROR_MESSAGE_USER_INVALID_CREDENTIALS
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UserService.Companion.ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UserService.Companion.JWT_VALID_DURATION_IN_DAYS
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.model.AuthenticationToken
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.model.TokenID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username
import io.ktor.http.Cookie
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
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaInstant
import org.koin.ktor.ext.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.days

internal fun Route.userManagementRoutes(): List<Route> {
    val logger = LoggerFactory.getLogger(Route::class.simpleName!!)

    val service by inject<UserService>()

    val errorResponseEmailIsAlreadyTaken = "Email is already taken"
    val errorResponseUsernameIsAlreadyTaken = "Username is already taken"

    val pathSegmentLogin = "/login"
    val pathSegmentLogout = "/logout"
    val pathSegmentBrowser = "/browser"
    val pathSegmentRegister = "/register"
    val pathSegmentUser = "/users"

    val compoundPathSegmentLogin = pathSegmentUser + pathSegmentLogin
    val compoundPathSegmentLogout = pathSegmentUser + pathSegmentLogout
    val compoundPathSegmentRegister = pathSegmentUser + pathSegmentRegister

    val signingSecret = environment!!.config.property(CONFIG_PROPERTY_JWT_SECRET).getString()

    val userInputDomainErrorMessages = setOf(
        DateOfBirth.TOO_EARLY_DATE_ERROR_MESSAGE,
        DateOfBirth.TOO_LATE_DATE_ERROR_MESSAGE,
        *Email.ERROR_MESSAGES.toTypedArray(),
        FirstName.INVALID_CHARACTER_ERROR_MESSAGE,
        FirstName.TOO_LONG_ERROR_MESSAGE,
        FirstName.TOO_SHORT_ERROR_MESSAGE,
        LastName.INVALID_CHARACTER_ERROR_MESSAGE,
        LastName.TOO_LONG_ERROR_MESSAGE,
        LastName.TOO_SHORT_ERROR_MESSAGE,
        Password.TOO_SHORT_ERROR_MESSAGE,
        Password.TOO_LONG_ERROR_MESSAGE,
        Username.INVALID_CHARACTER_ERROR_MESSAGE,
        Username.TOO_LONG_ERROR_MESSAGE,
        Username.TOO_SHORT_ERROR_MESSAGE,
        ERROR_MESSAGE_INVALID_DATE_FORMAT,
    )

    return listOf(
        route(compoundPathSegmentLogin) {
            post {
                logIn(
                    call = call,
                    logger = logger,
                    service = service,
                )?.let { authenticationToken ->
                    call.respond(
                        OK,
                        JwtResponse(
                            createJwt(
                                authenticationToken = authenticationToken,
                                signingSecret = signingSecret,
                            )
                        )
                    )
                }
            }
        },

        route(pathSegmentBrowser + compoundPathSegmentLogin) {
            post {
                logIn(
                    call = call,
                    logger = logger,
                    service = service,
                )?.let { authenticationToken ->
                    setAuthenticationCookies(authenticationToken, call, signingSecret)

                    call.respond(OK)
                }
            }
        },

        route(compoundPathSegmentRegister) {
            post {
                register(
                    call = call,
                    inputErrorMessages = userInputDomainErrorMessages,
                    errorResponseEmailIsAlreadyTaken = errorResponseEmailIsAlreadyTaken,
                    errorResponseUsernameIsAlreadyTaken = errorResponseUsernameIsAlreadyTaken,
                    logger = logger,
                    service = service,
                )?.let { authenticationToken ->
                    call.respond(
                        Created,
                        JwtResponse(
                            createJwt(
                                authenticationToken = authenticationToken,
                                signingSecret = signingSecret,
                            )
                        )
                    )

                }
            }
        },

        route(pathSegmentBrowser + compoundPathSegmentRegister) {
            post {
                register(
                    call = call,
                    inputErrorMessages = userInputDomainErrorMessages,
                    errorResponseEmailIsAlreadyTaken = errorResponseEmailIsAlreadyTaken,
                    errorResponseUsernameIsAlreadyTaken = errorResponseUsernameIsAlreadyTaken,
                    logger = logger,
                    service = service,
                )?.let { authenticationToken ->
                    setAuthenticationCookies(authenticationToken, call, signingSecret)

                    call.respond(Created)
                }
            }
        },

        authenticate(AUTHENTICATION_PROVIDER_NAME) {
            route(compoundPathSegmentLogout) {
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

            route(pathSegmentUser) {
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

                        val updatedUserDto = updatableUserCandidate.run {
                            service.updateUser(
                                UpdatableUserCandidate(
                                    id = UserID(subject),
                                    currentPassword = Password(currentPassword),
                                    newPassword = newPassword?.let { Password(it) },
                                    user = User(
                                        dateOfBirth = DateOfBirth(
                                            LocalDate.parse(dateOfBirth)
                                        ),
                                        email = Email(email),
                                        firstName = FirstName(firstName),
                                        lastName = LastName(lastName),
                                        username = Username(username),
                                    ),
                                )
                            ).toDto()
                        }

                        logger.info("Successful user update for subject: $subject")

                        call.respond(
                            status = OK,
                            message = updatedUserDto,
                        )
                    } catch (e: IllegalArgumentException) {
                        when (e.message) {
                            in userInputDomainErrorMessages -> {
                                logger.warn(
                                    "Invalid user update input for ${updatableUserCandidate}: " +
                                            "${e.message}"
                                )

                                call.respond(BadRequest, e.message!!)
                            }

                            ERROR_MESSAGE_USER_INVALID_CREDENTIALS -> {
                                logger.info("Password was incorrect for user update!")

                                call.respond(Unauthorized)
                            }

                            ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN -> {
                                logger.info("Updated username is already taken for profile update!")

                                call.respond(Conflict, errorResponseUsernameIsAlreadyTaken)
                            }

                            ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN -> {
                                logger.info("Updated email is already taken for profile update!")

                                call.respond(Conflict, errorResponseEmailIsAlreadyTaken)
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

                        val password = call.receiveText()

                        service.deleteUser(Password(password), UserID(subject))

                        logger.info("Successful user deletion for subject: $subject")

                        call.respond(NoContent)
                    } catch (e: IllegalArgumentException) {
                        when (e.message) {
                            in setOf(
                                Password.TOO_SHORT_ERROR_MESSAGE,
                                Password.TOO_LONG_ERROR_MESSAGE,
                            ) -> call.respond(BadRequest, e.message!!)

                            ERROR_MESSAGE_USER_INVALID_CREDENTIALS -> call.respond(Unauthorized)

                            else -> call.respondWithDefaultError(logger, e)
                        }
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

private suspend fun logIn(
    call: ApplicationCall,
    logger: Logger,
    service: UserService,
): AuthenticationToken? {
    var credentials: LoginCredentials? = null
    return try {
        credentials = call.receive<LoginCredentials>()

        service.login(
            Email(credentials.email),
            Password(credentials.password)
        ).also { logger.info("Successful login for ${credentials.email}!") }
    } catch (e: IllegalArgumentException) {
        when (e.message) {
            in setOf(
                *Email.ERROR_MESSAGES.toTypedArray(),
                Password.TOO_SHORT_ERROR_MESSAGE,
                Password.TOO_LONG_ERROR_MESSAGE,
            ) -> {
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

        null
    } catch (t: Throwable) {
        call.respondWithDefaultError(logger, t)

        null
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
                JWT_VALID_DURATION_IN_DAYS,
                DateTimeUnit.DAY,
                TimeZone.currentSystemDefault(),
            ).toJavaInstant()
        )
        .sign(Algorithm.HMAC256(signingSecret))
}


private fun setAuthenticationCookies(
    authenticationToken: AuthenticationToken,
    call: ApplicationCall,
    signingSecret: String,
) {
    //val authenticationCookieNameUserID = "user_id" TODO probably not needed

    val cookieNameSameSite = "SameSite"
    val cookieSameSiteStrictValue = "strict"

    val authenticationCookieMaxAge = JWT_VALID_DURATION_IN_DAYS.days.inWholeSeconds.toInt()

    // TODO set secure when HTTPS support will be implemented for backend
    val jwtCookie = Cookie(
        extensions = mapOf(
            cookieNameSameSite to cookieSameSiteStrictValue
        ),
        httpOnly = true,
        maxAge = authenticationCookieMaxAge,
        name = AUTHENTICATION_COOKIE_NAME_JWT,
        value = createJwt(
            authenticationToken = authenticationToken,
            signingSecret = signingSecret,
        ),
    )

    call.response.cookies.append(jwtCookie)

    /*    TODO probably not needed
    call.response.cookies.append(
        jwtCookie.copy(
            httpOnly = false,
            name = authenticationCookieNameUserID,
            value = authenticationToken.ownerUserID.value,
        )
    )*/
}

private suspend fun register(
    call: ApplicationCall,
    inputErrorMessages: Set<String>,
    errorResponseUsernameIsAlreadyTaken: String,
    errorResponseEmailIsAlreadyTaken: String,
    logger: Logger,
    service: UserService,
): AuthenticationToken? {
    var userInput: AuthenticatedUser? = null

    return try {
        userInput = call.receive<AuthenticatedUser>()

        return service.register(userInput.toDomainModel()).also {
            logger.info("Successful registration for: ${userInput.email}")
        }
    } catch (e: IllegalArgumentException) {
        when (e.message) {
            in inputErrorMessages -> {
                logger.warn(
                    "Invalid user registration input for ${userInput}: ${e.message}"
                )

                call.respond(BadRequest, e.message!!)
            }

            ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN -> {
                call.respond(Conflict, errorResponseUsernameIsAlreadyTaken)
            }

            ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN -> {
                call.respond(Conflict, errorResponseEmailIsAlreadyTaken)
            }

            else -> {
                call.respondWithDefaultError(logger, e)
            }
        }

        null
    } catch (t: Throwable) {
        call.respondWithDefaultError(logger, t)

        null
    }

}

private suspend fun ApplicationCall.respondWithDefaultError(logger: Logger, t: Throwable) {
    logger.warn("Unexpected server side error: ${t.message}\n${t.stackTraceToString()}")

    respond(InternalServerError)
}