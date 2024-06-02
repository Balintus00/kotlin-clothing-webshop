package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.datasource.ArticleLocalDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.ArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class DefaultArticleTransientDatasource : ArticleLocalDatasource {

    private val articles = MutableStateFlow<List<Article>>(emptyList())

    private val articlePreviews = MutableStateFlow<List<ArticlePreview>>(emptyList())

    override fun getAllPreviews(): Flow<List<ArticlePreview>> = articlePreviews

    override fun getByID(articleID: ArticleID): Flow<Article?> = articles.map { articles ->
        articles.firstOrNull { it.id == articleID }
    }

    override suspend fun clear() {
        articles.update { emptyList() }
        articlePreviews.update { emptyList() }
    }

    override suspend fun insertPreviews(vararg articlePreviews: ArticlePreview) {
        this.articlePreviews.update { previousList ->
            previousList.toMutableList().apply { addAll(articlePreviews) }
        }
    }

    override suspend fun update(vararg articles: Article) {
        this.articles.update { previousList ->
            previousList.toMutableList().apply { addAll(articles) }
        }
    }
}