package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.ArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import kotlinx.coroutines.flow.Flow

interface ArticleLocalDatasource {

    fun getAllPreviews(): Flow<List<ArticlePreview>>

    fun getByID(articleID: ArticleID): Flow<Article?>

    suspend fun clear()

    suspend fun insertPreviews(vararg articlePreviews: ArticlePreview)

    suspend fun update(vararg articles: Article)
}