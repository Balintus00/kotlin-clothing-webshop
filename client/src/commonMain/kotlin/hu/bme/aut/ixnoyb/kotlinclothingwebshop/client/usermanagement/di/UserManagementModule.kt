package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.di

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.KtorUserRemoteDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.MultiplatformSettingsUserPersistentDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.DefaultUserRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.UserRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserPersistentDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserRemoteDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val userManagementModule = module {
    includes(platformSpecificUserManagementModule)

    single<UserRepository> {
        DefaultUserRepository(
            userSecurePersistentDatasource = get(),
            userRemoteDatasource = get(),
            userPersistentDatasource = get(),
        )
    }

    single<UserRemoteDatasource> { KtorUserRemoteDatasource(httpClient = get()) }

    single<UserPersistentDatasource> {
        MultiplatformSettingsUserPersistentDatasource(
            settings = get(named(MULTIPLATFORM_PERSISTENT_DATASOURCE_NAME)),
        )
    }
}

internal const val MULTIPLATFORM_PERSISTENT_DATASOURCE_NAME =
    "MULTIPLATFORM_PERSISTENT_DATASOURCE_NAME"