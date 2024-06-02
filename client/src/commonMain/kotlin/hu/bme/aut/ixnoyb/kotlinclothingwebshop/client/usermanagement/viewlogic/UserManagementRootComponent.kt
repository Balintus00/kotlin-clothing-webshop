package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.createAndGetStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.toStateFlow
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.UserManagementRootComponent.Child
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.UserManagementRootComponent.Child.Login
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.UserManagementRootComponent.Child.ModifyAccount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.UserManagementRootComponent.Child.Profile
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.UserManagementRootComponent.Child.Registration
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.UserManagementRootComponent.Child.UnauthenticatedInformation
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.store.UserManagementRootStoreProvider
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface UserManagementRootComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {

        class Login(val component: LoginComponent) : Child

        class ModifyAccount(val component: ModifyAccountComponent) : Child

        class Profile(val component: ProfileComponent) : Child

        class Registration(val component: RegistrationComponent) : Child

        class UnauthenticatedInformation(val component: UnauthenticatedInformationComponent) : Child
    }
}

internal class DefaultUserManagementRootComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val navigateToPurchaseHistoryAction: () -> Unit = {},
) : UserManagementRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val store = instanceKeeper.createAndGetStore {
        UserManagementRootStoreProvider(storeFactory).create()
    }

    override val childStack: StateFlow<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = if (store.state.isAuthenticated) {
                Config.Profile
            } else {
                Config.UnauthenticatedInformation
            },
            handleBackButton = true,
            childFactory = ::createChild,
        ).toStateFlow()

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ): Child {
        val navigateBackToUnauthenticatedInformationAction = {
            navigation.popWhile { (it is Config.UnauthenticatedInformation).not() }
        }
        val replaceToProfileAction = { navigation.replaceAll(Config.Profile) }

        return when (config) {
            Config.Login -> {
                Login(
                    DefaultLoginComponent(
                        componentContext = componentContext,
                        navigateBackAction = navigateBackToUnauthenticatedInformationAction,
                        onSuccessfulLoginAction = replaceToProfileAction,
                        storeFactory = storeFactory,
                    )
                )
            }

            Config.ModifyAccount -> {
                ModifyAccount(
                    DefaultModifyAccountComponent(
                        componentContext = componentContext,
                        navigateBackAction = navigateBackToUnauthenticatedInformationAction,
                        onSuccessfulAccountModificationAction = {
                            navigation.popWhile { (it is Config.Profile).not() }
                        },
                        storeFactory = storeFactory,
                    )
                )
            }

            Config.Profile -> {
                Profile(
                    DefaultProfileComponent(
                        componentContext = componentContext,
                        navigateToPurchaseHistoryAction = navigateToPurchaseHistoryAction,
                        navigateToModifyAccountAction = {
                            navigation.pushNew(Config.ModifyAccount)
                        },
                        onSuccessfulExitAction = {
                            navigation.replaceAll(Config.UnauthenticatedInformation)
                        },
                        storeFactory = storeFactory,
                    )
                )
            }

            Config.Registration -> {
                Registration(
                    DefaultRegistrationComponent(
                        componentContext = componentContext,
                        navigateBackAction = navigateBackToUnauthenticatedInformationAction,
                        onSuccessfulRegistrationAction = replaceToProfileAction,
                        storeFactory = storeFactory,
                    )
                )
            }

            Config.UnauthenticatedInformation -> {
                UnauthenticatedInformation(
                    DefaultUnauthenticatedInformationComponent(
                        componentContext = componentContext,
                        navigateToLoginAction = { navigation.pushNew(Config.Login) },
                        navigateToRegistrationAction = { navigation.pushNew(Config.Registration) },
                    )
                )
            }
        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object UnauthenticatedInformation : Config

        @Serializable
        data object Login : Config

        @Serializable
        data object Registration : Config

        @Serializable
        data object Profile : Config

        @Serializable
        data object ModifyAccount : Config
    }
}