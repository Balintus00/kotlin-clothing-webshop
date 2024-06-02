package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.query

import com.auth0.jwt.JWTVerifier
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.dto.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.dto.toGraphQLArticle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.ArticleService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql.KotlinClothingWebshopContextFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UserService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.validateKotlinClothingWebshopRequirements
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID
import org.slf4j.LoggerFactory

class RecommendedArticlesQuery(
    private val articleService: ArticleService,
    private val jwtVerifier: JWTVerifier,
    private val userService: UserService,
) : Query {

    @Suppress("unused")
    suspend fun getRecommendedArticles(
        dataFetchingEnvironment: DataFetchingEnvironment,
    ): List<Article> = try {
        val graphQLContext = dataFetchingEnvironment.graphQlContext

        var jwt: String?

        jwt = graphQLContext.getOrDefault<String>(
            KotlinClothingWebshopContextFactory.JWT_AUTHENTICATION_HEADER_KEY,
            null,
        )

        if (jwt == null) {
            jwt = graphQLContext.getOrDefault<String>(
                KotlinClothingWebshopContextFactory.JWT_COOKIE_KEY,
                null,
            )
        }

        val userID: String? = jwt?.let {
            try {
                val decodedJwt = jwtVerifier.verify(jwt)

                validateKotlinClothingWebshopRequirements(
                    expiresAt = decodedJwt?.expiresAt,
                    jwtID = decodedJwt?.id,
                    logger = logger,
                    subject = decodedJwt?.subject,
                    userService = userService,
                )

                decodedJwt?.subject
            } catch (t: Throwable) {
                logger.warn(
                    "Invalid JWT found: $jwt\nException: $t\nMessage: ${t.message}\n" +
                            "Stacktrace: ${t.stackTraceToString()}\n" +
                            "Making fallback to default user ID."
                )

                null
            }
        }

        logger.info("User ID: $userID")

        articleService.getRecommendedArticles(
            userID?.let { UserID(userID) }
        ).map { it.toGraphQLArticle() }
    } catch (t: Throwable) {
        logger.warn("Unexpected exception: $t\n${t.message}\n${t.stackTraceToString()}")

        throw Exception()
    }

    companion object {

        private val logger = LoggerFactory.getLogger(RecommendedArticlesQuery::class.simpleName!!)
    }
}