package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.routing

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.AUTHENTICATION_PROVIDER_NAME
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.AuthenticatedUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.JwtResponse
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.LoginCredentials
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto.User
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

private const val PATH_SEGMENT_USER = "/user"
private const val PATH_SEGMENT_LOGIN = "/login"
private const val PATH_SEGMENT_REGISTER = "/register"
private const val PATH_SEGMENT_LOGOUT = "/logout"

fun Route.userManagementRoutes(): List<Route> = listOf(
    route(PATH_SEGMENT_USER + PATH_SEGMENT_LOGIN) {
        post {
            // TODO
            val credentials = call.receive<LoginCredentials>()

            call.respond(JwtResponse("good"))
        }
    },
    route(PATH_SEGMENT_USER + PATH_SEGMENT_REGISTER) {
        post {
            // TODO
            val user = call.receive<AuthenticatedUser>()

            call.respond(JwtResponse("good"))
        }
    },
    authenticate(AUTHENTICATION_PROVIDER_NAME) {
        route(PATH_SEGMENT_USER + PATH_SEGMENT_LOGOUT) {
            post {
                // TODO
                call.respond(OK)
            }
        }
        route(PATH_SEGMENT_USER) {
            put {
                // TODO
                val updatedUser = call.receive<AuthenticatedUser>()

                call.respond(
                    status = OK,
                    message = User(
                        username = "username",
                        email = "email",
                        firstName = "firstName",
                        lastName = "lastName",
                        dateOfBirth = "dateOfBirth",
                    ),
                )
            }
        }
    },
).onEach {
    it.install(ContentNegotiation) {
        json()
    }
}