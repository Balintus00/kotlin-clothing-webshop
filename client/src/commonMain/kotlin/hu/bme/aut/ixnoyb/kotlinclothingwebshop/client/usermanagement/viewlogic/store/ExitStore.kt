package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Store
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ExitStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ExitStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password

interface ExitStore : Store<Intent, State, Nothing> {

    sealed interface Intent {

        data class DeleteAccount(val password: String) : Intent

        data object Logout : Intent
    }


    sealed interface State {

        data class Idle(
            val isLogoutFailedRecently: Boolean = false,
            val recentDeleteFailure: DeleteFailure? = null,
        ) : State {

            enum class DeleteFailure {
                GENERAL_ERROR,
                INVALID_PASSWORD,
                PASSWORD_TOO_LONG,
                PASSWORD_TOO_SHORT,
            }

            companion object {

                const val PASSWORD_MAXIMUM_LENGTH = Password.MAXIMUM_LENGTH
            }
        }

        data object Exiting : State

        data object Exited : State
    }
}