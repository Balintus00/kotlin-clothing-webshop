package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.UserRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ExitStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ExitStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ExitStore.State.Exited
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ExitStore.State.Exiting
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ExitStore.State.Idle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ExitStoreProvider(private val storeFactory: StoreFactory) : KoinComponent {

    private val userRepository: UserRepository by inject()

    fun create(): ExitStore =
        object : ExitStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = STORE_NAME,
            initialState = Idle(),
            executorFactory = { Executor(userRepository) },
            reducer = DefaultReducer,
        ) {}

    private class Executor(private val repository: UserRepository) :
        CoroutineExecutor<Intent, Nothing, State, Message, Nothing>() {

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.DeleteAccount -> {
                    if (state() is Idle) {
                        dispatch(Message.ExitingStarted)

                        scope.launch {
                            try {
                                repository.deleteAccount(Password(intent.password))

                                dispatch(Message.SuccessfulExit)
                            } catch (t: Throwable) {
                                dispatch(
                                    Message.FailedDeletion(
                                        when (t.message) {
                                            Password.TOO_LONG_ERROR_MESSAGE -> {
                                                Idle.DeleteFailure.PASSWORD_TOO_LONG
                                            }

                                            Password.TOO_SHORT_ERROR_MESSAGE -> {
                                                Idle.DeleteFailure.PASSWORD_TOO_SHORT
                                            }

                                            UserRepository.ERROR_MESSAGE_INVALID_CREDENTIALS -> {
                                                Idle.DeleteFailure.INVALID_PASSWORD
                                            }

                                            else -> {
                                                Idle.DeleteFailure.GENERAL_ERROR
                                            }
                                        }
                                    )
                                )
                            }
                        }
                    }
                }

                is Intent.Logout -> {
                    dispatch(Message.ExitingStarted)

                    scope.launch {
                        try {
                            repository.logOut()

                            dispatch(Message.SuccessfulExit)
                        } catch (t: Throwable) {
                            dispatch(Message.FailedDeletion(Idle.DeleteFailure.GENERAL_ERROR))
                        }
                    }
                }
            }
        }
    }

    private sealed interface Message {

        data object ExitingStarted : Message

        data object SuccessfulExit : Message

        data class FailedDeletion(val exitFailure: Idle.DeleteFailure) : Message

        data object FailedLogout : Message
    }

    private object DefaultReducer : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State = when {
            msg is Message.ExitingStarted && this is Idle -> {
                Exiting
            }

            msg is Message.SuccessfulExit && this is Exiting -> {
                Exited
            }

            msg is Message.FailedDeletion && this is Exiting -> {
                Idle(recentDeleteFailure =  msg.exitFailure)
            }

            msg is Message.FailedLogout && this is Exiting -> {
                Idle(isLogoutFailedRecently = true)
            }

            else -> this
        }
    }

    companion object {

        private const val STORE_NAME = "EXIT_STORE_NAME"
    }
}