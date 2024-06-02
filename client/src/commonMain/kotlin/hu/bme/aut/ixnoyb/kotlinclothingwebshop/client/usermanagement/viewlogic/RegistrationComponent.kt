package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.createAndGetStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.getViewStateStateFlow
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.RegistrationComponent.ViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model.Account
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStore.State.Idle.RegistrationFailure
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStoreProvider
import kotlinx.coroutines.flow.StateFlow

interface RegistrationComponent {

    val viewState: StateFlow<ViewState>

    sealed interface ViewState {

        fun navigateBack()

        interface Idle : ViewState {

            val errorMessage: String?

            fun register(account: Account)

            companion object {

                const val DATE_OF_BIRTH_EARLIEST_ALLOWED_YEAR =
                    RegistrationStore.State.DATE_OF_BIRTH_EARLIEST_ALLOWED_YEAR
                const val DATE_OF_BIRTH_EARLIEST_ALLOWED_MONTH =
                    RegistrationStore.State.DATE_OF_BIRTH_EARLIEST_ALLOWED_MONTH
                const val DATE_OF_BIRTH_EARLIEST_ALLOWED_DAY =
                    RegistrationStore.State.DATE_OF_BIRTH_EARLIEST_ALLOWED_DAY
                const val DATE_OF_BIRTH_LATEST_ALLOWED_YEAR_DIFFERENCE =
                    RegistrationStore.State.DATE_OF_BIRTH_LATEST_ALLOWED_YEAR_DIFFERENCE
                const val EMAIL_MAXIMUM_LENGTH = RegistrationStore.State.EMAIL_MAXIMUM_LENGTH
                const val FIRST_NAME_MAXIMUM_LENGTH =
                    RegistrationStore.State.FIRST_NAME_MAXIMUM_LENGTH
                const val LAST_NAME_MAXIMUM_LENGTH =
                    RegistrationStore.State.LAST_NAME_MAXIMUM_LENGTH
                const val PASSWORD_MAXIMUM_LENGTH = RegistrationStore.State.PASSWORD_MAXIMUM_LENGTH
                const val USERNAME_MAXIMUM_LENGTH = RegistrationStore.State.USERNAME_MAXIMUM_LENGTH

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

        interface RegistrationInProgress : ViewState
    }
}

internal class DefaultRegistrationComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val navigateBackAction: () -> Unit = {},
    private val onSuccessfulRegistrationAction: () -> Unit = {},
) : RegistrationComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.createAndGetStore {
        RegistrationStoreProvider(storeFactory).create()
    }

    override val viewState: StateFlow<ViewState> =
        store.getViewStateStateFlow(
            component = this,
            mapper = { it.toViewState() },
        )

    private fun State.toViewState(): ViewState = when (this) {
        is State.Idle -> object : ViewState.Idle {

            override val errorMessage: String? = previousFailure?.toErrorMessage()

            override fun navigateBack() {
                navigateBackAction()
            }


            override fun register(account: Account) {
                store.accept(RegistrationStore.Intent.Register(account))
            }
        }

        is State.Registered -> {
            onSuccessfulRegistrationAction()

            createRegistrationInProgressViewState()
        }

        is State.RegistrationInProgress -> {
            createRegistrationInProgressViewState()
        }
    }

    private fun RegistrationFailure.toErrorMessage(): String = when (this) {
        RegistrationFailure.DATE_OF_BIRTH_TOO_EARLY -> {
            ViewState.Idle.ERROR_MESSAGE_DATE_OF_BIRTH_TOO_EARLY
        }

        RegistrationFailure.DATE_OF_BIRTH_TOO_LATE -> {
            ViewState.Idle.ERROR_MESSAGE_DATE_OF_BIRTH_TOO_LATE
        }

        RegistrationFailure.EMAIL_IS_ALREADY_TAKEN -> {
            ViewState.Idle.ERROR_MESSAGE_EMAIL_IS_ALREADY_TAKEN
        }

        RegistrationFailure.EMAIL_IS_INVALID -> {
            ViewState.Idle.ERROR_MESSAGE_EMAIL_IS_INVALID
        }

        RegistrationFailure.EMAIL_IS_TOO_LONG -> {
            ViewState.Idle.ERROR_MESSAGE_EMAIL_IS_TOO_LONG
        }

        RegistrationFailure.GENERAL_ERROR -> {
            ViewState.Idle.ERROR_MESSAGE_GENERAL_ERROR
        }

        RegistrationFailure.FIRST_NAME_CONTAINS_INVALID_CHARACTER -> {
            ViewState.Idle.ERROR_MESSAGE_FIRST_NAME_CONTAINS_INVALID_CHARACTER
        }

        RegistrationFailure.FIRST_NAME_IS_TOO_LONG -> {
            ViewState.Idle.ERROR_MESSAGE_FIRST_NAME_IS_TOO_LONG
        }

        RegistrationFailure.FIRST_NAME_IS_TOO_SHORT -> {
            ViewState.Idle.ERROR_MESSAGE_FIRST_NAME_IS_TOO_SHORT
        }

        RegistrationFailure.LAST_NAME_CONTAINS_INVALID_CHARACTER -> {
            ViewState.Idle.ERROR_MESSAGE_LAST_NAME_CONTAINS_INVALID_CHARACTER
        }

        RegistrationFailure.LAST_NAME_IS_TOO_LONG -> {
            ViewState.Idle.ERROR_MESSAGE_LAST_NAME_IS_TOO_LONG
        }

        RegistrationFailure.LAST_NAME_IS_TOO_SHORT -> {
            ViewState.Idle.ERROR_MESSAGE_LAST_NAME_IS_TOO_SHORT
        }

        RegistrationFailure.PASSWORD_IS_TOO_LONG -> {
            ViewState.Idle.ERROR_MESSAGE_PASSWORD_IS_TOO_LONG
        }

        RegistrationFailure.PASSWORD_IS_TOO_SHORT -> {
            ViewState.Idle.ERROR_MESSAGE_PASSWORD_IS_TOO_SHORT
        }

        RegistrationFailure.USERNAME_CONTAINS_INVALID_CHARACTER -> {
            ViewState.Idle.ERROR_MESSAGE_USERNAME_CONTAINS_INVALID_CHARACTER
        }

        RegistrationFailure.USERNAME_IS_ALREADY_TAKEN -> {
            ViewState.Idle.ERROR_MESSAGE_USERNAME_IS_ALREADY_TAKEN
        }

        RegistrationFailure.USERNAME_IS_TOO_LONG -> {
            ViewState.Idle.ERROR_MESSAGE_USERNAME_IS_TOO_LONG
        }

        RegistrationFailure.USERNAME_IS_TOO_SHORT -> {
            ViewState.Idle.ERROR_MESSAGE_USERNAME_IS_TOO_SHORT
        }
    }

    private fun createRegistrationInProgressViewState(): ViewState.RegistrationInProgress =
        object : ViewState.RegistrationInProgress {

            override fun navigateBack() {
                navigateBackAction()
            }
        }
}