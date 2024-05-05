import androidx.compose.ui.window.ComposeUIViewController
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.KotlinClothingWebshopRootScreen

@Suppress("FunctionName", "unused") // Public API for iOS project
fun KotlinClothingWebshopClientViewController() = ComposeUIViewController {
    KotlinClothingWebshopRootScreen()
}