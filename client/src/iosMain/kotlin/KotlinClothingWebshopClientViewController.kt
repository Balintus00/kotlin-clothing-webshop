import androidx.compose.ui.window.ComposeUIViewController
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.UserInterface
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.viewlogic.RootComponent

@Suppress("FunctionName", "unused") // Public API for iOS project
fun KotlinClothingWebshopClientViewController(component: RootComponent) = ComposeUIViewController {
    UserInterface(component)
}