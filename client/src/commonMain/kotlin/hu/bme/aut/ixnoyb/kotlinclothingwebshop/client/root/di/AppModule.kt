package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.di

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.di.articleBrowsingModule
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.checkout.di.checkoutModule
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.di.userManagementModule
import org.koin.dsl.module

internal val appModule = module {
    includes(articleBrowsingModule, checkoutModule, userManagementModule)
}