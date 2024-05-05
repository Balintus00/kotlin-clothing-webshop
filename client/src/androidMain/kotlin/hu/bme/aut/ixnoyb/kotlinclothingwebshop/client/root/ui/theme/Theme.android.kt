package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun getColorScheme(): ColorScheme {
    val isDarkTheme = isSystemInDarkTheme()
    return  when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isDarkTheme -> {
            dynamicDarkColorScheme(LocalContext.current)
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            dynamicLightColorScheme(LocalContext.current)
        }
        isDarkTheme -> {
            darkScheme
        }
        else -> {
            lightScheme
        }
    }
}

@Composable
actual fun ApplyPlatformSpecificThemeSettings(colorScheme: ColorScheme) {
    val view = LocalView.current
    val isDarkTheme = isSystemInDarkTheme()
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDarkTheme
        }
    }
}

@Composable
actual fun ApplyPlatformSpecificCompositionLocalSettings(content: @Composable () -> Unit) {
    content()
}