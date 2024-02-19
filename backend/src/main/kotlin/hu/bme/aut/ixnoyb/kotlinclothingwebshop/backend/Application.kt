package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend

import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql.query.RecommendedArticlesQuery
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql.schema.KotlinClothingWebshopSchema
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(
        CIO,
        port = 5400,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)
}

fun Application.module() {
    install(CORS) {
        allowHost("0.0.0.0:8080")
        allowHost("127.0.0.1:8080")
        allowHost("localhost:8080")

        allowHeader(HttpHeaders.Accept)
        allowHeader(HttpHeaders.AcceptEncoding)
        allowHeader(HttpHeaders.AcceptLanguage)
        allowHeader(HttpHeaders.AccessControlRequestHeaders)
        allowHeader(HttpHeaders.AccessControlRequestMethod)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.Connection)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Host)
        allowHeader(HttpHeaders.Origin)
        allowHeader(HttpHeaders.Referrer)
        allowHeadersPrefixed("Sec-")
        allowHeader(HttpHeaders.UserAgent)
        allowHeadersPrefixed("X-Apollo-Operation")
        allowHeadersPrefixed("x-apollo-operation")

        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
    }
    install(GraphQL) {
        schema {
            packages = listOf("hu.bme.aut.ixnoyb.kotlinclothingwebshop")
            queries = listOf(
                RecommendedArticlesQuery()
            )
            schemaObject = KotlinClothingWebshopSchema()
        }
    }
    routing {
        graphQLGetRoute()
        graphQLPostRoute()
    }
}