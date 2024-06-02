package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Store
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model.Account
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.RegistrationStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username

internal interface RegistrationStore : Store<Intent, State, Nothing> {

    sealed interface Intent {

        data class Register(val userCandidate: Account) : Intent
    }

    sealed interface State {

        data class Idle(val previousFailure: RegistrationFailure? = null) : State {

            enum class RegistrationFailure {
                DATE_OF_BIRTH_TOO_LATE,
                DATE_OF_BIRTH_TOO_EARLY,
                EMAIL_IS_ALREADY_TAKEN,
                EMAIL_IS_INVALID,
                EMAIL_IS_TOO_LONG,
                GENERAL_ERROR,
                FIRST_NAME_CONTAINS_INVALID_CHARACTER,
                FIRST_NAME_IS_TOO_LONG,
                FIRST_NAME_IS_TOO_SHORT,
                LAST_NAME_CONTAINS_INVALID_CHARACTER,
                LAST_NAME_IS_TOO_SHORT,
                LAST_NAME_IS_TOO_LONG,
                PASSWORD_IS_TOO_LONG,
                PASSWORD_IS_TOO_SHORT,
                USERNAME_CONTAINS_INVALID_CHARACTER,
                USERNAME_IS_ALREADY_TAKEN,
                USERNAME_IS_TOO_LONG,
                USERNAME_IS_TOO_SHORT,
            }
        }

        data object RegistrationInProgress : State

        data object Registered : State

        companion object {

            const val DATE_OF_BIRTH_EARLIEST_ALLOWED_YEAR = DateOfBirth.EARLIEST_ALLOWED_YEAR
            const val DATE_OF_BIRTH_EARLIEST_ALLOWED_MONTH = DateOfBirth.EARLIEST_ALLOWED_MONTH
            const val DATE_OF_BIRTH_EARLIEST_ALLOWED_DAY = DateOfBirth.EARLIEST_ALLOWED_DAY
            const val DATE_OF_BIRTH_LATEST_ALLOWED_YEAR_DIFFERENCE =
                DateOfBirth.LATEST_ALLOWED_YEAR_DIFFERENCE

            const val EMAIL_MAXIMUM_LENGTH = Email.TOTAL_MAXIMUM_LENGTH

            const val FIRST_NAME_MAXIMUM_LENGTH = FirstName.MAXIMUM_LENGTH

            const val LAST_NAME_MAXIMUM_LENGTH = LastName.MAXIMUM_LENGTH

            const val PASSWORD_MAXIMUM_LENGTH = Password.MAXIMUM_LENGTH

            const val USERNAME_MAXIMUM_LENGTH = Username.MAXIMUM_LENGTH
        }
    }
}