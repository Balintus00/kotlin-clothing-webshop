package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend

import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql.query.RecommendedArticlesQuery
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql.schema.KotlinClothingWebshopSchema
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
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