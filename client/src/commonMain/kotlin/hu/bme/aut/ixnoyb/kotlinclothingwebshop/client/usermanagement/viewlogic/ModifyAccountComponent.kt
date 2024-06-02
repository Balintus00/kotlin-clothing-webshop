package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.createAndGetStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.getViewStateStateFlow
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.ModifyAccountComponent.ViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model.Profile
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model.UpdatedAccount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model.toProfile
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStore.State.UserUnderModification.ModificationFailure
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ModifyAccountStoreProvider
import kotlinx.coroutines.flow.StateFlow

interface ModifyAccountComponent {

    val viewState: StateFlow<ViewState>

    sealed interface ViewState {

        fun navigateBack()

        interface InitialLoading : ViewState

        interface InitialLoadingFailed : ViewState {

            fun retry()
        }

        interface UserUnderModification : ViewState {

            val currentProfile: Profile

            val errorMessage: String?

            fun modifyAccount(currentPassword: String, updatedAccount: UpdatedAccount)

            companion object {

                const val DATE_OF_BIRTH_EARLIEST_ALLOWED_YEAR =
                    ModifyAccountStore.State.DATE_OF_BIRTH_EARLIEST_ALLOWED_YEAR
                const val DATE_OF_BIRTH_EARLIEST_ALLOWED_MONTH =
                    ModifyAccountStore.State.DATE_OF_BIRTH_EARLIEST_ALLOWED_MONTH
                const val DATE_OF_BIRTH_EARLIEST_ALLOWED_DAY =
                    ModifyAccountStore.State.DATE_OF_BIRTH_EARLIEST_ALLOWED_DAY
                const val DATE_OF_BIRTH_LATEST_ALLOWED_YEAR_DIFFERENCE =
                    ModifyAccountStore.State.DATE_OF_BIRTH_LATEST_ALLOWED_YEAR_DIFFERENCE
                const val EMAIL_MAXIMUM_LENGTH = ModifyAccountStore.State.EMAIL_MAXIMUM_LENGTH
                const val FIRST_NAME_MAXIMUM_LENGTH =
                    ModifyAccountStore.State.FIRST_NAME_MAXIMUM_LENGTH
                const val LAST_NAME_MAXIMUM_LENGTH =
                    ModifyAccountStore.State.LAST_NAME_MAXIMUM_LENGTH
                const val PASSWORD_MAXIMUM_LENGTH = ModifyAccountStore.State.PASSWORD_MAXIMUM_LENGTH
                const val USERNAME_MAXIMUM_LENGTH = ModifyAccountStore.State.USERNAME_MAXIMUM_LENGTH

                const val ERROR_MESSAGE_CURRENT_PASSWORD_IS_TOO_LONG =
                    "ERROR_MESSAGE_CURRENT_PASSWORD_IS_TOO_LONG"
                const val ERROR_MESSAGE_CURRENT_PASSWORD_IS_TOO_SHORT =
                    "ERROR_MESSAGE_CURRENT_PASSWORD_IS_TOO_SHORT"
                const val ERROR_MESSAGE_CURRENT_PASSWORD_IS_WRONG =
                    "ERROR_MESSAGE_CURRENT_PASSWORD_IS_WRONG"
                const val ERROR_MESSAGE_DATE_OF_BIRTH_TOO_LATE =
                    "ERROR_MESSAGE_DATE_OF_BIRTH_TOO_LATE"
                const val ERROR_MESSAGE_DATE_OF_BIRTH_TOO_EARLY =
                    "ERROR_MESSAGE_DATE_OF_BIRTH_TOO_EARLY"
                const val ERROR_MESSAGE_EMAIL_IS_ALREADY_TAKEN =
                    "ERROR_MESSAGE_EMAIL_IS_ALREADY_TAKEN"
                const val ERROR_MESSAGE_EMAIL_IS_INVALID = "ERROR_MESSAGE_EMAIL_IS_INVALID"
                const val ERROR_MESSAGE_EMAIL_IS_TOO_LONG = "ERROR_MESSAGE_EMAIL_IS_TOO_LONG"
                const val ERROR_MESSAGE_GENERAL_ERROR = "ERROR_MESSAGE_GENERAL_ERROR"
                const val ERROR_MESSAGE_FIRST_NAME_CONTAINS_INVALID_CHARACTER =
                    "ERROR_MESSAGE_FIRST_NAME_CONTAINS_INVALID_CHARACTER"
                const val ERROR_MESSAGE_FIRST_NAME_IS_TOO_LONG =
                    "ERROR_MESSAGE_FIRST_NAME_IS_TOO_LONG"
                const val ERROR_MESSAGE_FIRST_NAME_IS_TOO_SHORT =
                    "ERROR_MESSAGE_FIRST_NAME_IS_TOO_SHORT"
                const val ERROR_MESSAGE_LAST_NAME_CONTAINS_INVALID_CHARACTER =
                    "ERROR_MESSAGE_LAST_NAME_CONTAINS_INVALID_CHARACTER"
                const val ERROR_MESSAGE_LAST_NAME_IS_TOO_SHORT =
                    "ERROR_MESSAGE_LAST_NAME_IS_TOO_SHORT"
                const val ERROR_MESSAGE_LAST_NAME_IS_TOO_LONG =
                    "ERROR_MESSAGE_LAST_NAME_IS_TOO_LONG"
                const val ERROR_MESSAGE_PASSWORD_IS_TOO_LONG = "ERROR_MESSAGE_PASSWORD_IS_TOO_LONG"
                const val ERROR_MESSAGE_PASSWORD_IS_TOO_SHORT =
                    "ERROR_MESSAGE_PASSWORD_IS_TOO_SHORT"
                const val ERROR_MESSAGE_USERNAME_CONTAINS_INVALID_CHARACTER =
                    "ERROR_MESSAGE_USERNAME_CONTAINS_INVALID_CHARACTER"
                const val ERROR_MESSAGE_USERNAME_IS_ALREADY_TAKEN =
                    "ERROR_MESSAGE_USERNAME_IS_ALREADY_TAKEN"
                const val ERROR_MESSAGE_USERNAME_IS_TOO_LONG = "ERROR_MESSAGE_USERNAME_IS_TOO_LONG"
                const val ERROR_MESSAGE_USERNAME_IS_TOO_SHORT =
                    "ERROR_MESSAGE_USERNAME_IS_TOO_SHORT"
            }
        }

        interface ModificationInProgress : ViewState
    }
}

internal class DefaultModifyAccountComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val navigateBackAction: () -> Unit = {},
    private val onSuccessfulAccountModificationAction: () -> Unit = {},
) : ModifyAccountComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.createAndGetStore {
        ModifyAccountStoreProvider(storeFactory).create()
    }

    override val viewState: StateFlow<ViewState> = store.getViewStateStateFlow(
        component = this,
        mapper = { it.toViewState() },
    )

    private fun State.toViewState(): ViewState =
        when (this) {
            State.UserLoading -> object : ViewState.InitialLoading {

                override fun navigateBack() {
                    navigateBackAction()
                }
            }

            State.UserUnavailable -> object : ViewState.InitialLoadingFailed {
                override fun navigateBack() {
                    navigateBackAction()
                }

                override fun retry() {
                    store.accept(ModifyAccountStore.Intent.ReloadUser)
                }
            }

            is State.UserUnderModification -> object : ViewState.UserUnderModification {

                override val currentProfile: Profile = currentUser.toProfile()

                override val errorMessage: String? = previousFailure?.toErrorMessage()

                override fun modifyAccount(
                    currentPassword: String,
                    updatedAccount: UpdatedAccount,
                ) {
                    store.accept(ModifyAccountStore.Intent.Modify(currentPassword, updatedAccount))
                }

                override fun navigateBack() {
                    navigateBackAction()
                }
            }

            is State.ModificationInProgress -> {
                createModificationInProgressViewState()
            }

            is State.Modified -> {
                onSuccessfulAccountModificationAction()

                createModificationInProgressViewState()
            }
        }

    private fun ModificationFailure.toErrorMessage(): String = when (this) {
        ModificationFailure.CURRENT_PASSWORD_IS_WRONG -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_CURRENT_PASSWORD_IS_WRONG
        }

        ModificationFailure.CURRENT_PASSWORD_IS_TOO_LONG -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_CURRENT_PASSWORD_IS_TOO_LONG
        }

        ModificationFailure.CURRENT_PASSWORD_IS_TOO_SHORT -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_CURRENT_PASSWORD_IS_TOO_SHORT
        }

        ModificationFailure.DATE_OF_BIRTH_TOO_EARLY -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_DATE_OF_BIRTH_TOO_EARLY
        }

        ModificationFailure.DATE_OF_BIRTH_TOO_LATE -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_DATE_OF_BIRTH_TOO_LATE
        }

        ModificationFailure.EMAIL_IS_ALREADY_TAKEN -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_EMAIL_IS_ALREADY_TAKEN
        }

        ModificationFailure.EMAIL_IS_INVALID -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_EMAIL_IS_INVALID
        }

        ModificationFailure.EMAIL_IS_TOO_LONG -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_EMAIL_IS_TOO_LONG
        }

        ModificationFailure.GENERAL_ERROR -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_GENERAL_ERROR
        }

        ModificationFailure.FIRST_NAME_CONTAINS_INVALID_CHARACTER -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_FIRST_NAME_CONTAINS_INVALID_CHARACTER
        }

        ModificationFailure.FIRST_NAME_IS_TOO_LONG -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_FIRST_NAME_IS_TOO_LONG
        }

        ModificationFailure.FIRST_NAME_IS_TOO_SHORT -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_FIRST_NAME_IS_TOO_SHORT
        }

        ModificationFailure.LAST_NAME_CONTAINS_INVALID_CHARACTER -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_LAST_NAME_CONTAINS_INVALID_CHARACTER
        }

        ModificationFailure.LAST_NAME_IS_TOO_LONG -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_LAST_NAME_IS_TOO_LONG
        }

        ModificationFailure.LAST_NAME_IS_TOO_SHORT -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_LAST_NAME_IS_TOO_SHORT
        }

        ModificationFailure.PASSWORD_IS_TOO_LONG -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_PASSWORD_IS_TOO_LONG
        }

        ModificationFailure.PASSWORD_IS_TOO_SHORT -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_PASSWORD_IS_TOO_SHORT
        }

        ModificationFailure.USERNAME_CONTAINS_INVALID_CHARACTER -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_USERNAME_CONTAINS_INVALID_CHARACTER
        }

        ModificationFailure.USERNAME_IS_ALREADY_TAKEN -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_USERNAME_IS_ALREADY_TAKEN
        }

        ModificationFailure.USERNAME_IS_TOO_LONG -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_USERNAME_IS_TOO_LONG
        }

        ModificationFailure.USERNAME_IS_TOO_SHORT -> {
            ViewState.UserUnderModification.ERROR_MESSAGE_USERNAME_IS_TOO_SHORT
        }
    }

    private fun createModificationInProgressViewState(): ViewState.ModificationInProgress =
        object : ViewState.ModificationInProgress {

            override fun navigateBack() {
                navigateBackAction()
            }
        }
}