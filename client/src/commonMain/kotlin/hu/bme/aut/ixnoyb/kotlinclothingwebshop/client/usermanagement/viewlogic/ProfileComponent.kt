package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.componentScope
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.createAndGetStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.getViewStateStateFlow
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.ProfileComponent.ViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.ProfileComponent.ViewState.ExitingViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.ProfileComponent.ViewState.ProfileViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model.Profile
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model.toProfile
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ExitStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ExitStoreProvider
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ProfileStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.ProfileStoreProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

interface ProfileComponent {

    val viewState: StateFlow<ViewState>

    interface ViewState {

        val exitingViewState: ExitingViewState

        val profileViewState: ProfileViewState

        fun navigateToModifyAccount()

        fun navigateToPurchaseHistory()

        interface ExitingViewState {

            interface Idle : ExitingViewState {

                val failure: ExitFailure?

                fun deleteAccount(password: String)

                fun logOut()

                enum class ExitFailure {
                    DELETION_GENERAL_ERROR,
                    DELETION_INVALID_PASSWORD,
                    DELETION_PASSWORD_TOO_SHORT,
                    DELETION_PASSWORD_TOO_LONG,

                    LOGOUT_GENERAL_ERROR,
                }

                companion object {

                    const val PASSWORD_MAXIMUM_LENGTH = ExitStore.State.Idle.PASSWORD_MAXIMUM_LENGTH
                }
            }

            interface Exiting : ExitingViewState
        }

        interface ProfileViewState {
            interface Loading : ProfileViewState

            interface Loaded : ProfileViewState {

                val profile: Profile
            }

            interface LoadingFailed : ProfileViewState {

                fun retry()
            }
        }
    }
}

internal class DefaultProfileComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val navigateToModifyAccountAction: () -> Unit = {},
    private val navigateToPurchaseHistoryAction: () -> Unit = {},
    private val onSuccessfulExitAction: () -> Unit = {},
) : ProfileComponent, ComponentContext by componentContext {

    private val profileStore = instanceKeeper.createAndGetStore {
        ProfileStoreProvider(storeFactory).create()
    }

    private val exitStore = instanceKeeper.createAndGetStore {
        ExitStoreProvider(storeFactory).create()
    }

    private val exitingViewState: StateFlow<ExitingViewState> = exitStore.getViewStateStateFlow(
        component = this,
        mapper = { it.toViewState() },
    )

    private val profileViewState: StateFlow<ProfileViewState> = profileStore.getViewStateStateFlow(
        component = this,
        mapper = { it.toViewState() },
    )

    override val viewState: StateFlow<ViewState> = exitingViewState
        .combine(profileViewState) { exitingState, profileState ->
            mergeViewState(exitingState, profileState)
        }.stateIn(
            initialValue = mergeViewState(exitingViewState.value, profileViewState.value),
            scope = componentScope,
            started = SharingStarted.Lazily,
        )

    private fun ExitStore.State.toViewState(): ExitingViewState = when (this) {
        is ExitStore.State.Exited -> {
            onSuccessfulExitAction()

            object : ExitingViewState.Exiting {}
        }

        is ExitStore.State.Exiting -> object : ExitingViewState.Exiting {}

        is ExitStore.State.Idle -> object : ExitingViewState.Idle {

            override val failure: ExitingViewState.Idle.ExitFailure? = if (isLogoutFailedRecently) {
                ExitingViewState.Idle.ExitFailure.LOGOUT_GENERAL_ERROR
            } else {
                recentDeleteFailure?.toUIModel()
            }

            private fun ExitStore.State.Idle.DeleteFailure.toUIModel():
                    ExitingViewState.Idle.ExitFailure = when (this) {
                ExitStore.State.Idle.DeleteFailure.GENERAL_ERROR -> {
                    ExitingViewState.Idle.ExitFailure.DELETION_GENERAL_ERROR
                }

                ExitStore.State.Idle.DeleteFailure.INVALID_PASSWORD -> {
                    ExitingViewState.Idle.ExitFailure.DELETION_INVALID_PASSWORD
                }

                ExitStore.State.Idle.DeleteFailure.PASSWORD_TOO_SHORT -> {
                    ExitingViewState.Idle.ExitFailure.DELETION_PASSWORD_TOO_SHORT
                }

                ExitStore.State.Idle.DeleteFailure.PASSWORD_TOO_LONG -> {
                    ExitingViewState.Idle.ExitFailure.DELETION_PASSWORD_TOO_LONG
                }
            }

            override fun deleteAccount(password: String) {
                exitStore.accept(ExitStore.Intent.DeleteAccount(password))
            }

            override fun logOut() {
                exitStore.accept(ExitStore.Intent.Logout)
            }
        }
    }

    private fun ProfileStore.State.toViewState(): ProfileViewState = when (this) {
        is ProfileStore.State.ProfileLoaded -> object : ProfileViewState.Loaded {

            override val profile: Profile = user.toProfile()
        }

        is ProfileStore.State.ProfileLoading -> object : ProfileViewState.Loading {}

        is ProfileStore.State.ProfileLoadingFailed -> object : ProfileViewState.LoadingFailed {

            override fun retry() {
                profileStore.accept(ProfileStore.Intent.RetryProfileLoading)
            }
        }
    }

    private fun mergeViewState(
        exitingViewState: ExitingViewState,
        profileViewState: ProfileViewState,
    ): ViewState = object : ViewState {

        override val exitingViewState: ExitingViewState = exitingViewState

        override val profileViewState: ProfileViewState = profileViewState

        private val isNavigationEnabled
            get() = (exitingViewState is ExitingViewState.Exiting).not()

        override fun navigateToModifyAccount() {
            if (isNavigationEnabled) {
                navigateToModifyAccountAction()
            }
        }

        override fun navigateToPurchaseHistory() {
            if (isNavigationEnabled) {
                navigateToPurchaseHistoryAction()
            }
        }
    }
}