package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ui.KotlinClothingWebshopRootScreen

@Preview(device = Devices.PIXEL_2)
@Composable
fun KotlinClothingWebshopRootScreenPreview() {
    KotlinClothingWebshopRootScreen()
}