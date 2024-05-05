package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.UserInterface
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.viewlogic.DefaultRootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()   // TODO
        super.onCreate(savedInstanceState)

        val rootComponent = DefaultRootComponent(
            componentContext = defaultComponentContext(),
            storeFactory = LoggingStoreFactory(DefaultStoreFactory()),
        )

        setContent {
            UserInterface(rootComponent)
        }
    }
}