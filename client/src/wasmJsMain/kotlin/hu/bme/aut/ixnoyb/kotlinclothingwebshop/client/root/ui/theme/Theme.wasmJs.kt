package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal actual fun getColorScheme(): ColorScheme = if (isSystemInDarkTheme()) {
    darkScheme
} else {
    lightScheme
}

@Composable
internal actual fun ApplyPlatformSpecificThemeSettings(colorScheme: ColorScheme) {
    // No-op
}
@Composable
internal actual fun ApplyPlatformSpecificCompositionLocalSettings(content: @Composable () -> Unit) {
    content()
}