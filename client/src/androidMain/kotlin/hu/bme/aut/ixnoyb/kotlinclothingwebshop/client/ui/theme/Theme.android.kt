@file:JvmName("ThemeKtAndroid") // https://youtrack.jetbrains.com/issue/KT-21186

package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ui.theme

import android.app.Activity
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun ApplyPlatformSpecificThemeSettings(colorScheme: ColorScheme, isDarkTheme: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(
                (view.context as Activity).window,
                view
            ).isAppearanceLightStatusBars = isDarkTheme
        }
    }
}