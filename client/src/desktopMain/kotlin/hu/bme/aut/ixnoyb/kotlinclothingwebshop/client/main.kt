package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.utils.setMainThreadId
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.di.appModule
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.RootScreen
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.viewlogic.DefaultRootComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        logger(
            KermitKoinLogger(Logger.withTag("koin"))
        )
        modules(appModule)
    }

    val lifecycle = LifecycleRegistry()

    val rootComponent = runBlocking(Dispatchers.Main) {
        setMainThreadId(Thread.currentThread().id)

        DefaultRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            storeFactory = LoggingStoreFactory(DefaultStoreFactory()),
        )
    }

    application {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)

        Window(
            icon = painterResource("icon.png"),
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Kotlin Clothing Webshop",
        ) {
            RootScreen(rootComponent)
        }
    }
}