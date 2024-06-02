package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.element

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.ic_calendar_today
import kotlinclothingwebshop.client.generated.resources.ic_visibility
import kotlinclothingwebshop.client.generated.resources.ic_visibility_off
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

// TODO experiment with TextField parameters, UX might be improved with them

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun DateOfBirthTextField(
    value: String,
    isEnabled: Boolean = true,
    labelValue: String = "",
    modifier: Modifier = Modifier,
    selectDateAction: () -> Unit = {},
) {
    TextField(
        enabled = isEnabled,
        label = { if (labelValue.isNotEmpty()) Text(labelValue) },
        modifier = modifier,
        onValueChange = { /*No-op*/ },
        readOnly = true,
        trailingIcon = {
            Icon(
                contentDescription = null,
                painter = painterResource(Res.drawable.ic_calendar_today),
                modifier = Modifier.clickable(onClick = selectDateAction),
            )
        },
        value = value,
    )
}

@Composable
internal fun EmailTextField(
    onValueChange: (String) -> Unit,
    value: String,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    labelValue: String = "",
    modifier: Modifier = Modifier,
    supportingTextValue: String = "",
) {
    TextField(
        enabled = isEnabled,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        label = { if (labelValue.isNotEmpty()) Text(labelValue) },
        modifier = modifier,
        onValueChange = onValueChange,
        singleLine = true,
        supportingText = { if (supportingTextValue.isNotEmpty()) Text(supportingTextValue) },
        value = value,
    )
}

@Composable
internal fun NamePartTextField(
    isEnabled: Boolean = true,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
    value: String,
    labelValue: String = "",
    modifier: Modifier = Modifier,
    supportingTextValue: String = "",
) {
    TextField(
        enabled = isEnabled,
        isError = isError,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            capitalization = KeyboardCapitalization.Words,
        ),
        label = { if (labelValue.isNotEmpty()) Text(labelValue) },
        modifier = modifier,
        onValueChange = onValueChange,
        singleLine = true,
        supportingText = {
            if (supportingTextValue.isNotEmpty()) {
                Text(supportingTextValue)
            }
        },
        value = value,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun PasswordTextField(
    changePasswordVisibilityAction: () -> Unit,
    isPasswordHidden: Boolean,
    onValueChange: (String) -> Unit,
    value: String,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    labelValue: String = "",
    modifier: Modifier = Modifier,
    supportingTextValue: String = "",
) {
    TextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        enabled = isEnabled,
        isError = isError,
        label = {
            if (labelValue.isNotEmpty()) {
                Text(labelValue)
            }
        },
        modifier = modifier,
        onValueChange = onValueChange,
        singleLine = true,
        supportingText = {
            if (supportingTextValue.isNotEmpty()) {
                Text(supportingTextValue)
            }
        },
        trailingIcon = {
            IconButton(
                onClick = changePasswordVisibilityAction,
            ) {
                Icon(
                    contentDescription = null,
                    painter = painterResource(
                        if (isPasswordHidden) {
                            Res.drawable.ic_visibility_off
                        } else {
                            Res.drawable.ic_visibility
                        }
                    ),
                )
            }
        },
        visualTransformation = if (isPasswordHidden) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        value = value,
    )
}

@Composable
internal fun UsernameTextField(
    isEnabled: Boolean = true,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
    value: String,
    labelValue: String = "",
    modifier: Modifier = Modifier,
    supportingTextValue: String = "",
) {
    TextField(
        enabled = isEnabled,
        isError = isError,
        keyboardOptions = KeyboardOptions(autoCorrect = false),
        label = { if (labelValue.isNotEmpty()) Text(labelValue) },
        modifier = modifier,
        onValueChange = onValueChange,
        singleLine = true,
        supportingText = {
            if (supportingTextValue.isNotEmpty()) {
                Text(supportingTextValue)
            }
        },
        value = value,
    )
}