package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.UserRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model.toUserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStore.Intent.Register
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStore.State.Idle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStore.State.Idle.RegistrationFailure
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStore.State.Registered
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStore.State.RegistrationInProgress
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStoreProvider.Message.RegistrationFailed
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStoreProvider.Message.RegistrationStarted
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStoreProvider.Message.SuccessfulRegistration
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class RegistrationStoreProvider(private val storeFactory: StoreFactory) : KoinComponent {

    private val userRepository: UserRepository by inject()

    fun create(): RegistrationStore =
        object : RegistrationStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = REGISTRATION_STORE_NAME,
            initialState = Idle(),
            executorFactory = { Executor(userRepository) },
            reducer = DefaultReducer,
        ) {}

    private class Executor(private val repository: UserRepository) :
        CoroutineExecutor<Intent, Nothing, State, Message, Nothing>() {

        override fun executeIntent(intent: Intent) {
            if (intent is Register && state() is Idle) {
                dispatch(RegistrationStarted)

                scope.launch {
                    try {
                        repository.register(intent.userCandidate.toUserCandidate())

                        dispatch(SuccessfulRegistration)
                    } catch (t: Throwable) {
                        dispatch(
                            RegistrationFailed(
                                when (t.message) {
                                    DateOfBirth.TOO_EARLY_DATE_ERROR_MESSAGE -> {
                                        RegistrationFailure.DATE_OF_BIRTH_TOO_EARLY
                                    }

                                    DateOfBirth.TOO_LATE_DATE_ERROR_MESSAGE -> {
                                        RegistrationFailure.DATE_OF_BIRTH_TOO_LATE
                                    }

                                    Email.TOO_LONG_ERROR_MESSAGE -> {
                                        RegistrationFailure.EMAIL_IS_TOO_LONG
                                    }

                                    in Email.ERROR_MESSAGES -> {
                                        RegistrationFailure.EMAIL_IS_INVALID
                                    }

                                    FirstName.INVALID_CHARACTER_ERROR_MESSAGE -> {
                                        RegistrationFailure.FIRST_NAME_CONTAINS_INVALID_CHARACTER
                                    }

                                    FirstName.TOO_LONG_ERROR_MESSAGE -> {
                                        RegistrationFailure.FIRST_NAME_IS_TOO_LONG
                                    }

                                    FirstName.TOO_SHORT_ERROR_MESSAGE -> {
                                        RegistrationFailure.FIRST_NAME_IS_TOO_SHORT
                                    }

                                    LastName.INVALID_CHARACTER_ERROR_MESSAGE -> {
                                        RegistrationFailure.LAST_NAME_CONTAINS_INVALID_CHARACTER
                                    }

                                    LastName.TOO_LONG_ERROR_MESSAGE -> {
                                        RegistrationFailure.LAST_NAME_IS_TOO_LONG
                                    }

                                    LastName.TOO_SHORT_ERROR_MESSAGE -> {
                                        RegistrationFailure.LAST_NAME_IS_TOO_SHORT
                                    }

                                    Password.TOO_LONG_ERROR_MESSAGE -> {
                                        RegistrationFailure.PASSWORD_IS_TOO_LONG
                                    }

                                    Password.TOO_SHORT_ERROR_MESSAGE -> {
                                        RegistrationFailure.PASSWORD_IS_TOO_SHORT
                                    }

                                    Username.INVALID_CHARACTER_ERROR_MESSAGE -> {
                                        RegistrationFailure.USERNAME_CONTAINS_INVALID_CHARACTER
                                    }

                                    Username.TOO_LONG_ERROR_MESSAGE -> {
                                        RegistrationFailure.USERNAME_IS_TOO_LONG
                                    }

                                    Username.TOO_SHORT_ERROR_MESSAGE -> {
                                        RegistrationFailure.USERNAME_IS_TOO_SHORT
                                    }

                                    UserRepository.ERROR_MESSAGE_EMAIL_ALREADY_TAKEN -> {
                                        RegistrationFailure.EMAIL_IS_ALREADY_TAKEN
                                    }

                                    UserRepository.ERROR_MESSAGE_USERNAME_ALREADY_TAKEN -> {
                                        RegistrationFailure.USERNAME_IS_ALREADY_TAKEN
                                    }

                                    else -> {
                                        RegistrationFailure.GENERAL_ERROR
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

        data object RegistrationStarted : Message

        data object SuccessfulRegistration : Message

        data class RegistrationFailed(val failure: RegistrationFailure) : Message
    }

    private object DefaultReducer : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State = when (msg) {
            is RegistrationFailed -> Idle(msg.failure)
            is RegistrationStarted -> RegistrationInProgress
            is SuccessfulRegistration -> Registered
        }
    }

    companion object {

        private const val REGISTRATION_STORE_NAME = "RegistrationStore"
    }
}