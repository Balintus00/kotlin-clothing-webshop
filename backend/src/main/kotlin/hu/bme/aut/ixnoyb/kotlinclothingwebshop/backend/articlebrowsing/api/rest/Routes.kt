package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.rest

import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.slf4j.LoggerFactory

internal fun Route.articleBrowsingRoutes(): List<Route> {
    val logger = LoggerFactory.getLogger(Route::class.simpleName!!)

    return listOf(
        route("/article/image/{articleID}") {
            get {
                call.parameters["articleID"]?.let { articleID ->
                    logger.info("Path: /images/$articleID.jpg")
                    val imageStream = javaClass.getResourceAsStream(
                        "/images/$articleID.jpg"
                    )

                    imageStream?.let {
                        call.respondBytes(imageStream.readAllBytes())
                    } ?: run {
                        call.respond(NotFound)
                    }
                } ?: run {
                    call.respond(NotFound)
                }
            }
        }
    )
}