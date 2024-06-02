package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

@Composable
internal actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    context: CoroutineContext,
): State<T> = this@collectAsStateWithLifecycle.collectAsStateWithLifecycle(context = context)