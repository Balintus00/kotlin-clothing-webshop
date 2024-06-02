package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic

import com.arkivanov.decompose.ComponentContext

interface UnauthenticatedInformationComponent {

    fun navigateToLogin()

    fun navigateToRegistration()
}

internal class DefaultUnauthenticatedInformationComponent(
    componentContext: ComponentContext,
    private val navigateToLoginAction: () -> Unit,
    private val navigateToRegistrationAction: () -> Unit,
) : UnauthenticatedInformationComponent, ComponentContext by componentContext {

    override fun navigateToLogin() {
        navigateToLoginAction()
    }

    override fun navigateToRegistration() {
        navigateToRegistrationAction()
    }
}