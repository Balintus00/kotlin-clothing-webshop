package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.di

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.datasource.ApolloArticleRemoteDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.datasource.DefaultArticleTransientDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.ArticleRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.DefaultArticleRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.datasource.ArticleLocalDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.datasource.ArticleRemoteDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val articleBrowsingModule = module {
    val persistentLocalDatasourceName = "persistentLocalDatasourceName"
    val transientLocalDatasourceName = "transientLocalDatasourceName"

    includes(
        getWasmLimitedArticleBrowsingModule(
            persistentArticleDatasourceName = persistentLocalDatasourceName
        )
    )

    single<ArticleRepository> {
        DefaultArticleRepository(
            coroutineScope = CoroutineScope(Dispatchers.Default),
            persistentDatasource = get<ArticleLocalDatasource>(
                named(persistentLocalDatasourceName)
            ),
            remoteDatasource = get(),
            transientDatasource = get<ArticleLocalDatasource>(
                named(transientLocalDatasourceName)
            ),
            userPersistentSecureDatasource = get(),
        )
    }

    single<ArticleLocalDatasource>(named(transientLocalDatasourceName)) {
        DefaultArticleTransientDatasource()
    }

    single<ArticleRemoteDatasource> { ApolloArticleRemoteDatasource(apolloClient = get()) }
}