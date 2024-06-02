package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.MultiplatformUserPersistentSecureDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserPersistentSecureDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

internal actual val platformSpecificUserManagementModule: Module = module {
    single<Settings>(named(MULTIPLATFORM_PERSISTENT_DATASOURCE_NAME)) {
        NSUserDefaultsSettings(NSUserDefaults())
    }

    single<UserPersistentSecureDatasource> {
        MultiplatformUserPersistentSecureDatasource(
            coroutineScope = CoroutineScope(Dispatchers.Default),
        )
    }
}