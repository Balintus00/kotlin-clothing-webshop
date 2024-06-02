package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

// Source: https://slack-chats.kotlinlang.org/t/13222387/hey-guys-any-way-to-use-collectasstatewithlifecycle-on-compo

@Composable
internal expect fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    context: CoroutineContext = EmptyCoroutineContext,
): State<T>