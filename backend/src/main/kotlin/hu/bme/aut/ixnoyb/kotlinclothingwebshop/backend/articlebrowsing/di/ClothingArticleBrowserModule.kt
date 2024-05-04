package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.di

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.service.ClothingWebshopService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.service.DefaultClothingWebshopService
import org.koin.dsl.module

val clothingArticleBrowserModule = module {
    single<ClothingWebshopService> { DefaultClothingWebshopService(databaseConnectionFactory =  get()) }
}