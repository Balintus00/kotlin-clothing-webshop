package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.KotlinClothingWebshopRootScreen

fun main() = application {
    Window(
        icon = painterResource("icon.png"),
        onCloseRequest = ::exitApplication,
        title = "Kotlin Clothing Webshop",
    ) {
        KotlinClothingWebshopRootScreen()
    }
}