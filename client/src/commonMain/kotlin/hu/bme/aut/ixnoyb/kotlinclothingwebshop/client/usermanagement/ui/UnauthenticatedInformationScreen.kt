package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_CONTENT_SPACE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_ICON_EDGE_SIZE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.getStandardSpace
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme.Material3Typography
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.image.rememberAuthenticateImage
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.UnauthenticatedInformationComponent
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.ic_add_box
import kotlinclothingwebshop.client.generated.resources.ic_door_open
import kotlinclothingwebshop.client.generated.resources.unauthenticated_information_screen_description
import kotlinclothingwebshop.client.generated.resources.unauthenticated_information_screen_login_button_text
import kotlinclothingwebshop.client.generated.resources.unauthenticated_information_screen_registration_button_text
import kotlinclothingwebshop.client.generated.resources.unauthenticated_information_screen_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal fun UnauthenticatedInformationScreenInformationPart(
    component: UnauthenticatedInformationComponent,
    modifier: Modifier = Modifier,
) {
    val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass

    val standardSpace = getStandardSpace(windowWidthSizeClass)

    if (windowWidthSizeClass == Expanded) {
        ExpandedWidthUnauthenticatedInformationScreen(
            modifier = modifier.padding(standardSpace),
            navigateToLoginAction = component::navigateToLogin,
            navigateToRegistrationAction = component::navigateToRegistration,
        )
    } else {
        UnauthenticatedInformationScreenInformationPart(
            modifier = modifier.padding(standardSpace),
            navigateToLoginAction = component::navigateToLogin,
            navigateToRegistrationAction = component::navigateToRegistration,
        )
    }
}

@Composable
internal fun ExpandedWidthUnauthenticatedInformationScreen(
    modifier: Modifier = Modifier,
    navigateToLoginAction: () -> Unit = {},
    navigateToRegistrationAction: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(getStandardSpace(Expanded)),
        modifier = modifier,
    ) {
        UnauthenticatedInformationScreenInformationPart(
            modifier = Modifier.weight(0.5f).fillMaxHeight(),
            navigateToLoginAction = navigateToLoginAction,
            navigateToRegistrationAction = navigateToRegistrationAction,
        )

        Image(
            contentDescription = null,
            imageVector = rememberAuthenticateImage(),
            modifier = Modifier.weight(0.5f).fillMaxHeight(),
        )
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
internal fun UnauthenticatedInformationScreenInformationPart(
    modifier: Modifier = Modifier,
    navigateToLoginAction: () -> Unit = {},
    navigateToRegistrationAction: () -> Unit = {},
) {
    val standardSpace = getStandardSpace(calculateWindowSizeClass().widthSizeClass)

    Column(modifier = modifier) {
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(standardSpace),
        ) {
            Text(
                color = MaterialTheme.colorScheme.primary,
                style = Material3Typography.headlineMedium,
                text = stringResource(Res.string.unauthenticated_information_screen_title),
            )
            Text(
                style = Material3Typography.bodyMedium,
                text = stringResource(Res.string.unauthenticated_information_screen_description),
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(standardSpace),
        ) {
            Button(
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                ),
                modifier = Modifier.fillMaxWidth(),
                onClick = navigateToRegistrationAction,
            ) {
                Icon(
                    modifier = Modifier
                        .width(COMMON_BUTTON_ICON_EDGE_SIZE)
                        .height(COMMON_BUTTON_ICON_EDGE_SIZE),
                    painter = painterResource(Res.drawable.ic_add_box),
                    contentDescription = null,
                )
                Spacer(Modifier.width(COMMON_BUTTON_CONTENT_SPACE))
                Text(
                    stringResource(
                        Res.string.unauthenticated_information_screen_registration_button_text
                    )
                )
            }

            Button(
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier.fillMaxWidth(),
                onClick = navigateToLoginAction,
            ) {
                Icon(
                    modifier = Modifier
                        .width(COMMON_BUTTON_ICON_EDGE_SIZE)
                        .height(COMMON_BUTTON_ICON_EDGE_SIZE),
                    painter = painterResource(Res.drawable.ic_door_open),
                    contentDescription = null,
                )
                Spacer(Modifier.width(COMMON_BUTTON_CONTENT_SPACE))
                Text(
                    stringResource(Res.string.unauthenticated_information_screen_login_button_text)
                )
            }
        }
    }
}