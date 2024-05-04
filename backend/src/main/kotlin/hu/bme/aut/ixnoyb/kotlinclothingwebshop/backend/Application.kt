package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend

import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.di.clothingArticleBrowserModule
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.di.getCommonModule
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql.query.RecommendedArticlesQuery
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql.schema.KotlinClothingWebshopSchema
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.service.ClothingWebshopService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.routing.userManagementRoutes
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.di.userManagementModule
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.TokenID
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.slf4j.LoggerFactory

internal const val AUTHENTICATION_PROVIDER_NAME = "auth-bearer"

internal const val CONFIG_PROPERTY_JWT_SECRET = "jwt.secret"

fun main(args: Array<String>) = io.ktor.server.cio.EngineMain.main(args)

fun Application.module() {
    configureDI()
    configureAuthentication()
    configureCORS()
    configureGraphQLPlugin()
    configureRouting()
}

private fun Application.configureDI() {
    install(Koin) {
        logger(KermitKoinLogger(Logger.withTag("koin")))

        modules(
            getCommonModule(environment),
            clothingArticleBrowserModule,
            userManagementModule,
        )
    }
}

private fun Application.configureAuthentication() {
    val configurationEnvironment = environment
    val userService: UserService by inject()

    install(Authentication) {
        val logger = LoggerFactory.getLogger(Authentication::class.simpleName!!)

        jwt(AUTHENTICATION_PROVIDER_NAME) {
            verifier {
                JWT.require(
                    Algorithm.HMAC256(
                        configurationEnvironment.config.property(CONFIG_PROPERTY_JWT_SECRET).getString()
                    )
                )
                    .build()
            }

            validate { jwtCredential ->
                try {
                    val jwtID = jwtCredential.jwtId
                    val subject = jwtCredential.subject
                    val expiresAt = jwtCredential.expiresAt

                    when {
                        setOf(jwtID, subject, expiresAt).any { it == null } -> {
                            logger.error("JWT misses required fields jti: $jwtID sub: $subject exp: $expiresAt")

                            null
                        }

                        expiresAt!!.toInstant() < Clock.System.now().toJavaInstant()-> {
                            logger.info(
                                "Expired JWT was tried to be used with jti: $jwtID sub: $subject exp: $expiresAt"
                            )

                            null
                        }

                        userService.checkAuthenticationTokenIsBlackListed(TokenID(jwtID!!)) -> {
                            logger.warn(
                                "Blacklisted JWT was tried to be used with jti: $jwtID sub: $subject exp: $expiresAt"
                            )

                            null
                        }

                        else -> {
                            logger.info("Successful validation for jti: $jwtID sub: $subject $expiresAt")

                            UserIdPrincipal(subject!!)
                        }
                    }
                } catch (t: Throwable) {
                    logger.error("Unexpected exception while validating JWT: ${t.message}\n${t.stackTraceToString()}")

                    null
                }
            }
        }
    }
}

private fun Application.configureCORS() {
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
        allowMethod(HttpMethod.Put)
    }
}

private fun Application.configureGraphQLPlugin() {
    val clothingWebshopService: ClothingWebshopService by inject()

    install(GraphQL) {
        schema {
            packages = listOf("hu.bme.aut.ixnoyb.kotlinclothingwebshop")
            queries = listOf(
                RecommendedArticlesQuery(clothingWebshopService)
            )
            schemaObject = KotlinClothingWebshopSchema()
        }
    }
}

private fun Application.configureRouting() {
    routing {
        graphQLGetRoute()
        graphQLPostRoute()
        userManagementRoutes()
    }
}