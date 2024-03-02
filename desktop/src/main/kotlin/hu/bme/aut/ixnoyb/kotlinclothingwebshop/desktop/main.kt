package hu.bme.aut.ixnoyb.kotlinclothingwebshop.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ui.KotlinClothingWebshopRootScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kotlin Clothing Webshop",
    ) {
        KotlinClothingWebshopRootScreen()
    }
}