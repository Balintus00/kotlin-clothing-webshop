package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.di

import org.koin.core.module.Module

internal expect fun getWasmLimitedArticleBrowsingModule(
    persistentArticleDatasourceName: String,
): Module