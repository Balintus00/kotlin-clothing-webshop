package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_CONTENT_SPACE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_ICON_EDGE_SIZE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.LOADING_BUTTON_CONTENT_SIZE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.PlatformSpecificVerticalListScrollbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.getStandardSpace
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.toDisplayDateFormat
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.element.DateOfBirthPickerDialog
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.element.DateOfBirthTextField
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.element.EmailTextField
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.element.NamePartTextField
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.element.PasswordTextField
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.element.UsernameTextField
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.RegistrationComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.RegistrationComponent.ViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.RegistrationComponent.ViewState.Idle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model.Account
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.error_message_email_invalid
import kotlinclothingwebshop.client.generated.resources.error_message_email_taken
import kotlinclothingwebshop.client.generated.resources.error_message_email_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_first_name_contains_invalid_character
import kotlinclothingwebshop.client.generated.resources.error_message_first_name_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_first_name_too_short
import kotlinclothingwebshop.client.generated.resources.error_message_general_error
import kotlinclothingwebshop.client.generated.resources.error_message_last_name_contains_invalid_character
import kotlinclothingwebshop.client.generated.resources.error_message_last_name_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_last_name_too_short
import kotlinclothingwebshop.client.generated.resources.error_message_password_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_password_too_short
import kotlinclothingwebshop.client.generated.resources.error_message_username_invalid_character
import kotlinclothingwebshop.client.generated.resources.error_message_username_taken
import kotlinclothingwebshop.client.generated.resources.error_message_username_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_username_too_short
import kotlinclothingwebshop.client.generated.resources.ic_add_box
import kotlinclothingwebshop.client.generated.resources.registration_screen_date_of_birth_text_field_label
import kotlinclothingwebshop.client.generated.resources.registration_screen_email_text_field_label
import kotlinclothingwebshop.client.generated.resources.registration_screen_first_name_text_field_label
import kotlinclothingwebshop.client.generated.resources.registration_screen_last_name_text_field_label
import kotlinclothingwebshop.client.generated.resources.registration_screen_password_text_field_label
import kotlinclothingwebshop.client.generated.resources.registration_screen_registration_button_text
import kotlinclothingwebshop.client.generated.resources.registration_screen_top_app_bar_title
import kotlinclothingwebshop.client.generated.resources.registration_screen_username_text_field_label
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
internal fun RegistrationScreenTopAppBar(
    component: RegistrationComponent,
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
        title = { Text(stringResource(Res.string.registration_screen_top_app_bar_title)) },
    )
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalResourceApi::class)
internal fun RegistrationScreen(
    component: RegistrationComponent,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass
    val standardSpace = getStandardSpace(windowWidthSizeClass)

    var isDatePickerDialogOpen by remember { mutableStateOf(false) }

    var dateOfBirthInput by remember { mutableStateOf("") }

    Box {
        val verticalScrollState = rememberScrollState()

        Column(
            modifier = modifier.verticalScroll(verticalScrollState).padding(standardSpace),
            verticalArrangement = Arrangement.spacedBy(standardSpace / 2),
        ) {
            var emailInput by remember { mutableStateOf("") }
            var firstNameInput by remember { mutableStateOf("") }
            var lastNameInput by remember { mutableStateOf("") }
            var passwordInput by remember { mutableStateOf("") }
            var usernameInput by remember { mutableStateOf("") }

            var isPasswordHidden by remember { mutableStateOf(true) }

            if (windowWidthSizeClass in setOf(Compact, Medium)) {
                CompactAndMediumWidthRegistrationFields(
                    changeEmailInputAction = { emailInput = it },
                    changeFirstNameInputAction = { firstNameInput = it },
                    changeLastNameInputAction = { lastNameInput = it },
                    changePasswordInputAction = { passwordInput = it },
                    changePasswordVisibilityAction = { isPasswordHidden = isPasswordHidden.not() },
                    changeUsernameInputAction = { usernameInput = it },
                    dateOfBirthInput = dateOfBirthInput,
                    emailInput = emailInput,
                    firstNameInput = firstNameInput,
                    isPasswordHidden = isPasswordHidden,
                    lastNameInput = lastNameInput,
                    modifier = Modifier,
                    openDatePickerDialogAction = { isDatePickerDialogOpen = true },
                    passwordInput = passwordInput,
                    usernameInput = usernameInput,
                    viewState = viewState,
                    windowWidthSizeClass = windowWidthSizeClass,
                )
            } else {
                ExpandedWidthRegistrationFields(
                    changeEmailInputAction = { emailInput = it },
                    changeFirstNameInputAction = { firstNameInput = it },
                    changeLastNameInputAction = { lastNameInput = it },
                    changePasswordInputAction = { passwordInput = it },
                    changePasswordVisibilityAction = { isPasswordHidden = isPasswordHidden.not() },
                    changeUsernameInputAction = { usernameInput = it },
                    dateOfBirthInput = dateOfBirthInput,
                    emailInput = emailInput,
                    firstNameInput = firstNameInput,
                    isPasswordHidden = isPasswordHidden,
                    lastNameInput = lastNameInput,
                    modifier = Modifier,
                    openDatePickerDialogAction = { isDatePickerDialogOpen = true },
                    passwordInput = passwordInput,
                    usernameInput = usernameInput,
                    viewState = viewState,
                )
            }

            Spacer(Modifier.weight(1.0f))

            Button(
                enabled = viewState is Idle && setOf(
                    dateOfBirthInput,
                    emailInput,
                    firstNameInput,
                    lastNameInput,
                    passwordInput,
                    usernameInput,
                ).all { it.isNotEmpty() },
                modifier = if (windowWidthSizeClass in setOf(Compact, Medium)) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier.fillMaxWidth(0.5f).padding(end = standardSpace / 2)
                },
                onClick = {
                    (viewState as? Idle)?.register(
                        Account(
                            dateOfBirth = LocalDate.parse(dateOfBirthInput),
                            email = emailInput,
                            firstName = firstNameInput,
                            lastName = lastNameInput,
                            password = passwordInput,
                            username = usernameInput,
                        )
                    )
                },
            ) {
                if (viewState is Idle) {
                    Icon(
                        modifier = Modifier
                            .width(COMMON_BUTTON_ICON_EDGE_SIZE)
                            .height(COMMON_BUTTON_ICON_EDGE_SIZE),
                        painter = painterResource(Res.drawable.ic_add_box),
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(COMMON_BUTTON_CONTENT_SPACE))
                    Text(stringResource(Res.string.registration_screen_registration_button_text))
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(LOADING_BUTTON_CONTENT_SIZE),
                    )
                }
            }
        }

        PlatformSpecificVerticalListScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            scrollState = verticalScrollState,
        )
    }

    (viewState as? Idle)?.errorMessage?.let {
        if (it == Idle.ERROR_MESSAGE_GENERAL_ERROR) {
            val errorMessage = stringResource(Res.string.error_message_general_error)

            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                )
            }
        }
    }

    if (isDatePickerDialogOpen) {
        DateOfBirthPickerDialog(
            closeDialogAction = { isDatePickerDialogOpen = false },
            earliestAllowedDate = LocalDate(
                year = Idle.DATE_OF_BIRTH_EARLIEST_ALLOWED_YEAR,
                monthNumber = Idle.DATE_OF_BIRTH_EARLIEST_ALLOWED_MONTH,
                dayOfMonth = Idle.DATE_OF_BIRTH_EARLIEST_ALLOWED_DAY,
            ),
            lastAllowedDate = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
                .minus(DatePeriod(years = DateOfBirth.LATEST_ALLOWED_YEAR_DIFFERENCE)),
            selectDateAction = { dateOfBirthInput = it.toString() },
        )
    }
}

@Composable
internal fun CompactAndMediumWidthRegistrationFields(
    dateOfBirthInput: String,
    emailInput: String,
    firstNameInput: String,
    lastNameInput: String,
    passwordInput: String,
    usernameInput: String,
    isPasswordHidden: Boolean,
    viewState: ViewState,
    windowWidthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    changeEmailInputAction: (String) -> Unit = {},
    changeFirstNameInputAction: (String) -> Unit = {},
    changeLastNameInputAction: (String) -> Unit = {},
    changePasswordInputAction: (String) -> Unit = {},
    changeUsernameInputAction: (String) -> Unit = {},
    changePasswordVisibilityAction: () -> Unit = {},
    openDatePickerDialogAction: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(getStandardSpace(windowWidthSizeClass)),
    ) {
        val areTextFieldsEnabled by derivedStateOf { viewState is Idle }
        val errorMessage by derivedStateOf { (viewState as? Idle)?.errorMessage }

        RegistrationFirstNameTextField(
            errorMessage = errorMessage,
            isEnabled = areTextFieldsEnabled,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changeFirstNameInputAction,
            value = firstNameInput,
        )
        RegistrationLastNameTextField(
            errorMessage = errorMessage,
            isEnabled = areTextFieldsEnabled,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changeLastNameInputAction,
            value = lastNameInput,
        )
        RegistrationDateOfBirthTextField(
            isEnabled = areTextFieldsEnabled,
            iso8601Value = dateOfBirthInput,
            modifier = Modifier.fillMaxWidth(),
            openDatePickerDialogAction = openDatePickerDialogAction,
        )
        RegistrationUsernameTextField(
            errorMessage = errorMessage,
            isEnabled = areTextFieldsEnabled,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changeUsernameInputAction,
            value = usernameInput,
        )
        RegistrationEmailTextField(
            errorMessage = errorMessage,
            isEnabled = areTextFieldsEnabled,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changeEmailInputAction,
            value = emailInput,
        )
        RegistrationPasswordTextField(
            changePasswordVisibility = changePasswordVisibilityAction,
            errorMessage = errorMessage,
            isEnabled = areTextFieldsEnabled,
            isPasswordHidden = isPasswordHidden,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changePasswordInputAction,
            value = passwordInput,
        )
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun RegistrationFirstNameTextField(
    isEnabled: Boolean,
    onValueChange: (String) -> Unit,
    value: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    NamePartTextField(
        isEnabled = isEnabled,
        isError = errorMessage in setOf(
            Idle.ERROR_MESSAGE_FIRST_NAME_CONTAINS_INVALID_CHARACTER,
            Idle.ERROR_MESSAGE_FIRST_NAME_IS_TOO_LONG,
            Idle.ERROR_MESSAGE_FIRST_NAME_IS_TOO_SHORT,
        ),
        labelValue = stringResource(Res.string.registration_screen_first_name_text_field_label),
        modifier = modifier,
        onValueChange = { if (it.length <= Idle.FIRST_NAME_MAXIMUM_LENGTH) onValueChange(it) },
        supportingTextValue = when (errorMessage) {
            Idle.ERROR_MESSAGE_FIRST_NAME_CONTAINS_INVALID_CHARACTER -> stringResource(
                Res.string.error_message_first_name_contains_invalid_character
            )

            Idle.ERROR_MESSAGE_FIRST_NAME_IS_TOO_LONG -> stringResource(
                Res.string.error_message_first_name_too_long
            )

            Idle.ERROR_MESSAGE_FIRST_NAME_IS_TOO_SHORT -> stringResource(
                Res.string.error_message_first_name_too_short
            )

            else -> ""
        },
        value = value,
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun RegistrationLastNameTextField(
    isEnabled: Boolean,
    onValueChange: (String) -> Unit,
    value: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    NamePartTextField(
        isEnabled = isEnabled,
        isError = errorMessage in setOf(
            Idle.ERROR_MESSAGE_LAST_NAME_CONTAINS_INVALID_CHARACTER,
            Idle.ERROR_MESSAGE_LAST_NAME_IS_TOO_LONG,
            Idle.ERROR_MESSAGE_LAST_NAME_IS_TOO_SHORT,
        ),
        labelValue = stringResource(Res.string.registration_screen_last_name_text_field_label),
        modifier = modifier,
        onValueChange = { if (it.length <= Idle.LAST_NAME_MAXIMUM_LENGTH) onValueChange(it) },
        supportingTextValue = when (errorMessage) {
            Idle.ERROR_MESSAGE_LAST_NAME_CONTAINS_INVALID_CHARACTER -> stringResource(
                Res.string.error_message_last_name_contains_invalid_character
            )

            Idle.ERROR_MESSAGE_LAST_NAME_IS_TOO_LONG -> stringResource(
                Res.string.error_message_last_name_too_long
            )

            Idle.ERROR_MESSAGE_LAST_NAME_IS_TOO_SHORT -> stringResource(
                Res.string.error_message_last_name_too_short
            )

            else -> ""
        },
        value = value,
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun RegistrationDateOfBirthTextField(
    isEnabled: Boolean,
    iso8601Value: String,
    modifier: Modifier = Modifier,
    openDatePickerDialogAction: () -> Unit,
) {
    DateOfBirthTextField(
        isEnabled = isEnabled,
        labelValue = stringResource(
            Res.string.registration_screen_date_of_birth_text_field_label
        ),
        modifier = modifier,
        selectDateAction = openDatePickerDialogAction,
        value = try {
            LocalDate.parse(iso8601Value).toDisplayDateFormat()
        } catch (t: Throwable) {
            ""
        },
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun RegistrationUsernameTextField(
    isEnabled: Boolean,
    onValueChange: (String) -> Unit,
    value: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    UsernameTextField(
        isEnabled = isEnabled,
        isError = errorMessage in setOf(
            Idle.ERROR_MESSAGE_USERNAME_CONTAINS_INVALID_CHARACTER,
            Idle.ERROR_MESSAGE_USERNAME_IS_ALREADY_TAKEN,
            Idle.ERROR_MESSAGE_USERNAME_IS_TOO_LONG,
            Idle.ERROR_MESSAGE_USERNAME_IS_TOO_SHORT,
        ),
        labelValue = stringResource(Res.string.registration_screen_username_text_field_label),
        modifier = modifier,
        onValueChange = { if (it.length <= Idle.USERNAME_MAXIMUM_LENGTH) onValueChange(it) },
        supportingTextValue = when (errorMessage) {
            Idle.ERROR_MESSAGE_USERNAME_CONTAINS_INVALID_CHARACTER -> stringResource(
                Res.string.error_message_username_invalid_character
            )

            Idle.ERROR_MESSAGE_USERNAME_IS_ALREADY_TAKEN -> stringResource(
                Res.string.error_message_username_taken
            )

            Idle.ERROR_MESSAGE_USERNAME_IS_TOO_LONG -> stringResource(
                Res.string.error_message_username_too_long
            )

            Idle.ERROR_MESSAGE_USERNAME_IS_TOO_SHORT -> stringResource(
                Res.string.error_message_username_too_short
            )

            else -> ""
        },
        value = value,
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun RegistrationEmailTextField(
    isEnabled: Boolean,
    onValueChange: (String) -> Unit,
    value: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    EmailTextField(
        isEnabled = isEnabled,
        isError = errorMessage in setOf(
            Idle.ERROR_MESSAGE_EMAIL_IS_ALREADY_TAKEN,
            Idle.ERROR_MESSAGE_EMAIL_IS_INVALID,
            Idle.ERROR_MESSAGE_EMAIL_IS_TOO_LONG,
        ),
        modifier = modifier,
        labelValue = stringResource(Res.string.registration_screen_email_text_field_label),
        onValueChange = { if (it.length <= Idle.EMAIL_MAXIMUM_LENGTH) onValueChange(it) },
        supportingTextValue = when (errorMessage) {
            Idle.ERROR_MESSAGE_EMAIL_IS_ALREADY_TAKEN -> stringResource(
                Res.string.error_message_email_taken
            )

            Idle.ERROR_MESSAGE_EMAIL_IS_INVALID -> stringResource(
                Res.string.error_message_email_invalid
            )

            Idle.ERROR_MESSAGE_EMAIL_IS_TOO_LONG -> stringResource(
                Res.string.error_message_email_too_long
            )

            else -> ""
        },
        value = value,
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun RegistrationPasswordTextField(
    isEnabled: Boolean,
    isPasswordHidden: Boolean,
    onValueChange: (String) -> Unit,
    value: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    changePasswordVisibility: () -> Unit = {},
) {
    PasswordTextField(
        changePasswordVisibilityAction = changePasswordVisibility,
        isEnabled = isEnabled,
        isError = errorMessage in setOf(
            Idle.ERROR_MESSAGE_PASSWORD_IS_TOO_LONG,
            Idle.ERROR_MESSAGE_PASSWORD_IS_TOO_SHORT,
        ),
        isPasswordHidden = isPasswordHidden,
        labelValue = stringResource(
            Res.string.registration_screen_password_text_field_label
        ),
        modifier = modifier,
        onValueChange = {
            if (it.length <= Idle.PASSWORD_MAXIMUM_LENGTH) {
                onValueChange(it)
            }
        },
        supportingTextValue = when (errorMessage) {
            Idle.ERROR_MESSAGE_PASSWORD_IS_TOO_LONG -> stringResource(
                Res.string.error_message_password_too_long
            )

            Idle.ERROR_MESSAGE_PASSWORD_IS_TOO_SHORT -> stringResource(
                Res.string.error_message_password_too_short
            )

            else -> ""
        },
        value = value,
    )
}

@Composable
internal fun ExpandedWidthRegistrationFields(
    viewState: ViewState,
    dateOfBirthInput: String,
    emailInput: String,
    firstNameInput: String,
    lastNameInput: String,
    passwordInput: String,
    usernameInput: String,
    isPasswordHidden: Boolean,
    modifier: Modifier = Modifier,
    changeEmailInputAction: (String) -> Unit = {},
    changeFirstNameInputAction: (String) -> Unit = {},
    changeLastNameInputAction: (String) -> Unit = {},
    changePasswordInputAction: (String) -> Unit = {},
    changeUsernameInputAction: (String) -> Unit = {},
    changePasswordVisibilityAction: () -> Unit = {},
    openDatePickerDialogAction: () -> Unit = {},
) {
    val standardSpace = getStandardSpace(Expanded)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(standardSpace),
    ) {
        val areTextFieldsEnabled by derivedStateOf { viewState is Idle }
        val errorMessage by derivedStateOf { (viewState as? Idle)?.errorMessage }

        Row(
            horizontalArrangement = Arrangement.spacedBy(standardSpace),
            modifier = Modifier.fillMaxWidth(),
        ) {
            RegistrationFirstNameTextField(
                errorMessage = errorMessage,
                isEnabled = areTextFieldsEnabled,
                modifier = Modifier.weight(0.5f),
                onValueChange = changeFirstNameInputAction,
                value = firstNameInput,
            )

            RegistrationLastNameTextField(
                errorMessage = errorMessage,
                isEnabled = areTextFieldsEnabled,
                modifier = Modifier.weight(0.5f),
                onValueChange = changeLastNameInputAction,
                value = lastNameInput,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(standardSpace),
            modifier = Modifier.fillMaxWidth(),
        ) {
            RegistrationDateOfBirthTextField(
                isEnabled = areTextFieldsEnabled,
                iso8601Value = dateOfBirthInput,
                modifier = Modifier.weight(0.5f),
                openDatePickerDialogAction = openDatePickerDialogAction,
            )

            RegistrationUsernameTextField(
                errorMessage = errorMessage,
                isEnabled = areTextFieldsEnabled,
                modifier = Modifier.weight(0.5f),
                onValueChange = changeUsernameInputAction,
                value = usernameInput,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(standardSpace),
            modifier = Modifier.fillMaxWidth(),
        ) {
            RegistrationEmailTextField(
                errorMessage = errorMessage,
                isEnabled = areTextFieldsEnabled,
                modifier = Modifier.weight(0.5f),
                onValueChange = changeEmailInputAction,
                value = emailInput,
            )

            RegistrationPasswordTextField(
                changePasswordVisibility = changePasswordVisibilityAction,
                errorMessage = errorMessage,
                isEnabled = areTextFieldsEnabled,
                isPasswordHidden = isPasswordHidden,
                modifier = Modifier.weight(0.5f),
                onValueChange = changePasswordInputAction,
                value = passwordInput,
            )
        }
    }
}