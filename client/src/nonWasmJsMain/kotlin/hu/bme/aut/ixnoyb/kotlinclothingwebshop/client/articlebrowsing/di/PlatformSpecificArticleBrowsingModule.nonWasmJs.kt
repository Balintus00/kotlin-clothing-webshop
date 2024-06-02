package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.di

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.datasource.SqlDelightAdapterArticleLocalDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.datasource.ArticleLocalDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource.ArticleQueries
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource.SqlDelightArticleDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource.SqlDelightDatabase
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource.createDatabase
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual fun getWasmLimitedArticleBrowsingModule(
    persistentArticleDatasourceName: String,
): Module = module {
    includes(nonWasmPlatformSpecificArticleBrowsingModule)

    single<ArticleLocalDatasource>(named(persistentArticleDatasourceName)) {
        SqlDelightAdapterArticleLocalDatasource(
            sqlDelightArticleDatasource = get(),
        )
    }

    single<SqlDelightDatabase> { createDatabase(driverFactory = get()) }

    single<ArticleQueries> {
        val database: SqlDelightDatabase = get()

        database.articleQueries
    }

    single { SqlDelightArticleDatasource(queries = get()) }
}