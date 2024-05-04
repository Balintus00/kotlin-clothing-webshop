package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.KotlinClothingWebshopRootScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KotlinClothingWebshopRootScreen()
        }
    }
}