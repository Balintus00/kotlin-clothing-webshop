import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ui.KotlinClothingWebshopRootScreen

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("Kotlin Clothing Webshop", canvasElementId = "ComposeTarget") {
        KotlinClothingWebshopRootScreen()
    }
}
