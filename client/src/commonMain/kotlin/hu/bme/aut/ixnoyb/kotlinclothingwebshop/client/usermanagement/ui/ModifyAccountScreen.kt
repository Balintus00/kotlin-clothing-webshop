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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
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
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.LoadingSection
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
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.ModifyAccountComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.ModifyAccountComponent.ViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.ModifyAccountComponent.ViewState.UserUnderModification
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model.UpdatedAccount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.error_message_email_invalid
import kotlinclothingwebshop.client.generated.resources.error_message_email_taken
import kotlinclothingwebshop.client.generated.resources.error_message_email_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_first_name_contains_invalid_character
import kotlinclothingwebshop.client.generated.resources.error_message_first_name_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_first_name_too_short
import kotlinclothingwebshop.client.generated.resources.error_message_last_name_contains_invalid_character
import kotlinclothingwebshop.client.generated.resources.error_message_last_name_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_last_name_too_short
import kotlinclothingwebshop.client.generated.resources.error_message_password_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_password_too_short
import kotlinclothingwebshop.client.generated.resources.error_message_username_invalid_character
import kotlinclothingwebshop.client.generated.resources.error_message_username_taken
import kotlinclothingwebshop.client.generated.resources.error_message_username_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_username_too_short
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_current_password_text_field_label
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_date_of_birth_text_field_label
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_email_text_field_label
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_error_message_wrong_current_password
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_first_name_text_field_label
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_initial_loading_snackbar_error_action_label
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_initial_loading_snackbar_error_message
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_last_name_text_field_label
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_modification_button_text
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_new_password_text_field_default_supporting_text
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_password_text_field_label
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_top_app_bar_title
import kotlinclothingwebshop.client.generated.resources.modify_account_screen_username_text_field_label
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
internal fun ModifyAccountScreenTopAppBar(
    component: ModifyAccountComponent,
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
        title = { Text(stringResource(Res.string.modify_account_screen_top_app_bar_title)) },
    )

}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun ModifyAccountScreen(
    component: ModifyAccountComponent,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    (viewState as? ViewState.InitialLoading)?.let {
        LoadingSection(modifier)    // TODO we could do skeleton loading
    } ?: (viewState as? ViewState.InitialLoadingFailed)?.let {
        val snackbarErrorActionLabel = stringResource(
            Res.string.modify_account_screen_initial_loading_snackbar_error_action_label
        )
        val snackbarErrorMessage = stringResource(
            Res.string.modify_account_screen_initial_loading_snackbar_error_message
        )

        rememberCoroutineScope().launch {
            val snackbarResult = snackbarHostState.showSnackbar(
                actionLabel = snackbarErrorActionLabel,
                message = snackbarErrorMessage,
                withDismissAction = true,
            )

            if (snackbarResult == SnackbarResult.ActionPerformed) {
                it.retry()
            }
        }
    } ?: run {
        InitializedModifyAccountScreen(modifier = modifier, viewState = viewState)
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
internal fun InitializedModifyAccountScreen(
    viewState: ViewState,
    modifier: Modifier = Modifier,
) {
    val currentProfile by derivedStateOf {
        (viewState as? UserUnderModification)?.currentProfile
    }

    val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass
    val standardSpace = getStandardSpace(windowWidthSizeClass)

    var isDatePickerDialogOpen by remember { mutableStateOf(false) }

    var dateOfBirthInput by remember {
        mutableStateOf(currentProfile?.dateOfBirth?.toString() ?: "")
    }

    Box {
        val verticalScrollState = rememberScrollState()

        Column(
            modifier = modifier.verticalScroll(verticalScrollState).padding(standardSpace),
            verticalArrangement = Arrangement.spacedBy(standardSpace / 2),
        ) {
            var currentPasswordInput by remember { mutableStateOf("") }
            var emailInput by remember { mutableStateOf(currentProfile?.email ?: "") }
            var firstNameInput by remember { mutableStateOf(currentProfile?.firstName ?: "") }
            var lastNameInput by remember { mutableStateOf(currentProfile?.lastName ?: "") }
            var newPasswordInput by remember { mutableStateOf("") }
            var usernameInput by remember { mutableStateOf(currentProfile?.username ?: "") }

            var isCurrentPasswordHidden by remember { mutableStateOf(true) }
            var isNewPasswordHidden by remember { mutableStateOf(true) }

            if (windowWidthSizeClass in setOf(Compact, Medium)) {
                CompactAndMediumWidthModificationFields(
                    changeCurrentPasswordInputAction = { currentPasswordInput = it },
                    changeCurrentPasswordVisibilityAction = {
                        isCurrentPasswordHidden = isCurrentPasswordHidden.not()
                    },
                    changeEmailInputAction = { emailInput = it },
                    changeFirstNameInputAction = { firstNameInput = it },
                    changeLastNameInputAction = { lastNameInput = it },
                    changeNewPasswordInputAction = { newPasswordInput = it },
                    changeNewPasswordVisibilityAction = {
                        isNewPasswordHidden = isNewPasswordHidden.not()
                    },
                    changeUsernameInputAction = { usernameInput = it },
                    currentPasswordInput = currentPasswordInput,
                    dateOfBirthInput = dateOfBirthInput,
                    emailInput = emailInput,
                    firstNameInput = firstNameInput,
                    isCurrentPasswordHidden = isCurrentPasswordHidden,
                    isNewPasswordHidden = isNewPasswordHidden,
                    lastNameInput = lastNameInput,
                    modifier = Modifier,
                    newPasswordInput = newPasswordInput,
                    openDatePickerDialogAction = { isDatePickerDialogOpen = true },
                    usernameInput = usernameInput,
                    viewState = viewState,
                    windowWidthSizeClass = windowWidthSizeClass,
                )
            } else {
                ExpandedWidthModificationFields(
                    changeCurrentPasswordInputAction = { currentPasswordInput = it },
                    changeCurrentPasswordVisibilityAction = {
                        isCurrentPasswordHidden = isCurrentPasswordHidden.not()
                    },
                    changeEmailInputAction = { emailInput = it },
                    changeFirstNameInputAction = { firstNameInput = it },
                    changeLastNameInputAction = { lastNameInput = it },
                    changeNewPasswordInputAction = { newPasswordInput = it },
                    changeNewPasswordVisibilityAction = {
                        isNewPasswordHidden = isNewPasswordHidden.not()
                    },
                    changeUsernameInputAction = { usernameInput = it },
                    currentPasswordInput = currentPasswordInput,
                    dateOfBirthInput = dateOfBirthInput,
                    emailInput = emailInput,
                    firstNameInput = firstNameInput,
                    isCurrentPasswordHidden = isCurrentPasswordHidden,
                    isNewPasswordHidden = isNewPasswordHidden,
                    lastNameInput = lastNameInput,
                    modifier = Modifier,
                    newPasswordInput = newPasswordInput,
                    openDatePickerDialogAction = { isDatePickerDialogOpen = true },
                    usernameInput = usernameInput,
                    viewState = viewState,
                )
            }

            Spacer(Modifier.weight(1.0f))

            Button(
                enabled = viewState is UserUnderModification && setOf(
                    dateOfBirthInput,
                    emailInput,
                    firstNameInput,
                    lastNameInput,
                    currentPasswordInput,
                    usernameInput,
                ).all { it.isNotEmpty() },
                modifier = if (windowWidthSizeClass in setOf(Compact, Medium)) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier.fillMaxWidth(0.5f).padding(end = standardSpace / 2)
                },
                onClick = {
                    (viewState as? UserUnderModification)?.modifyAccount(
                        currentPassword = currentPasswordInput,
                        updatedAccount = UpdatedAccount(
                            dateOfBirth = LocalDate.parse(dateOfBirthInput),
                            email = emailInput,
                            firstName = firstNameInput,
                            lastName = lastNameInput,
                            newPassword = newPasswordInput,
                            username = usernameInput,
                        ),
                    )
                },
            ) {
                if (viewState is UserUnderModification) {
                    Icon(
                        modifier = Modifier
                            .width(COMMON_BUTTON_ICON_EDGE_SIZE)
                            .height(COMMON_BUTTON_ICON_EDGE_SIZE),
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(COMMON_BUTTON_CONTENT_SPACE))
                    Text(stringResource(Res.string.modify_account_screen_modification_button_text))
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

    if (isDatePickerDialogOpen) {
        DateOfBirthPickerDialog(
            closeDialogAction = { isDatePickerDialogOpen = false },
            earliestAllowedDate = LocalDate(
                year = UserUnderModification.DATE_OF_BIRTH_EARLIEST_ALLOWED_YEAR,
                monthNumber = UserUnderModification.DATE_OF_BIRTH_EARLIEST_ALLOWED_MONTH,
                dayOfMonth = UserUnderModification.DATE_OF_BIRTH_EARLIEST_ALLOWED_DAY,
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
internal fun CompactAndMediumWidthModificationFields(
    currentPasswordInput: String,
    dateOfBirthInput: String,
    emailInput: String,
    firstNameInput: String,
    lastNameInput: String,
    newPasswordInput: String,
    usernameInput: String,
    isCurrentPasswordHidden: Boolean,
    isNewPasswordHidden: Boolean,
    viewState: ViewState,
    windowWidthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    changeCurrentPasswordInputAction: (String) -> Unit = {},
    changeEmailInputAction: (String) -> Unit = {},
    changeFirstNameInputAction: (String) -> Unit = {},
    changeLastNameInputAction: (String) -> Unit = {},
    changeNewPasswordInputAction: (String) -> Unit = {},
    changeUsernameInputAction: (String) -> Unit = {},
    changeCurrentPasswordVisibilityAction: () -> Unit = {},
    changeNewPasswordVisibilityAction: () -> Unit = {},
    openDatePickerDialogAction: () -> Unit = {},
) {
    val areTextFieldsEnabled by derivedStateOf { viewState is UserUnderModification }
    val errorMessage by derivedStateOf { (viewState as? UserUnderModification)?.errorMessage }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(getStandardSpace(windowWidthSizeClass)),
    ) {
        ModificationFirstNameTextField(
            errorMessage = errorMessage,
            isEnabled = areTextFieldsEnabled,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changeFirstNameInputAction,
            value = firstNameInput,
        )
        ModificationLastNameTextField(
            errorMessage = errorMessage,
            isEnabled = areTextFieldsEnabled,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changeLastNameInputAction,
            value = lastNameInput,
        )
        ModificationDateOfBirthTextField(
            isEnabled = areTextFieldsEnabled,
            iso8601Value = dateOfBirthInput,
            modifier = Modifier.fillMaxWidth(),
            openDatePickerDialogAction = openDatePickerDialogAction,
        )
        ModificationUsernameTextField(
            errorMessage = errorMessage,
            isEnabled = areTextFieldsEnabled,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changeUsernameInputAction,
            value = usernameInput,
        )
        ModificationEmailTextField(
            errorMessage = errorMessage,
            isEnabled = areTextFieldsEnabled,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changeEmailInputAction,
            value = emailInput,
        )
        ModificationNewPasswordTextField(
            changePasswordVisibility = changeNewPasswordVisibilityAction,
            errorMessage = errorMessage,
            isEnabled = areTextFieldsEnabled,
            isPasswordHidden = isNewPasswordHidden,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changeNewPasswordInputAction,
            value = newPasswordInput,
        )
        ModificationCurrentPasswordTextField(
            changePasswordVisibility = changeCurrentPasswordVisibilityAction,
            errorMessage = errorMessage,
            isEnabled = areTextFieldsEnabled,
            isPasswordHidden = isCurrentPasswordHidden,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changeCurrentPasswordInputAction,
            value = currentPasswordInput,
        )
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun ModificationCurrentPasswordTextField(
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
            UserUnderModification.ERROR_MESSAGE_CURRENT_PASSWORD_IS_WRONG,
            UserUnderModification.ERROR_MESSAGE_CURRENT_PASSWORD_IS_TOO_SHORT,
            UserUnderModification.ERROR_MESSAGE_CURRENT_PASSWORD_IS_TOO_LONG,
        ),
        isPasswordHidden = isPasswordHidden,
        labelValue = stringResource(
            Res.string.modify_account_screen_current_password_text_field_label
        ),
        modifier = modifier,
        onValueChange = {
            if (it.length <= UserUnderModification.PASSWORD_MAXIMUM_LENGTH) {
                onValueChange(it)
            }
        },
        supportingTextValue = when (errorMessage) {
            UserUnderModification.ERROR_MESSAGE_CURRENT_PASSWORD_IS_WRONG -> stringResource(
                Res.string.modify_account_screen_error_message_wrong_current_password
            )

            UserUnderModification.ERROR_MESSAGE_PASSWORD_IS_TOO_LONG -> stringResource(
                Res.string.error_message_password_too_long
            )

            UserUnderModification.ERROR_MESSAGE_PASSWORD_IS_TOO_SHORT -> stringResource(
                Res.string.error_message_password_too_short
            )

            else -> ""
        },
        value = value,
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun ModificationFirstNameTextField(
    isEnabled: Boolean,
    onValueChange: (String) -> Unit,
    value: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    NamePartTextField(
        isEnabled = isEnabled,
        isError = errorMessage in setOf(
            UserUnderModification.ERROR_MESSAGE_FIRST_NAME_CONTAINS_INVALID_CHARACTER,
            UserUnderModification.ERROR_MESSAGE_FIRST_NAME_IS_TOO_LONG,
            UserUnderModification.ERROR_MESSAGE_FIRST_NAME_IS_TOO_SHORT,
        ),
        labelValue = stringResource(Res.string.modify_account_screen_first_name_text_field_label),
        modifier = modifier,
        onValueChange = {
            if (it.length <= UserUnderModification.FIRST_NAME_MAXIMUM_LENGTH) onValueChange(it)
        },
        supportingTextValue = when (errorMessage) {
            UserUnderModification.ERROR_MESSAGE_FIRST_NAME_CONTAINS_INVALID_CHARACTER
            -> stringResource(
                Res.string.error_message_first_name_contains_invalid_character
            )

            UserUnderModification.ERROR_MESSAGE_FIRST_NAME_IS_TOO_LONG -> stringResource(
                Res.string.error_message_first_name_too_long
            )

            UserUnderModification.ERROR_MESSAGE_FIRST_NAME_IS_TOO_SHORT -> stringResource(
                Res.string.error_message_first_name_too_short
            )

            else -> ""
        },
        value = value,
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun ModificationLastNameTextField(
    isEnabled: Boolean,
    onValueChange: (String) -> Unit,
    value: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    NamePartTextField(
        isEnabled = isEnabled,
        isError = errorMessage in setOf(
            UserUnderModification.ERROR_MESSAGE_LAST_NAME_CONTAINS_INVALID_CHARACTER,
            UserUnderModification.ERROR_MESSAGE_LAST_NAME_IS_TOO_LONG,
            UserUnderModification.ERROR_MESSAGE_LAST_NAME_IS_TOO_SHORT,
        ),
        labelValue = stringResource(Res.string.modify_account_screen_last_name_text_field_label),
        modifier = modifier,
        onValueChange = {
            if (it.length <= UserUnderModification.LAST_NAME_MAXIMUM_LENGTH) onValueChange(
                it
            )
        },
        supportingTextValue = when (errorMessage) {
            UserUnderModification.ERROR_MESSAGE_LAST_NAME_CONTAINS_INVALID_CHARACTER
            -> stringResource(
                Res.string.error_message_last_name_contains_invalid_character
            )

            UserUnderModification.ERROR_MESSAGE_LAST_NAME_IS_TOO_LONG -> stringResource(
                Res.string.error_message_last_name_too_long
            )

            UserUnderModification.ERROR_MESSAGE_LAST_NAME_IS_TOO_SHORT -> stringResource(
                Res.string.error_message_last_name_too_short
            )

            else -> ""
        },
        value = value,
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun ModificationDateOfBirthTextField(
    isEnabled: Boolean,
    iso8601Value: String,
    modifier: Modifier = Modifier,
    openDatePickerDialogAction: () -> Unit,
) {
    DateOfBirthTextField(
        isEnabled = isEnabled,
        labelValue = stringResource(
            Res.string.modify_account_screen_date_of_birth_text_field_label
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
internal fun ModificationUsernameTextField(
    isEnabled: Boolean,
    onValueChange: (String) -> Unit,
    value: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    UsernameTextField(
        isEnabled = isEnabled,
        isError = errorMessage in setOf(
            UserUnderModification.ERROR_MESSAGE_USERNAME_CONTAINS_INVALID_CHARACTER,
            UserUnderModification.ERROR_MESSAGE_USERNAME_IS_ALREADY_TAKEN,
            UserUnderModification.ERROR_MESSAGE_USERNAME_IS_TOO_LONG,
            UserUnderModification.ERROR_MESSAGE_USERNAME_IS_TOO_SHORT,
        ),
        labelValue = stringResource(Res.string.modify_account_screen_username_text_field_label),
        modifier = modifier,
        onValueChange = {
            if (it.length <= UserUnderModification.USERNAME_MAXIMUM_LENGTH) onValueChange(
                it
            )
        },
        supportingTextValue = when (errorMessage) {
            UserUnderModification.ERROR_MESSAGE_USERNAME_CONTAINS_INVALID_CHARACTER
            -> stringResource(
                Res.string.error_message_username_invalid_character
            )

            UserUnderModification.ERROR_MESSAGE_USERNAME_IS_ALREADY_TAKEN -> stringResource(
                Res.string.error_message_username_taken
            )

            UserUnderModification.ERROR_MESSAGE_USERNAME_IS_TOO_LONG -> stringResource(
                Res.string.error_message_username_too_long
            )

            UserUnderModification.ERROR_MESSAGE_USERNAME_IS_TOO_SHORT -> stringResource(
                Res.string.error_message_username_too_short
            )

            else -> ""
        },
        value = value,
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun ModificationEmailTextField(
    isEnabled: Boolean,
    onValueChange: (String) -> Unit,
    value: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    EmailTextField(
        isEnabled = isEnabled,
        isError = errorMessage in setOf(
            UserUnderModification.ERROR_MESSAGE_EMAIL_IS_ALREADY_TAKEN,
            UserUnderModification.ERROR_MESSAGE_EMAIL_IS_INVALID,
            UserUnderModification.ERROR_MESSAGE_EMAIL_IS_TOO_LONG,
        ),
        modifier = modifier,
        labelValue = stringResource(Res.string.modify_account_screen_email_text_field_label),
        onValueChange = {
            if (it.length <= UserUnderModification.EMAIL_MAXIMUM_LENGTH) onValueChange(
                it
            )
        },
        supportingTextValue = when (errorMessage) {
            UserUnderModification.ERROR_MESSAGE_EMAIL_IS_ALREADY_TAKEN -> stringResource(
                Res.string.error_message_email_taken
            )

            UserUnderModification.ERROR_MESSAGE_EMAIL_IS_INVALID -> stringResource(
                Res.string.error_message_email_invalid
            )

            UserUnderModification.ERROR_MESSAGE_EMAIL_IS_TOO_LONG -> stringResource(
                Res.string.error_message_email_too_long
            )

            else -> ""
        },
        value = value,
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun ModificationNewPasswordTextField(
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
            UserUnderModification.ERROR_MESSAGE_PASSWORD_IS_TOO_LONG,
            UserUnderModification.ERROR_MESSAGE_PASSWORD_IS_TOO_SHORT,
        ),
        isPasswordHidden = isPasswordHidden,
        labelValue = stringResource(
            Res.string.modify_account_screen_password_text_field_label
        ),
        modifier = modifier,
        onValueChange = {
            if (it.length <= UserUnderModification.PASSWORD_MAXIMUM_LENGTH) {
                onValueChange(it)
            }
        },
        supportingTextValue = when (errorMessage) {
            UserUnderModification.ERROR_MESSAGE_PASSWORD_IS_TOO_LONG -> stringResource(
                Res.string.error_message_password_too_long
            )

            UserUnderModification.ERROR_MESSAGE_PASSWORD_IS_TOO_SHORT -> stringResource(
                Res.string.error_message_password_too_short
            )

            else -> stringResource(
                Res.string.modify_account_screen_new_password_text_field_default_supporting_text
            )
        },
        value = value,
    )
}

@Composable
internal fun ExpandedWidthModificationFields(
    currentPasswordInput: String,
    dateOfBirthInput: String,
    emailInput: String,
    firstNameInput: String,
    lastNameInput: String,
    newPasswordInput: String,
    usernameInput: String,
    isCurrentPasswordHidden: Boolean,
    isNewPasswordHidden: Boolean,
    viewState: ViewState,
    modifier: Modifier = Modifier,
    changeCurrentPasswordInputAction: (String) -> Unit = {},
    changeEmailInputAction: (String) -> Unit = {},
    changeFirstNameInputAction: (String) -> Unit = {},
    changeLastNameInputAction: (String) -> Unit = {},
    changeNewPasswordInputAction: (String) -> Unit = {},
    changeUsernameInputAction: (String) -> Unit = {},
    changeCurrentPasswordVisibilityAction: () -> Unit = {},
    changeNewPasswordVisibilityAction: () -> Unit = {},
    openDatePickerDialogAction: () -> Unit = {},
) {
    val standardSpace = getStandardSpace(WindowWidthSizeClass.Expanded)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(standardSpace),
    ) {
        val areTextFieldsEnabled by derivedStateOf { viewState is UserUnderModification }
        val errorMessage by derivedStateOf { (viewState as? UserUnderModification)?.errorMessage }

        Row(
            horizontalArrangement = Arrangement.spacedBy(standardSpace),
            modifier = Modifier.fillMaxWidth(),
        ) {
            ModificationFirstNameTextField(
                errorMessage = errorMessage,
                isEnabled = areTextFieldsEnabled,
                modifier = Modifier.weight(0.5f),
                onValueChange = changeFirstNameInputAction,
                value = firstNameInput,
            )

            ModificationLastNameTextField(
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
            ModificationDateOfBirthTextField(
                isEnabled = areTextFieldsEnabled,
                iso8601Value = dateOfBirthInput,
                modifier = Modifier.weight(0.5f),
                openDatePickerDialogAction = openDatePickerDialogAction,
            )

            ModificationUsernameTextField(
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
            ModificationEmailTextField(
                errorMessage = errorMessage,
                isEnabled = areTextFieldsEnabled,
                modifier = Modifier.weight(0.5f),
                onValueChange = changeEmailInputAction,
                value = emailInput,
            )

            ModificationNewPasswordTextField(
                changePasswordVisibility = changeNewPasswordVisibilityAction,
                errorMessage = errorMessage,
                isEnabled = areTextFieldsEnabled,
                isPasswordHidden = isNewPasswordHidden,
                modifier = Modifier.weight(0.5f),
                onValueChange = changeNewPasswordInputAction,
                value = newPasswordInput,
            )
        }

        ModificationCurrentPasswordTextField(
            changePasswordVisibility = changeCurrentPasswordVisibilityAction,
            isEnabled = areTextFieldsEnabled,
            isPasswordHidden = isCurrentPasswordHidden,
            modifier = Modifier.fillMaxWidth(0.5f).padding(end = standardSpace / 2),
            onValueChange = changeCurrentPasswordInputAction,
            value = currentPasswordInput,
        )
    }
}