package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.createAndGetStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.getViewStateStateFlow
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.LoginComponent.ViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStoreProvider
import kotlinx.coroutines.flow.StateFlow

interface LoginComponent {

    val viewState: StateFlow<ViewState>

    sealed interface ViewState {

        fun navigateBack()

        interface Idle : ViewState {

            val errorMessage: String?

            fun logIn(email: String, password: String)

            // TODO moving these values to properties would allow more flexibility
            companion object {

                const val EMAIL_MAXIMUM_LENGTH = LoginStore.State.EMAIL_MAXIMUM_LENGTH
                const val PASSWORD_MAXIMUM_LENGTH = LoginStore.State.PASSWORD_MAXIMUM_LENGTH

                const val ERROR_MESSAGE_EMAIL_TOO_LONG = "ERROR_MESSAGE_EMAIL_TOO_LONG"
                const val ERROR_MESSAGE_GENERAL_ERROR = "ERROR_MESSAGE_GENERAL_ERROR"
                const val ERROR_MESSAGE_INVALID_CREDENTIALS = "ERROR_MESSAGE_INVALID_CREDENTIALS"
                const val ERROR_MESSAGE_INVALID_EMAIL_ADDRESS = "ERROR_MESSAGE_INVALID_EMAIL_ADDRESS"
                const val ERROR_MESSAGE_PASSWORD_TOO_SHORT = "ERROR_MESSAGE_PASSWORD_TOO_SHORT"
                const val ERROR_MESSAGE_PASSWORD_TOO_LONG = "ERROR_MESSAGE_PASSWORD_TOO_LONG"
            }
        }

        interface LoginInProgress : ViewState
    }
}

internal class DefaultLoginComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val navigateBackAction: () -> Unit = {},
    private val onSuccessfulLoginAction: () -> Unit = {},
) : LoginComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.createAndGetStore {
        LoginStoreProvider(storeFactory).create()
    }

    override val viewState: StateFlow<ViewState> = store.getViewStateStateFlow(
        component = this,
        mapper = { it.toViewState() },
    )

    private fun State.toViewState(): ViewState = when (this) {
        is State.Authenticated ->{
            onSuccessfulLoginAction()

            createLoginInProgressViewState()
        }

        is State.Idle -> object : ViewState.Idle {

            override val errorMessage: String? = when (previousFailure) {
                State.Idle.LoginFailure.GENERAL_ERROR -> {
                    ViewState.Idle.ERROR_MESSAGE_GENERAL_ERROR
                }

                State.Idle.LoginFailure.INVALID_CREDENTIALS -> {
                    ViewState.Idle.ERROR_MESSAGE_INVALID_CREDENTIALS
                }

                State.Idle.LoginFailure.INVALID_EMAIL_ADDRESS -> {
                    ViewState.Idle.ERROR_MESSAGE_INVALID_EMAIL_ADDRESS
                }

                State.Idle.LoginFailure.TOO_LONG_EMAIL -> {
                    ViewState.Idle.ERROR_MESSAGE_EMAIL_TOO_LONG
                }

                State.Idle.LoginFailure.TOO_LONG_PASSWORD -> {
                    ViewState.Idle.ERROR_MESSAGE_PASSWORD_TOO_LONG
                }

                State.Idle.LoginFailure.TOO_SHORT_PASSWORD -> {
                    ViewState.Idle.ERROR_MESSAGE_PASSWORD_TOO_SHORT
                }

                null -> {
                    null
                }
            }

            override fun logIn(email: String, password: String) {
                store.accept(LoginStore.Intent.Login(email = email, password = password))
            }

            override fun navigateBack() {
                navigateBackAction()
            }
        }

        is State.LoginInProgress -> createLoginInProgressViewState()
    }

    private fun createLoginInProgressViewState(): ViewState.LoginInProgress =
        object : ViewState.LoginInProgress {

            override fun navigateBack() {
                navigateBackAction()
            }
        }
}