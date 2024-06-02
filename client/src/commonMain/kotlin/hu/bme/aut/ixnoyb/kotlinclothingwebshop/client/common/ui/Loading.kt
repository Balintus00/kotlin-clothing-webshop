package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun LoadingSection(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        CircularProgressIndicator()
    }
}

internal val LOADING_BUTTON_CONTENT_SIZE = (
        ButtonDefaults.MinHeight
                - ButtonDefaults.ContentPadding.calculateTopPadding()
                - ButtonDefaults.ContentPadding.calculateBottomPadding()
        )