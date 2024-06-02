package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.di

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource.DriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val nonWasmPlatformSpecificArticleBrowsingModule: Module = module {
    single { DriverFactory() }
}