package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

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
    val scrollBarTheme = defaultScrollbarStyle().copy(
        unhoverColor = MaterialTheme.colorScheme.outline,
        hoverColor = MaterialTheme.colorScheme.secondary,
    )

    CompositionLocalProvider(LocalScrollbarStyle provides scrollBarTheme) {
        content()
    }
}