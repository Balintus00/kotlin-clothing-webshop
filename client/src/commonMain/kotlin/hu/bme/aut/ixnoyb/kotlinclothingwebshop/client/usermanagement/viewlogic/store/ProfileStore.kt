package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Store
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ProfileStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ProfileStore.State

interface ProfileStore : Store<Intent, State, Nothing> {

    sealed interface Intent {

        data object RetryProfileLoading : Intent
    }

    sealed interface State {

        data object ProfileLoading : State

        data object ProfileLoadingFailed : State

        data class ProfileLoaded(val user: User) : State
    }
}