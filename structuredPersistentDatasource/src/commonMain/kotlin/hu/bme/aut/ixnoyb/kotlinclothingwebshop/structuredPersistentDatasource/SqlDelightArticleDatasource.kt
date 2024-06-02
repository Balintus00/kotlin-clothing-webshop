package hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Suppress("unused") // Public API
class SqlDelightArticleDatasource(private val queries: ArticleQueries) {

    fun getAllPreviews(): Flow<List<Article>> =
        queries
            .selectAllPreview { id, name, price, brand, description, imageUrl ->
                Article(
                    id = id,
                    name = name,
                    price = price,
                    brand = brand,
                    description = description,
                    image_url = imageUrl,
                    article_index = null,
                    color = null,
                    garment_group = null,
                    graphical_appearance = null,
                    shade = null,
                )
            }
            .asFlow()
            .mapToList(Dispatchers.IO)

    fun getArticleByID(articleID: ArticleID): Flow<Article?> =
        queries
            .selectByID(articleID.value) { id, name, price, brand, description, imageUrl, color,
                                           garmentGroup, graphicalAppearance, index, shade ->
                Article(
                    id = id,
                    name = name,
                    price = price,
                    brand = brand,
                    description = description,
                    image_url = imageUrl,
                    article_index = index,
                    color = color,
                    garment_group = garmentGroup,
                    graphical_appearance = graphicalAppearance,
                    shade = shade,
                )
            }
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)

    suspend fun clear() = withContext(Dispatchers.IO) {
        queries.clear()
    }

    suspend fun insertPreviews(vararg previews: Article) = withContext(Dispatchers.IO) {
        queries.transaction {
            previews.forEach {
                queries.insertPreview(it)
            }
        }
    }

    suspend fun updateArticles(vararg articles: Article) = withContext(Dispatchers.IO) {
        queries.transaction {
            articles.forEach { queries.upsertArticle(it) }
        }
    }
}