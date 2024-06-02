package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.UserRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStore.State.Authenticated
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStore.State.Idle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStore.State.Idle.LoginFailure
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStore.State.LoginInProgress
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStoreProvider.Message.LoginFailed
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStoreProvider.Message.LoginStarted
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStoreProvider.Message.SuccessfulLogin
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class LoginStoreProvider(private val storeFactory: StoreFactory) : KoinComponent {

    private val userRepository: UserRepository by inject()

    fun create(): LoginStore =
        object : LoginStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = LOGIN_STORE_NAME,
            initialState = Idle(),
            executorFactory = { Executor(userRepository) },
            reducer = DefaultReducer,
        ) {}

    private class Executor(
        private val userRepository: UserRepository,
    ) : CoroutineExecutor<Intent, Nothing, State, Message, Nothing>() {

        override fun executeIntent(intent: Intent) {
            if (intent is Intent.Login && state() is Idle) {
                dispatch(LoginStarted)

                scope.launch {
                    try {
                        userRepository.logIn(Email(intent.email), Password(intent.password))

                        dispatch(SuccessfulLogin)
                    } catch (t: Throwable) {
                        dispatch(
                            LoginFailed(
                                when (t.message) {
                                    Email.TOO_LONG_ERROR_MESSAGE -> {
                                        LoginFailure.TOO_LONG_EMAIL
                                    }

                                    in Email.ERROR_MESSAGES -> {
                                        LoginFailure.INVALID_EMAIL_ADDRESS
                                    }

                                    Password.TOO_SHORT_ERROR_MESSAGE -> {
                                        LoginFailure.TOO_SHORT_PASSWORD
                                    }

                                    Password.TOO_LONG_ERROR_MESSAGE -> {
                                        LoginFailure.TOO_LONG_PASSWORD
                                    }

                                    UserRepository.ERROR_MESSAGE_INVALID_CREDENTIALS -> {
                                        LoginFailure.INVALID_CREDENTIALS
                                    }

                                    else -> {
                                        LoginFailure.GENERAL_ERROR
                                    }
                                }

                            )
                        )
                    }
                }
            }
        }
    }

    private sealed interface Message {

        data object LoginStarted : Message

        data object SuccessfulLogin : Message

        data class LoginFailed(val failure: LoginFailure) : Message
    }

    private object DefaultReducer : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State = when (msg) {
            is LoginFailed -> Idle(msg.failure)
            is LoginStarted -> LoginInProgress
            is SuccessfulLogin -> Authenticated
        }
    }

    companion object {

        private const val LOGIN_STORE_NAME = "LoginStore"
    }
}