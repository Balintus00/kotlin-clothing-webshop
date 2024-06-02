package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.di

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.KomapperArticleDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.KotlinDLOnnxRecommendationDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.ArticleService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.DefaultArticleService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.datasource.ArticleDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.datasource.RecommendationDatasource
import org.koin.dsl.module

val clothingArticleBrowserModule = module {
    single<ArticleService> {
        DefaultArticleService(
            articleDatasource = get(),
            recommendationDatasource = get(),
            userDatasource = get(),
        )
    }

    single<ArticleDatasource> {
        KomapperArticleDatasource(
            database = get(),
            databaseConnectionFactory = get(),
        )
    }

    single<RecommendationDatasource> { KotlinDLOnnxRecommendationDatasource() }
}