package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.store.create
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.UserRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.UserManagementRootStore.State
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class UserManagementRootStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val userRepository: UserRepository by inject()

    fun create(): UserManagementRootStore =
        object : UserManagementRootStore, Store<Nothing, State, Nothing> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(userRepository.isAuthenticated.value),
            executorFactory = { Executor(userRepository) },
            reducer = DefaultReducer,
        ) {}


    private class Executor(
        private val repository: UserRepository,
    ) : CoroutineExecutor<Nothing, Nothing, State, Message, Nothing>() {

        init {
            scope.launch {
                repository.isAuthenticated.collect {
                    dispatch(Message(isAuthenticated = it))
                }
            }
        }
    }

    private data class Message(val isAuthenticated: Boolean)

    private object DefaultReducer : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State = State(msg.isAuthenticated)
    }

    companion object {
        private const val STORE_NAME = "UserManagementRootStore"
    }
}