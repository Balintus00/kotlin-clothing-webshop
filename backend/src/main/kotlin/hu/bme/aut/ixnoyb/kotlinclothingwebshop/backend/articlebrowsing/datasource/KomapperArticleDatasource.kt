package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity.Color
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity.GarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity.GraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity.Index
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity.Shade
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity.article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity.toDomainArticle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity.toEntityColor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity.toEntityGarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity.toEntityGraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity.toEntityIndex
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity.toEntityShade
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.datasource.ArticleDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.datasource.ArticleDatasource.Companion.ERROR_MESSAGE_ARTICLE_NOT_FOUND
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Brand
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Description
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Name
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Price
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.paging.ArticlePageSpecification
import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.expression.WhereDeclaration
import org.komapper.core.dsl.operator.and
import org.komapper.core.dsl.operator.lower
import org.komapper.core.dsl.operator.or
import org.komapper.core.dsl.query.firstOrNull
import org.komapper.core.dsl.query.single
import org.komapper.core.dsl.scope.WhereScope
import org.komapper.r2dbc.R2dbcDatabase
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime

class KomapperArticleDatasource(
    private val database: R2dbcDatabase,
    private val databaseConnectionFactory: ConnectionFactory,
) : ArticleDatasource {

    override suspend fun getArticleByID(articleID: ArticleID): Article = database.runQuery {
        val articleSchema = Meta.article

        QueryDsl
            .from(articleSchema)
            .where { articleSchema.id eq articleID.value }
            .firstOrNull()
    }?.toDomainArticle() ?: throw IllegalArgumentException(ERROR_MESSAGE_ARTICLE_NOT_FOUND)

    override suspend fun getArticles(
        filter: ArticleFilter,
        page: ArticlePageSpecification,
    ): List<Article> = database.withTransaction {
        val articleSchema = Meta.article

        val whereDeclaration: WhereDeclaration? = filter.toWhereDeclaration(
            lastReadArticleIDWithCreationDateTime = page.lastReceivedArticleID?.let {
                it to (
                        database.runQuery {
                            QueryDsl
                                .from(articleSchema)
                                .where { articleSchema.id eq it.value }
                                .select(articleSchema.creationDateTime)
                                .single()
                        } ?: throw IllegalArgumentException(ERROR_MESSAGE_ARTICLE_NOT_FOUND)
                        )
            },
        )

        database.runQuery {
            QueryDsl
                .from(articleSchema)
                .limit(page.size.value)
                .orderBy(articleSchema.creationDateTime, articleSchema.id)
                .run { whereDeclaration?.let { where(it) } ?: this }
        }.mapNotNull {
            try {
                it.toDomainArticle()
            } catch (t: Throwable) {
                logger.warn(
                    "Caught exception while mapping persisted article to domain model: " +
                            "$t\nMessage: ${t.message}\nStacktrace: ${t.stackTraceToString()}"
                )

                null
            }
        }
    }

    private fun ArticleFilter.toWhereDeclaration(
        lastReadArticleIDWithCreationDateTime: Pair<ArticleID, OffsetDateTime>?,
    ): WhereDeclaration? {
        val articleSchema = Meta.article

        return listOf(
            name?.value?.let { { lower(articleSchema.name) like it.lowercase() } },
            brand?.value?.let { { lower(articleSchema.brand) like it.lowercase() } },
            minimumPrice?.value?.let { { articleSchema.price greaterEq it } },
            maximumPrice?.value?.let { { articleSchema.price lessEq it } },
            if (colors.isNotEmpty()) {
                val colorFilter: WhereScope.() -> Unit = {
                    articleSchema.color inList colors.map { it.toEntityColor() }
                }

                colorFilter
            } else {
                null
            },
            if (garmentGroups.isNotEmpty()) {
                val garmentGroupFilter: WhereScope.() -> Unit = {
                    articleSchema.garmentGroup inList garmentGroups.map {
                        it.toEntityGarmentGroup()
                    }
                }

                garmentGroupFilter
            } else {
                null
            },
            if (graphicalAppearances.isNotEmpty()) {
                val graphicalAppearanceFilter: WhereScope.() -> Unit = {
                    articleSchema.graphicalAppearance inList graphicalAppearances.map {
                        it.toEntityGraphicalAppearance()
                    }
                }

                graphicalAppearanceFilter
            } else {
                null
            },
            if (indices.isNotEmpty()) {
                val indexFilter: WhereScope.() -> Unit = {
                    articleSchema.index inList indices.map { it.toEntityIndex() }
                }

                indexFilter
            } else {
                null
            },
            if (shades.isNotEmpty()) {
                val shadeFilter: WhereScope.() -> Unit = {
                    articleSchema.shade inList shades.map { it.toEntityShade() }
                }

                shadeFilter
            } else {
                null
            },
            lastReadArticleIDWithCreationDateTime?.let {
                val creationNewerFilter: WhereDeclaration = {
                    articleSchema.creationDateTime less it.second
                }
                val creationEqualFilter: WhereDeclaration = {
                    articleSchema.creationDateTime eq it.second
                }
                val idOrderFilter: WhereDeclaration = {
                    articleSchema.id less it.first.value
                }

                creationNewerFilter.or { creationEqualFilter.and(idOrderFilter) }
            },
        ).reduceOrNull { acc, next ->
            next?.let { acc?.and(it) } ?: acc
        }
    }

    override suspend fun getRecommendedArticles(userEmbedding: FloatArray): List<Article> {
        var connection: Connection? = null

        return try {
            val sqlQuery = """
                |SELECT id, name, brand, price, description, color, garment_group,
                | graphical_appearance, index, shade 
                | FROM articles
                | ORDER BY recommendation_embedding <-> '$userEmbedding' 
                | LIMIT $RECOMMENDED_ARTICLES_COUNT;
                |""".trimMargin()

            connection = databaseConnectionFactory.create().awaitFirst()

            connection.beginTransaction().awaitFirst()

            val query = connection.createStatement(sqlQuery)
            val results = query.add().execute().awaitFirstOrNull()?.map { row, _ ->
                Article(
                    id = ArticleID(row.get("id")?.toString()!!),
                    name = Name(row.get("name")?.toString()!!),
                    brand = Brand(row.get("brand")?.toString()!!),
                    price = Price(row.get("price")?.toString()?.toInt()!!),
                    description = Description(row.get("description")?.toString()!!),
                    color = Color.entries.first {
                        it.ordinal == row.get("color")?.toString()?.toInt()
                    }.toDomainColor(),
                    garmentGroup = GarmentGroup.entries.first {
                        it.ordinal == row.get("garment_group")?.toString()?.toInt()
                    }.toDomainGarmentGroup(),
                    graphicalAppearance = GraphicalAppearance.entries.first {
                        it.ordinal == row.get("graphical_appearance")?.toString()?.toInt()
                    }.toDomainGraphicalAppearance(),
                    index = Index.entries.first {
                        it.ordinal == row.get("index")?.toString()?.toInt()
                    }.toDomainIndex(),
                    shade = Shade.entries.first {
                        it.ordinal == row.get("shade")?.toString()?.toInt()
                    }.toDomainShade(),
                )
            }!!.asFlow().fold(mutableListOf<Article>()) { acc, value ->
                acc.apply { add(value) }
            }

            connection.commitTransaction().awaitFirstOrNull()

            results
        } catch (t: Throwable) {
            logger.warn("Unexpected exception: $t\n${t.message}\n${t.stackTraceToString()}")
            throw t
        } finally {
            connection?.close()?.awaitFirst()
        }
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(KomapperArticleDatasource::class.simpleName!!)

        private const val RECOMMENDED_ARTICLES_COUNT = 40
    }
}