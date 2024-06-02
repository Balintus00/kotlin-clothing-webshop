package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend

import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.auth0.jwt.JWTVerifier
import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.query.ArticleByIdQuery
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.query.ArticlesQuery
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.query.RecommendedArticlesQuery
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.rest.articleBrowsingRoutes
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.di.clothingArticleBrowserModule
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.ArticleService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.di.getCommonModule
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql.KotlinClothingWebshopContextFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql.KotlinClothingWebshopSchema
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.routing.userManagementRoutes
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.di.userManagementModule
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UserService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.model.TokenID
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.Principal
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.parseAuthorizationHeader
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.slf4j.LoggerFactory
import java.util.Date

internal const val AUTHENTICATION_PROVIDER_NAME = "auth-bearer"
internal const val AUTHENTICATION_COOKIE_NAME_JWT = "access_token"

internal const val CONFIG_PROPERTY_JWT_SECRET = "jwt.secret"

fun main(args: Array<String>) = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // This function is referenced from the application.conf file
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
    val jwtVerifier: JWTVerifier by inject()
    val userService: UserService by inject()

    val logger = LoggerFactory.getLogger(Authentication::class.simpleName!!)

    install(Authentication) {
        jwt(AUTHENTICATION_PROVIDER_NAME) {
            authHeader {
                it.request.parseAuthorizationHeader() ?: run {
                    it.request.cookies[AUTHENTICATION_COOKIE_NAME_JWT]?.let { jwtCandidate ->
                        try {
                            parseAuthorizationHeader("Bearer $jwtCandidate")
                        } catch (t: Throwable) {
                            logger.warn(
                                "Invalid JWT found in authentication cookie: $jwtCandidate\n" +
                                        "Message: ${t.message}\n" +
                                        "Stacktrace: ${t.stackTraceToString()}"
                            )

                            null
                        }
                    }
                }
            }

            verifier { jwtVerifier }

            validate { jwtCredential ->
                validateKotlinClothingWebshopRequirements(
                    jwtID = jwtCredential.jwtId,
                    subject = jwtCredential.subject,
                    expiresAt = jwtCredential.expiresAt,
                    logger = logger,
                    userService = userService,
                )
            }
        }
    }
}

suspend fun validateKotlinClothingWebshopRequirements(
    jwtID: String?,
    subject: String?,
    expiresAt: Date?,
    logger: org.slf4j.Logger,
    userService: UserService,
): Principal? = try {
    when {
        setOf(jwtID, subject, expiresAt).any { it == null } -> {
            logger.error(
                "JWT misses required fields jti: $jwtID sub: $subject " +
                        "exp: $expiresAt"
            )

            null
        }

        expiresAt!!.toInstant() < Clock.System.now().toJavaInstant() -> {
            logger.info(
                "Expired JWT was tried to be used with jti: $jwtID sub: $subject " +
                        "exp: $expiresAt"
            )

            null
        }

        userService.checkAuthenticationTokenIsBlackListed(TokenID(jwtID!!)) -> {
            logger.warn(
                "Blacklisted JWT was tried to be used with jti: $jwtID " +
                        "sub: $subject exp: $expiresAt"
            )

            null
        }

        else -> {
            logger.info(
                "Successful validation for jti: $jwtID sub: $subject $expiresAt"
            )

            UserIdPrincipal(subject!!)
        }
    }
} catch (t: Throwable) {
    logger.error(
        "Unexpected exception while validating JWT: " +
                "${t.message}\n${t.stackTraceToString()}"
    )

    null
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
    val articleService: ArticleService by inject()
    val jwtVerifier: JWTVerifier by inject()
    val userService: UserService by inject()

    install(GraphQL) {
        schema {
            packages = listOf("hu.bme.aut.ixnoyb.kotlinclothingwebshop")
            queries = listOf(
                ArticleByIdQuery(articleService),
                ArticlesQuery(articleService),
                RecommendedArticlesQuery(
                    articleService = articleService,
                    jwtVerifier = jwtVerifier,
                    userService = userService,
                ),
            )
            schemaObject = KotlinClothingWebshopSchema()
            server {
                contextFactory = KotlinClothingWebshopContextFactory()
            }
        }
    }
}

private fun Application.configureRouting() {
    routing {
        graphQLGetRoute()
        graphQLPostRoute()
        articleBrowsingRoutes()
        userManagementRoutes()
    }
}