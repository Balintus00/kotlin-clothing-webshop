package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Store

internal interface UserManagementRootStore :
    Store<Nothing, UserManagementRootStore.State, Nothing> {

    data class State(val isAuthenticated: Boolean)
}