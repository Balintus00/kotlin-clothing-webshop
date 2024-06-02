package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.UserManagementRootComponent

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun UserManagementRootScreenTopAppBar(
    component: UserManagementRootComponent,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    val rootChild by component.childStack.collectAsStateWithLifecycle()

    Children(
        animation = stackAnimation(fade()),
        modifier = modifier,
        stack = rootChild,
    ) { childContainer ->
        when (val child = childContainer.instance) {
            is UserManagementRootComponent.Child.Login -> {
                LoginScreenTopAppBar(child.component, topAppBarScrollBehavior)
            }

            is UserManagementRootComponent.Child.ModifyAccount -> {
                ModifyAccountScreenTopAppBar(child.component, topAppBarScrollBehavior)
            }

            is UserManagementRootComponent.Child.Profile -> {
                ProfileScreenTopAppBar(
                    component = child.component,
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                )
            }

            is UserManagementRootComponent.Child.Registration -> {
                RegistrationScreenTopAppBar(child.component, topAppBarScrollBehavior)
            }

            is UserManagementRootComponent.Child.UnauthenticatedInformation -> {
                // No-op
            }
        }
    }
}

@Composable
internal fun UserManagementRootScreen(
    component: UserManagementRootComponent,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val rootChild by component.childStack.collectAsStateWithLifecycle()

    Children(
        animation = stackAnimation(slide()),
        modifier = modifier,
        stack = rootChild,
    ) { childContainer ->
        when (val child = childContainer.instance) {
            is UserManagementRootComponent.Child.Login -> {
                LoginScreen(
                    component = child.component,
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is UserManagementRootComponent.Child.ModifyAccount -> {
                ModifyAccountScreen(
                    component = child.component,
                    modifier = Modifier.fillMaxSize(),
                    snackbarHostState = snackbarHostState,
                )
            }

            is UserManagementRootComponent.Child.Profile -> {
                ProfileScreen(
                    component = child.component,
                    modifier = Modifier.fillMaxSize(),
                    snackbarHostState = snackbarHostState,
                )
            }

            is UserManagementRootComponent.Child.Registration -> {
                RegistrationScreen(
                    component = child.component,
                    modifier = Modifier.fillMaxSize(),
                    snackbarHostState = snackbarHostState,
                )
            }

            is UserManagementRootComponent.Child.UnauthenticatedInformation -> {
                UnauthenticatedInformationScreenInformationPart(
                    component = child.component,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}