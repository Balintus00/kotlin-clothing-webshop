package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.core.store.Store

internal fun <T : Store<*, *, *>> InstanceKeeper.createAndGetStore(key: Any, factory: () -> T): T =
    getOrCreate(key = key) {
        StoreInstance(factory())
    }.store

internal inline fun <reified T : Store<*, *, *>> InstanceKeeper.createAndGetStore(noinline factory: () -> T): T =
    createAndGetStore(key = T::class, factory = factory)

internal inline fun <reified T : Store<*, *, *>> InstanceKeeper.getStore(): T? =
    (get(T::class) as? StoreInstance<*>)?.store as? T

internal inline fun <reified T : Store<*, *, *>> InstanceKeeper.removeStore() =
    remove(T::class)

internal class StoreInstance<out T : Store<*, *, *>>(
    val store: T
) : InstanceKeeper.Instance {
    override fun onDestroy() {
        store.dispose()
    }
}