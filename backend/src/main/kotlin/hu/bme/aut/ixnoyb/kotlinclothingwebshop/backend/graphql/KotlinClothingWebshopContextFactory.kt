package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql

import com.expediagroup.graphql.generator.extensions.plus
import com.expediagroup.graphql.server.ktor.DefaultKtorGraphQLContextFactory
import graphql.GraphQLContext
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.AUTHENTICATION_COOKIE_NAME_JWT
import io.ktor.server.request.ApplicationRequest

class KotlinClothingWebshopContextFactory : DefaultKtorGraphQLContextFactory() {

    override suspend fun generateContext(request: ApplicationRequest): GraphQLContext {
        return super.generateContext(request).plus(
            mapOf(
                JWT_AUTHENTICATION_HEADER_KEY to request.headers["Authentication"]
                    ?.split(' ')
                    ?.getOrNull(1),
                JWT_COOKIE_KEY to request.cookies[AUTHENTICATION_COOKIE_NAME_JWT],
            ).filter { it.value != null }
        )
    }

    companion object {

        const val JWT_AUTHENTICATION_HEADER_KEY = "JWT_AUTHENTICATION_HEADER_KEY"
        const val JWT_COOKIE_KEY = "JWT_COOKIE_KEY"
    }
}