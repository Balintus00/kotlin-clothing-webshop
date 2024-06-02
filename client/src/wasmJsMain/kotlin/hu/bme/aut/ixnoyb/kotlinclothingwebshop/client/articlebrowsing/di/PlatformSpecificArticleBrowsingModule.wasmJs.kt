package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.di

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.datasource.DefaultArticleTransientDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.datasource.ArticleLocalDatasource
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual fun getWasmLimitedArticleBrowsingModule(
    persistentArticleDatasourceName: String
): Module = module {
    single<ArticleLocalDatasource>(named(persistentArticleDatasourceName)) {
        DefaultArticleTransientDatasource()
    }
}