package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.UserRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model.toDomainUpdatedAccount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStore.Intent.Modify
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStore.State.ModificationInProgress
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStore.State.Modified
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStore.State.UserLoading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStore.State.UserUnavailable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStore.State.UserUnderModification
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStore.State.UserUnderModification.ModificationFailure
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStoreProvider.Message.ModificationStarted
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStoreProvider.Message.SuccessfulModification
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ModifyAccountStoreProvider(private val storeFactory: StoreFactory) : KoinComponent {

    private val userRepository: UserRepository by inject()

    fun create(): ModifyAccountStore =
        object : ModifyAccountStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = MODIFY_ACCOUNT_STORE_NAME,
            initialState = userRepository.user.value?.let {
                UserUnderModification(it)
            } ?: UserUnavailable,
            executorFactory = { Executor(userRepository) },
            reducer = DefaultReducer,
        ) {}

    private class Executor(private val repository: UserRepository) :
        CoroutineExecutor<Intent, Nothing, State, Message, Nothing>() {

        override fun executeIntent(intent: Intent) {
            when {
                intent is Modify && state() is UserUnderModification -> {
                    dispatch(ModificationStarted)

                    val currentPassword = try {
                        Password(intent.currentPassword)
                    } catch (t: Throwable) {
                        dispatchCurrentPasswordInputError(t.message)

                        return
                    }

                    val updatedUser = try {
                        intent.updatedAccount.toDomainUpdatedAccount()
                    } catch (t: Throwable) {
                        dispatchUpdatedUserInputError(t.message)

                        return
                    }

                    scope.launch {
                        try {
                            repository.updateAccount(
                                currentPassword,
                                updatedUser,
                            )

                            dispatch(SuccessfulModification)
                        } catch (t: Throwable) {
                            dispatchModificationError(t.message)
                        }
                    }
                }

                intent is Intent.ReloadUser && state() is UserUnavailable -> {
                    dispatch(Message.UserReloadingStarted)
                    scope.launch {
                        try {
                            repository.loadAccount()

                            dispatch(
                                repository.user.value?.let {
                                    Message.UserLoadingSuccessful(it)
                                } ?: Message.UserUnavailable
                            )
                        } catch (t: Throwable) {
                            dispatch(Message.UserUnavailable)
                        }
                    }
                }
            }
        }

        private fun dispatchCurrentPasswordInputError(errorMessage: String?) {
            dispatch(
                Message.ModificationFailed(
                    when (errorMessage) {
                        Password.TOO_LONG_ERROR_MESSAGE -> {
                            ModificationFailure.CURRENT_PASSWORD_IS_TOO_LONG
                        }

                        Password.TOO_SHORT_ERROR_MESSAGE -> {
                            ModificationFailure.CURRENT_PASSWORD_IS_TOO_SHORT
                        }

                        else -> {
                            ModificationFailure.GENERAL_ERROR
                        }
                    }
                )
            )
        }

        private fun dispatchUpdatedUserInputError(errorMessage: String?) {
            dispatch(
                Message.ModificationFailed(
                    when (errorMessage) {
                        DateOfBirth.TOO_EARLY_DATE_ERROR_MESSAGE -> {
                            ModificationFailure.DATE_OF_BIRTH_TOO_EARLY
                        }

                        DateOfBirth.TOO_LATE_DATE_ERROR_MESSAGE -> {
                            ModificationFailure.DATE_OF_BIRTH_TOO_LATE
                        }

                        Email.TOO_LONG_ERROR_MESSAGE -> {
                            ModificationFailure.EMAIL_IS_TOO_LONG
                        }

                        in Email.ERROR_MESSAGES -> {
                            ModificationFailure.EMAIL_IS_INVALID
                        }

                        FirstName.INVALID_CHARACTER_ERROR_MESSAGE -> {
                            ModificationFailure.FIRST_NAME_CONTAINS_INVALID_CHARACTER
                        }

                        FirstName.TOO_LONG_ERROR_MESSAGE -> {
                            ModificationFailure.FIRST_NAME_IS_TOO_LONG
                        }

                        FirstName.TOO_SHORT_ERROR_MESSAGE -> {
                            ModificationFailure.FIRST_NAME_IS_TOO_SHORT
                        }

                        LastName.INVALID_CHARACTER_ERROR_MESSAGE -> {
                            ModificationFailure.LAST_NAME_CONTAINS_INVALID_CHARACTER
                        }

                        LastName.TOO_LONG_ERROR_MESSAGE -> {
                            ModificationFailure.LAST_NAME_IS_TOO_LONG
                        }

                        LastName.TOO_SHORT_ERROR_MESSAGE -> {
                            ModificationFailure.LAST_NAME_IS_TOO_SHORT
                        }

                        Password.TOO_LONG_ERROR_MESSAGE -> {
                            ModificationFailure.PASSWORD_IS_TOO_LONG
                        }

                        Password.TOO_SHORT_ERROR_MESSAGE -> {
                            ModificationFailure.PASSWORD_IS_TOO_SHORT
                        }

                        Username.INVALID_CHARACTER_ERROR_MESSAGE -> {
                            ModificationFailure.USERNAME_CONTAINS_INVALID_CHARACTER
                        }

                        Username.TOO_LONG_ERROR_MESSAGE -> {
                            ModificationFailure.USERNAME_IS_TOO_LONG
                        }

                        Username.TOO_SHORT_ERROR_MESSAGE -> {
                            ModificationFailure.USERNAME_IS_TOO_SHORT
                        }

                        else -> {
                            ModificationFailure.GENERAL_ERROR
                        }
                    }
                )
            )
        }

        private fun dispatchModificationError(errorMessage: String?) {
            dispatch(
                Message.ModificationFailed(
                    when (errorMessage) {
                        UserRepository.ERROR_MESSAGE_EMAIL_ALREADY_TAKEN -> {
                            ModificationFailure.EMAIL_IS_ALREADY_TAKEN
                        }

                        UserRepository.ERROR_MESSAGE_INVALID_CREDENTIALS -> {
                            ModificationFailure.CURRENT_PASSWORD_IS_WRONG
                        }

                        UserRepository.ERROR_MESSAGE_USERNAME_ALREADY_TAKEN -> {
                            ModificationFailure.USERNAME_IS_ALREADY_TAKEN
                        }

                        else -> {
                            ModificationFailure.GENERAL_ERROR
                        }
                    }
                )
            )
        }
    }

    private sealed interface Message {

        data class UserLoadingSuccessful(val user: User) : Message

        data object UserUnavailable : Message

        data object UserReloadingStarted : Message

        data object ModificationStarted : Message

        data object SuccessfulModification : Message

        data class ModificationFailed(val failure: ModificationFailure) : Message
    }

    private object DefaultReducer : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State = when {
            msg is Message.ModificationFailed && this is ModificationInProgress -> {
                UserUnderModification(currentUser, msg.failure)
            }

            msg is ModificationStarted && this is UserUnderModification -> {
                ModificationInProgress(currentUser)
            }

            msg is SuccessfulModification && this is ModificationInProgress -> {
                Modified
            }

            msg is Message.UserLoadingSuccessful && this is UserLoading -> {
                UserUnderModification(msg.user)
            }

            msg is Message.UserReloadingStarted && this is UserUnavailable -> {
                UserLoading
            }

            msg is Message.UserUnavailable && this is UserLoading -> {
                UserUnavailable
            }

            else -> {
                this
            }
        }
    }

    companion object {

        private const val MODIFY_ACCOUNT_STORE_NAME = "MODIFY_ACCOUNT_STORE_NAME"
    }
}