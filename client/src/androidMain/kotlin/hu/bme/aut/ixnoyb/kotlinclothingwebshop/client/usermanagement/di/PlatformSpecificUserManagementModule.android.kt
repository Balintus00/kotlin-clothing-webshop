package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.di

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.MultiplatformUserPersistentSecureDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserPersistentSecureDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val platformSpecificUserManagementModule: Module = module {

    single<Settings>(named(MULTIPLATFORM_PERSISTENT_DATASOURCE_NAME)) {
        val context: Context = get()

        val sharedPreferencesFileName =
            "hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.PREFERENCE_FILE_KEY"

        SharedPreferencesSettings(
            context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
        )
    }

    single<UserPersistentSecureDatasource> {
        MultiplatformUserPersistentSecureDatasource(
            context = get(),
            coroutineScope = CoroutineScope(Dispatchers.Default),
        )
    }
}