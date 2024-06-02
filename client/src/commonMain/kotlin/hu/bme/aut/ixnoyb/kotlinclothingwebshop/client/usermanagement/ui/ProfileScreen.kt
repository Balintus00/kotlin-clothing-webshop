package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.BasicDialogWithClassicalLayout
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_CONTENT_SPACE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_ICON_EDGE_SIZE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.LOADING_BUTTON_CONTENT_SIZE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.getStandardSpace
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.placeholder
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.toDisplayDateFormat
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme.Material3Typography
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.element.PasswordTextField
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.ProfileComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.ProfileComponent.ViewState.ExitingViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.ProfileComponent.ViewState.ExitingViewState.Idle.ExitFailure
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.ProfileComponent.ViewState.ProfileViewState
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.error_message_general_error
import kotlinclothingwebshop.client.generated.resources.error_message_password_too_long
import kotlinclothingwebshop.client.generated.resources.error_message_password_too_short
import kotlinclothingwebshop.client.generated.resources.ic_deployed_code_history
import kotlinclothingwebshop.client.generated.resources.ic_logout
import kotlinclothingwebshop.client.generated.resources.profile_screen_date_of_birth_user_attribute
import kotlinclothingwebshop.client.generated.resources.profile_screen_delete_account_dialog_cancel_button_text
import kotlinclothingwebshop.client.generated.resources.profile_screen_delete_account_dialog_delete_button_text
import kotlinclothingwebshop.client.generated.resources.profile_screen_delete_account_dialog_description
import kotlinclothingwebshop.client.generated.resources.profile_screen_delete_account_dialog_instruction
import kotlinclothingwebshop.client.generated.resources.profile_screen_delete_account_dialog_password_text_field_invalid_error
import kotlinclothingwebshop.client.generated.resources.profile_screen_delete_account_dialog_password_text_field_label
import kotlinclothingwebshop.client.generated.resources.profile_screen_delete_account_dialog_title
import kotlinclothingwebshop.client.generated.resources.profile_screen_email_user_attribute
import kotlinclothingwebshop.client.generated.resources.profile_screen_first_name_user_attribute
import kotlinclothingwebshop.client.generated.resources.profile_screen_last_name_user_attribute
import kotlinclothingwebshop.client.generated.resources.profile_screen_menu_delete_account_label
import kotlinclothingwebshop.client.generated.resources.profile_screen_menu_logout_label
import kotlinclothingwebshop.client.generated.resources.profile_screen_menu_modify_account_label
import kotlinclothingwebshop.client.generated.resources.profile_screen_profile_loading_error_snackbar_action_label
import kotlinclothingwebshop.client.generated.resources.profile_screen_profile_loading_error_snackbar_message
import kotlinclothingwebshop.client.generated.resources.profile_screen_purchase_history_button_text
import kotlinclothingwebshop.client.generated.resources.profile_screen_top_app_bar_title
import kotlinclothingwebshop.client.generated.resources.profile_screen_username_user_attribute
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalResourceApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
internal fun ProfileScreenTopAppBar(
    component: ProfileComponent,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()
    val exitViewState = viewState.exitingViewState

    val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass

    var isMenuOpened by remember { mutableStateOf(false) }

    var isDialogVisible by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        actions = {
            IconButton(
                onClick = { isMenuOpened = isMenuOpened.not() },
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                )

                if (windowWidthSizeClass != Compact) {
                    ProfileActionsDropdownMenu(
                        closeMenuAction = { isMenuOpened = false },
                        isMenuExpanded = isMenuOpened,
                        onDeleteAccountItemClickedAction = {
                            isMenuOpened = false
                            isDialogVisible = true
                        },
                        onLogoutItemClickedAction = {
                            (exitViewState as? ExitingViewState.Idle)
                                ?.logOut()
                        },
                        onModifyAccountItemClickedAction = { viewState.navigateToModifyAccount() },
                    )
                }
            }
        },
        modifier = modifier,
        scrollBehavior = topAppBarScrollBehavior,
        title = { Text(stringResource(Res.string.profile_screen_top_app_bar_title)) },
    )

    if (windowWidthSizeClass == Compact && isMenuOpened) {
        ModalBottomSheet(
            onDismissRequest = { isMenuOpened = false }
        ) {
            ProfileActionBottomSheetContent(
                onDeleteAccountItemClickedAction = {
                    isMenuOpened = false
                    isDialogVisible = true
                },
                onLogoutItemClickedAction = {
                    (exitViewState as? ExitingViewState.Idle)
                        ?.logOut()
                },
                onModifyAccountItemClickedAction = { viewState.navigateToModifyAccount() },
            )
        }
    }

    if (isDialogVisible) {
        BasicAlertDialog(
            onDismissRequest = {
                if (viewState.exitingViewState is ExitingViewState.Idle) {
                    isDialogVisible = false
                }
            }
        ) {
            DeleteAccountConfirmationDialogContent(
                deleteAccountAction = {
                    (viewState.exitingViewState as? ExitingViewState.Idle)?.deleteAccount(it)
                },
                dismissDialogAction = {
                    if (viewState.exitingViewState is ExitingViewState.Idle) {
                        isDialogVisible = false
                    }
                },
                isDeleting = viewState.exitingViewState is ExitingViewState.Exiting,
                previousDeletionFailure = (viewState.exitingViewState as? ExitingViewState.Idle)?.failure,
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun ProfileActionsDropdownMenu(
    isMenuExpanded: Boolean,
    closeMenuAction: () -> Unit = {},
    onDeleteAccountItemClickedAction: () -> Unit = {},
    onLogoutItemClickedAction: () -> Unit = {},
    onModifyAccountItemClickedAction: () -> Unit = {},
) {
    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = closeMenuAction,
    ) {
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    contentDescription = null,
                    painter = painterResource(Res.drawable.ic_logout),
                )
            },
            onClick = onLogoutItemClickedAction,
            text = { Text(stringResource(Res.string.profile_screen_menu_logout_label)) },
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Default.Edit,
                )
            },
            onClick = onModifyAccountItemClickedAction,
            text = { Text(stringResource(Res.string.profile_screen_menu_modify_account_label)) },
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Default.Delete,
                    tint = MaterialTheme.colorScheme.error,
                )
            },
            onClick = onDeleteAccountItemClickedAction,
            text = {
                Text(
                    color = MaterialTheme.colorScheme.error,
                    text = stringResource(Res.string.profile_screen_menu_delete_account_label),
                )
            },
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun ProfileActionBottomSheetContent(
    modifier: Modifier = Modifier,
    onDeleteAccountItemClickedAction: () -> Unit = {},
    onLogoutItemClickedAction: () -> Unit = {},
    onModifyAccountItemClickedAction: () -> Unit = {},
) {
    Column(modifier = modifier) {
        ListItem(
            headlineContent = {
                Text(
                    color = MaterialTheme.colorScheme.error,
                    text = stringResource(Res.string.profile_screen_menu_delete_account_label),
                )
            },
            leadingContent = {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Default.Delete,
                    tint = MaterialTheme.colorScheme.error,
                )
            },
            modifier = Modifier.clickable(onClick = onDeleteAccountItemClickedAction),
        )
        ListItem(
            headlineContent = {
                Text(stringResource(Res.string.profile_screen_menu_modify_account_label))
            },
            leadingContent = {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Default.Edit,
                )
            },
            modifier = Modifier.clickable(onClick = onModifyAccountItemClickedAction),
        )
        ListItem(
            headlineContent = {
                Text(stringResource(Res.string.profile_screen_menu_logout_label))
            },
            leadingContent = {
                Icon(
                    contentDescription = null,
                    painter = painterResource(Res.drawable.ic_logout),
                )
            },
            modifier = Modifier.clickable(onClick = onLogoutItemClickedAction),
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun DeleteAccountConfirmationDialogContent(
    isDeleting: Boolean,
    previousDeletionFailure: ExitFailure?,
    modifier: Modifier = Modifier,
    deleteAccountAction: (String) -> Unit = {},
    dismissDialogAction: () -> Unit = {},
) {
    var password by remember { mutableStateOf("") }

    BasicDialogWithClassicalLayout(
        topContentColumn = {
            var isPasswordHidden by remember { mutableStateOf(true) }

            Icon(
                contentDescription = null,
                imageVector = Icons.Default.Delete,
                tint = MaterialTheme.colorScheme.secondary,
            )
            Text(
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(),
                style = Material3Typography.headlineSmall,
                text = stringResource(
                    Res.string.profile_screen_delete_account_dialog_title
                ),
                textAlign = TextAlign.Center,
            )
            Text(
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(),
                style = Material3Typography.bodyMedium,
                text = stringResource(
                    Res.string.profile_screen_delete_account_dialog_description
                ),
            )
            PasswordTextField(
                changePasswordVisibilityAction = { isPasswordHidden = isPasswordHidden.not() },
                isEnabled = isDeleting.not(),
                isError = previousDeletionFailure in setOf(
                    ExitFailure.DELETION_INVALID_PASSWORD,
                    ExitFailure.DELETION_PASSWORD_TOO_LONG,
                    ExitFailure.DELETION_PASSWORD_TOO_SHORT,
                ),
                isPasswordHidden = isPasswordHidden,
                labelValue = stringResource(
                    Res.string.profile_screen_delete_account_dialog_password_text_field_label
                ),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    if (password.length <= ExitingViewState.Idle.PASSWORD_MAXIMUM_LENGTH) {
                        password = it
                    }
                },
                supportingTextValue = stringResource(
                    when (previousDeletionFailure) {
                        ExitFailure.DELETION_INVALID_PASSWORD -> {
                            Res.string.profile_screen_delete_account_dialog_password_text_field_invalid_error
                        }

                        ExitFailure.DELETION_PASSWORD_TOO_LONG -> {
                            Res.string.error_message_password_too_long
                        }

                        ExitFailure.DELETION_PASSWORD_TOO_SHORT -> {
                            Res.string.error_message_password_too_short
                        }

                        else -> {
                            Res.string.profile_screen_delete_account_dialog_instruction
                        }
                    }
                ),
                value = password,
            )

            if (previousDeletionFailure == ExitFailure.DELETION_GENERAL_ERROR) {
                Text(
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    style = Material3Typography.titleSmall,
                    text = stringResource(Res.string.error_message_general_error),
                )
            }
        },
        bottomActionRow = {
            TextButton(
                enabled = isDeleting.not(),
                onClick = dismissDialogAction,
            ) {
                Text(
                    stringResource(
                        Res.string.profile_screen_delete_account_dialog_cancel_button_text
                    )
                )
            }
            TextButton(
                enabled = isDeleting.not(),
                onClick = { deleteAccountAction(password) },
            ) {
                if (isDeleting.not()) {
                    Text(
                        color = MaterialTheme.colorScheme.error,
                        text = stringResource(
                            Res.string.profile_screen_delete_account_dialog_delete_button_text
                        ),
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(LOADING_BUTTON_CONTENT_SIZE),
                    )
                }
            }
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalResourceApi::class)
@Composable
internal fun ProfileScreen(
    component: ProfileComponent,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass
    val standardSpace = getStandardSpace(windowWidthSizeClass)

    if (windowWidthSizeClass == Compact) {
        CompactWidthProfileScreen(component, modifier.padding(standardSpace))
    } else {
        MediumAndExpandedWidthProfileScreen(
            component = component,
            modifier = modifier.padding(standardSpace),
            windowWidthSizeClass = windowWidthSizeClass,
        )
    }

    (viewState.profileViewState as? ProfileViewState.LoadingFailed)?.let {
        val snackbarActionLabel =stringResource(
            Res.string.profile_screen_profile_loading_error_snackbar_action_label
        )
        val snackbarMessage = stringResource(
            Res.string.profile_screen_profile_loading_error_snackbar_message
        )

        scope.launch {
            snackbarHostState.showSnackbar(
                actionLabel = snackbarActionLabel,
                message = snackbarMessage,
                withDismissAction = true,
            )
        }
    } ?: run {
        if ((viewState.exitingViewState as? ExitingViewState.Idle)?.failure
            == ExitFailure.LOGOUT_GENERAL_ERROR) {
            val snackbarMessage = stringResource(Res.string.error_message_general_error)

            scope.launch {
                snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun CompactWidthProfileScreen(
    component: ProfileComponent,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(getStandardSpace(Compact)),
    ) {
        ProfileLabeledAttribute(
            isLoading = viewState.profileViewState is ProfileViewState.Loading,
            label = stringResource(Res.string.profile_screen_first_name_user_attribute),
            modifier = Modifier.fillMaxWidth(),
            value = (viewState.profileViewState as? ProfileViewState.Loaded)
                ?.profile?.firstName ?: "",
        )
        ProfileLabeledAttribute(
            isLoading = viewState.profileViewState is ProfileViewState.Loading,
            label = stringResource(Res.string.profile_screen_last_name_user_attribute),
            modifier = Modifier.fillMaxWidth(),
            value = (viewState.profileViewState as? ProfileViewState.Loaded)
                ?.profile?.lastName ?: "",
        )
        ProfileLabeledAttribute(
            isLoading = viewState.profileViewState is ProfileViewState.Loading,
            label = stringResource(Res.string.profile_screen_username_user_attribute),
            modifier = Modifier.fillMaxWidth(),
            value = (viewState.profileViewState as? ProfileViewState.Loaded)
                ?.profile?.username ?: "",
        )
        ProfileLabeledAttribute(
            isLoading = viewState.profileViewState is ProfileViewState.Loading,
            label = stringResource(Res.string.profile_screen_email_user_attribute),
            modifier = Modifier.fillMaxWidth(),
            value = (viewState.profileViewState as? ProfileViewState.Loaded)?.profile?.email ?: "",
        )
        ProfileLabeledAttribute(
            isLoading = viewState.profileViewState is ProfileViewState.Loading,
            label = stringResource(Res.string.profile_screen_date_of_birth_user_attribute),
            modifier = Modifier.fillMaxWidth(),
            value = (viewState.profileViewState as? ProfileViewState.Loaded)
                ?.profile?.dateOfBirth?.toDisplayDateFormat() ?: "",
        )

        Spacer(modifier = Modifier.weight(1.0f))

        PurchaseHistoryCtaButton(
            isEnabled = viewState.profileViewState is ProfileViewState.Loaded
                    || viewState.profileViewState is ProfileViewState.LoadingFailed,
            modifier = Modifier.fillMaxWidth(),
            onClickAction = {},
        )
    }
}

@Composable
internal fun ProfileLabeledAttribute(
    label: String,
    value: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().placeholder(isLoading),
            style = Material3Typography.labelMedium,
            text = label,
        )

        Text(
            modifier = Modifier.fillMaxWidth().placeholder(isLoading),
            style = Material3Typography.bodyLarge,
            text = value,
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun PurchaseHistoryCtaButton(
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit = {},
) {
    Button(
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        enabled = isEnabled,
        modifier = modifier,
        onClick = onClickAction,
    ) {
        Icon(
            contentDescription = null,
            modifier = Modifier.size(COMMON_BUTTON_ICON_EDGE_SIZE),
            painter = painterResource(Res.drawable.ic_deployed_code_history),
        )
        Spacer(Modifier.width(COMMON_BUTTON_CONTENT_SPACE))
        Text(stringResource(Res.string.profile_screen_purchase_history_button_text))
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun MediumAndExpandedWidthProfileScreen(
    component: ProfileComponent,
    modifier: Modifier = Modifier,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()
    val standardSpace = getStandardSpace(windowWidthSizeClass)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(standardSpace),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(standardSpace),
            modifier = Modifier.fillMaxWidth(),
        ) {
            ProfileLabeledAttribute(
                isLoading = viewState.profileViewState is ProfileViewState.Loading,
                label = stringResource(Res.string.profile_screen_first_name_user_attribute),
                modifier = Modifier.weight(0.5f),
                value = (viewState.profileViewState as? ProfileViewState.Loaded)
                    ?.profile?.firstName ?: "",
            )
            ProfileLabeledAttribute(
                isLoading = viewState.profileViewState is ProfileViewState.Loading,
                label = stringResource(Res.string.profile_screen_last_name_user_attribute),
                modifier = Modifier.weight(0.5f),
                value = (viewState.profileViewState as? ProfileViewState.Loaded)
                    ?.profile?.lastName ?: "",
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(standardSpace),
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileLabeledAttribute(
                isLoading = viewState.profileViewState is ProfileViewState.Loading,
                label = stringResource(Res.string.profile_screen_username_user_attribute),
                modifier = Modifier.weight(0.5f),
                value = (viewState.profileViewState as? ProfileViewState.Loaded)
                    ?.profile?.username ?: "",
            )
            ProfileLabeledAttribute(
                isLoading = viewState.profileViewState is ProfileViewState.Loading,
                label = stringResource(Res.string.profile_screen_email_user_attribute),
                modifier = Modifier.weight(0.5f),
                value = (viewState.profileViewState as? ProfileViewState.Loaded)?.profile?.email
                    ?: "",
            )
        }

        ProfileLabeledAttribute(
            isLoading = viewState.profileViewState is ProfileViewState.Loading,
            label = stringResource(Res.string.profile_screen_date_of_birth_user_attribute),
            modifier = Modifier.fillMaxWidth(),
            value = (viewState.profileViewState as? ProfileViewState.Loaded)
                ?.profile?.dateOfBirth?.toDisplayDateFormat() ?: "",
        )

        Spacer(modifier = Modifier.weight(1.0f))

        // TODO rossz oldal? + Modify screen
        PurchaseHistoryCtaButton(
            isEnabled = viewState.profileViewState is ProfileViewState.Loaded
                    || viewState.profileViewState is ProfileViewState.LoadingFailed,
            modifier = Modifier.fillMaxWidth(0.5f).padding(end = standardSpace / 2),
            onClickAction = {},
        )
    }
}