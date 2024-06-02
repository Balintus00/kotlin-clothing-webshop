package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_CONTENT_SPACE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_ICON_EDGE_SIZE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.LOADING_BUTTON_CONTENT_SIZE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.LoginComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.getStandardSpace
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.element.EmailTextField
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.element.PasswordTextField
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.image.rememberAuthenticateImage
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.LoginComponent.ViewState.Idle.Companion.ERROR_MESSAGE_EMAIL_TOO_LONG
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.LoginComponent.ViewState.Idle.Companion.ERROR_MESSAGE_GENERAL_ERROR
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.LoginComponent.ViewState.Idle.Companion.ERROR_MESSAGE_INVALID_CREDENTIALS
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.LoginComponent.ViewState.Idle.Companion.ERROR_MESSAGE_INVALID_EMAIL_ADDRESS
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.LoginComponent.ViewState.Idle.Companion.ERROR_MESSAGE_PASSWORD_TOO_LONG
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.LoginComponent.ViewState.Idle.Companion.ERROR_MESSAGE_PASSWORD_TOO_SHORT
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.LoginComponent.ViewState.Idle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.LoginComponent.ViewState.Idle.Companion.EMAIL_MAXIMUM_LENGTH
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.LoginComponent.ViewState.Idle.Companion.PASSWORD_MAXIMUM_LENGTH
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.error_message_email_invalid
import kotlinclothingwebshop.client.generated.resources.error_message_email_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_general_error
import kotlinclothingwebshop.client.generated.resources.error_message_password_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_password_too_short
import kotlinclothingwebshop.client.generated.resources.ic_door_open
import kotlinclothingwebshop.client.generated.resources.login_screen_email_text_field_label
import kotlinclothingwebshop.client.generated.resources.login_screen_error_message_invalid_credentials
import kotlinclothingwebshop.client.generated.resources.login_screen_login_button_text
import kotlinclothingwebshop.client.generated.resources.login_screen_password_text_field_label
import kotlinclothingwebshop.client.generated.resources.login_screen_top_app_bar_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
internal fun LoginScreenTopAppBar(
    component: LoginComponent,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = viewState::navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        scrollBehavior = topAppBarScrollBehavior,
        title = { Text(stringResource(Res.string.login_screen_top_app_bar_title)) },
    )
}

// TODO an iteration could be done on the whole UI to segregate stateful and stateless functions,
//  and add previews.
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun LoginScreen(
    component: LoginComponent,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass
    val standardSpace = getStandardSpace(windowWidthSizeClass)

    Row(
        horizontalArrangement = Arrangement.spacedBy(standardSpace),
        modifier = modifier.padding(standardSpace),
    ) {
        LoginScreenForm(
            component = component,
            snackbarHostState = snackbarHostState,
            modifier = if (windowWidthSizeClass == Expanded) {
                Modifier.weight(0.5f).fillMaxHeight()
            } else {
                Modifier.fillMaxSize()
            },
        )

        if (windowWidthSizeClass == Expanded) {
            Image(
                contentDescription = null,
                imageVector = rememberAuthenticateImage(),
                modifier = Modifier.weight(0.5f).fillMaxHeight(),
            )
        }
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
internal fun LoginScreenForm(
    component: LoginComponent,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        var emailInput by remember { mutableStateOf("") }
        var passwordInput by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(
                getStandardSpace(calculateWindowSizeClass().widthSizeClass)
            ),
        ) {
            var isPasswordHidden by remember { mutableStateOf(true) }

            EmailTextField(
                isEnabled = viewState is Idle,
                isError = (viewState as? Idle)?.errorMessage in setOf(
                    ERROR_MESSAGE_EMAIL_TOO_LONG,
                    ERROR_MESSAGE_INVALID_EMAIL_ADDRESS,
                ),
                labelValue = stringResource(Res.string.login_screen_email_text_field_label),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    if (it.length <= EMAIL_MAXIMUM_LENGTH) {
                        emailInput = it
                    }
                },
                supportingTextValue = when ((viewState as? Idle)?.errorMessage) {
                    ERROR_MESSAGE_EMAIL_TOO_LONG -> {
                        stringResource(Res.string.error_message_email_too_long)
                    }

                    ERROR_MESSAGE_INVALID_EMAIL_ADDRESS -> {
                        stringResource(Res.string.error_message_email_invalid)
                    }

                    else -> ""
                },
                value = emailInput,
            )

            PasswordTextField(
                isEnabled = viewState is Idle,
                isError = (viewState as? Idle)?.errorMessage in setOf(
                    ERROR_MESSAGE_PASSWORD_TOO_SHORT,
                    ERROR_MESSAGE_PASSWORD_TOO_LONG,
                ),
                isPasswordHidden = isPasswordHidden,
                labelValue = stringResource(Res.string.login_screen_password_text_field_label),
                modifier = Modifier.fillMaxWidth(),
                changePasswordVisibilityAction = { isPasswordHidden = isPasswordHidden.not() },
                onValueChange = {
                    if (it.length <= PASSWORD_MAXIMUM_LENGTH) {
                        passwordInput = it
                    }
                },
                supportingTextValue = when ((viewState as? Idle)?.errorMessage) {
                    ERROR_MESSAGE_PASSWORD_TOO_SHORT -> {
                        stringResource(Res.string.error_message_password_too_short)
                    }

                    ERROR_MESSAGE_PASSWORD_TOO_LONG -> {
                        stringResource(Res.string.error_message_password_too_long)
                    }

                    else -> ""
                },
                value = passwordInput,
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            enabled = viewState is Idle,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                (viewState as? Idle)?.logIn(email = emailInput, password = passwordInput)
            },
        ) {
            if (viewState is Idle) {
                Icon(
                    modifier = Modifier
                        .width(COMMON_BUTTON_ICON_EDGE_SIZE)
                        .height(COMMON_BUTTON_ICON_EDGE_SIZE),
                    painter = painterResource(Res.drawable.ic_door_open),
                    contentDescription = null,
                )
                Spacer(Modifier.width(COMMON_BUTTON_CONTENT_SPACE))
                Text(stringResource(Res.string.login_screen_login_button_text))
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(LOADING_BUTTON_CONTENT_SIZE),
                )
            }
        }
    }

    (viewState as? Idle)?.errorMessage?.let {
        if (it in setOf(ERROR_MESSAGE_GENERAL_ERROR, ERROR_MESSAGE_INVALID_CREDENTIALS)) {
            val errorMessage = stringResource(
                if (it == ERROR_MESSAGE_INVALID_CREDENTIALS) {
                    Res.string.login_screen_error_message_invalid_credentials
                } else {
                    Res.string.error_message_general_error
                }
            )

            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                )
            }
        }
    }
}