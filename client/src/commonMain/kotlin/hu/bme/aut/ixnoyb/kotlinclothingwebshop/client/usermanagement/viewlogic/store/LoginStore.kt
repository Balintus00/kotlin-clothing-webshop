package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Store
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.LoginStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password

internal interface LoginStore : Store<Intent, State, Nothing> {

    sealed interface Intent {

        data class Login(val email: String, val password: String) : Intent
    }

    sealed interface State {

        data class Idle(val previousFailure: LoginFailure? = null) : State {

            enum class LoginFailure {
                GENERAL_ERROR,
                INVALID_EMAIL_ADDRESS,
                INVALID_CREDENTIALS,
                TOO_LONG_EMAIL,
                TOO_LONG_PASSWORD,
                TOO_SHORT_PASSWORD,
            }
        }

        data object LoginInProgress : State

        data object Authenticated : State

        companion object {

            const val EMAIL_MAXIMUM_LENGTH = Email.TOTAL_MAXIMUM_LENGTH
            const val PASSWORD_MAXIMUM_LENGTH = Password.MAXIMUM_LENGTH
        }
    }
}