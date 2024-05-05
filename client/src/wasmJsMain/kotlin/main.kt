import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.di.appModule
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.UserInterface
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.viewlogic.DefaultRootComponent
import kotlinx.browser.document
import org.koin.core.context.startKoin
import org.w3c.dom.Document

// TODO https://github.com/JetBrains/compose-multiplatform/issues/4639
@JsFun("(document) => document.visibilityState")
private external fun visibilityState(document: Document): String

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(appModule)
    }

    val lifecycle = LifecycleRegistry()

    val rootComponent = DefaultRootComponent(
        componentContext = DefaultComponentContext(lifecycle = lifecycle),
        storeFactory = LoggingStoreFactory(DefaultStoreFactory()),
    )

    lifecycle.attachToDocument()

    CanvasBasedWindow("Kotlin Clothing Webshop", canvasElementId = "ComposeTarget") {
        UserInterface(rootComponent)
    }
}


private fun LifecycleRegistry.attachToDocument() {
    fun onVisibilityChanged() {
        if (visibilityState(document) == DOCUMENT_VISIBILITY_STATE_VISIBLE) {
            resume()
        } else {
            stop()
        }
    }

    onVisibilityChanged()

    document.addEventListener(
        type = DOCUMENT_EVENT_TYPE_VISIBILITY_CHANGE,
        callback = { onVisibilityChanged() },
    )
}

private const val DOCUMENT_EVENT_TYPE_VISIBILITY_CHANGE = "visibilitychange"
private const val DOCUMENT_VISIBILITY_STATE_VISIBLE = "visible"