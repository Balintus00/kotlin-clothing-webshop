package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.UserRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ProfileStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ProfileStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ProfileStore.State.ProfileLoaded
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ProfileStore.State.ProfileLoading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ProfileStore.State.ProfileLoadingFailed
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ProfileStoreProvider(private val storeFactory: StoreFactory) : KoinComponent {

    private val userRepository: UserRepository by inject()

    fun create(): ProfileStore =
        object : ProfileStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = STORE_NAME,
            initialState = ProfileLoading,
            bootstrapper = SimpleBootstrapper<Action>(Action.LoadProfile),
            executorFactory = { Executor(userRepository) },
            reducer = DefaultReducer,
        ) {}

    private sealed interface Action {

        data object LoadProfile : Action
    }

    private class Executor(val repository: UserRepository) :
        CoroutineExecutor<Intent, Action, State, Message, Nothing>() {

        override fun executeAction(action: Action) {
            if (action is Action.LoadProfile && state() == ProfileLoading) {

            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {

                is Intent.RetryProfileLoading -> {
                    dispatch(Message.StartedProfileReloading)

                    scope.launch {
                        try {
                            repository.loadAccount()

                            dispatch(
                                repository.user.value?.let {
                                    Message.SuccessfulProfileReloading(it)
                                } ?: Message.FailedProfileReloading
                            )

                        } catch (t: Throwable) {
                            dispatch(Message.FailedProfileReloading)
                        }
                    }
                }
            }
        }
    }

    private sealed interface Message {

        data object StartedProfileReloading : Message

        data class SuccessfulProfileReloading(val user: User) : Message

        data object FailedProfileReloading : Message
    }

    private object DefaultReducer : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State = when {
            msg is Message.StartedProfileReloading && this is ProfileLoadingFailed -> {
                ProfileLoading
            }

            msg is Message.SuccessfulProfileReloading && this is ProfileLoading -> {
                ProfileLoaded(msg.user)
            }

            msg is Message.FailedProfileReloading && this is ProfileLoading -> {
                ProfileLoadingFailed
            }

            else -> this
        }
    }

    companion object {

        private const val STORE_NAME = "PROFILE_STORE_NAME"
    }
}