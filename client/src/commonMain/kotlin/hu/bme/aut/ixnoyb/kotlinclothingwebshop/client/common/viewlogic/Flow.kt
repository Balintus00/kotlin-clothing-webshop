package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow

// Source: https://github.com/arkivanov/Decompose/issues/516#issuecomment-1791022312
@Suppress("unused")
internal fun <T : Any> Value<T>.asFlow(): Flow<T> = channelFlow {
    val cancellation = subscribe(channel::trySend)
    awaitClose(cancellation::cancel)
}

// Source: https://slack-chats.kotlinlang.org/t/16225841/how-can-i-convert-value-to-flow
internal fun <T : Any> Value<T>.toStateFlow(): StateFlow<T> = ValueStateFlow(this)

private class ValueStateFlow<out T : Any>(private val source: Value<T>) : StateFlow<T> {

    override val value: T
        get() = source.value

    override val replayCache: List<T>
        get() = listOf(source.value)

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        val flow = MutableStateFlow(source.value)
        val disposable = source.subscribe { flow.value = it }

        try {
            flow.collect(collector)
        } finally {
            disposable.cancel()
        }
    }
}

// Source: https://gist.github.com/aartikov/a56cc94bb306e05b7b7927353910da08
internal val ComponentContext.componentScope: CoroutineScope get() {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    } else {
        scope.cancel()
    }

    return scope
}