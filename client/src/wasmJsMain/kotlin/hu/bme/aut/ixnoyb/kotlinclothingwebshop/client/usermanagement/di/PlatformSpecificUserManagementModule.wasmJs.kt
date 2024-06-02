package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.di

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.MultiplatformUserPersistentSecureDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserPersistentSecureDatasource
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val platformSpecificUserManagementModule: Module = module {
    single<Settings>(named(MULTIPLATFORM_PERSISTENT_DATASOURCE_NAME)) {
        StorageSettings()
    }

    single<UserPersistentSecureDatasource> {
        MultiplatformUserPersistentSecureDatasource()
    }
}