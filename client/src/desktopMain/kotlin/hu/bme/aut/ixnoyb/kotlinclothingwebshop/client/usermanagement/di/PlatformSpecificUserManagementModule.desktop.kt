package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.di

import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.MultiplatformUserPersistentSecureDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserPersistentSecureDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.Properties

internal actual val platformSpecificUserManagementModule: Module = module {
    single<Settings>(named(MULTIPLATFORM_PERSISTENT_DATASOURCE_NAME)) {
        PropertiesSettings(Properties())
    }

    single<UserPersistentSecureDatasource> {
        MultiplatformUserPersistentSecureDatasource(
            coroutineScope = CoroutineScope(Dispatchers.Default),
        )
    }
}