package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_CONTENT_SPACE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_ICON_EDGE_SIZE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.LoadingSection
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.PlatformSpecificVerticalListScrollbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.formatAmount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.getStandardSpace
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.FinalizeOrderComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.FinalizeOrderComponent.ViewState.AmountAvailable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.FinalizeOrderComponent.ViewState.LoadingAmount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.model.OrderAddress
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme.Material3Typography
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.basket_screen_amount_template
import kotlinclothingwebshop.client.generated.resources.basket_screen_total_amount_label
import kotlinclothingwebshop.client.generated.resources.finalize_order_screen_city_input_label
import kotlinclothingwebshop.client.generated.resources.finalize_order_screen_country_input_label
import kotlinclothingwebshop.client.generated.resources.finalize_order_screen_cta_button_text
import kotlinclothingwebshop.client.generated.resources.finalize_order_screen_street_input_label
import kotlinclothingwebshop.client.generated.resources.finalize_order_screen_top_appbar_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
internal fun FinalizeOrderScreenTopAppbar(
    component: FinalizeOrderComponent,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = component::navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        scrollBehavior = topAppBarScrollBehavior,
        title = { Text(stringResource(Res.string.finalize_order_screen_top_appbar_title)) },
    )
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalResourceApi::class)
internal fun FinalizeOrderScreen(
    component: FinalizeOrderComponent,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    (viewState as? LoadingAmount)?.let {
        LoadingSection(modifier)
    } ?: (viewState as? AmountAvailable)?.let { amountAvailableState ->
        Box {
            val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass
            val standardSpace = getStandardSpace(windowWidthSizeClass)

            val verticalScrollState = rememberScrollState()

            Column(
                modifier = modifier.verticalScroll(verticalScrollState).padding(standardSpace),
                verticalArrangement = Arrangement.spacedBy(standardSpace),
            ) {
                var country by remember { mutableStateOf("") }
                var city by remember { mutableStateOf("") }
                var street by remember { mutableStateOf("") }

                if (windowWidthSizeClass == Expanded) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardSpace),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        TextField(
                            label = {
                                Text(
                                    stringResource(
                                        Res.string.finalize_order_screen_country_input_label
                                    )
                                )
                            },
                            modifier = Modifier.weight(0.5f),
                            onValueChange = { country = it },
                            singleLine = true,
                            value = country,
                        )

                        TextField(
                            label = {
                                Text(
                                    stringResource(
                                        Res.string.finalize_order_screen_city_input_label
                                    )
                                )
                            },
                            modifier = Modifier.weight(0.5f),
                            onValueChange = { city = it },
                            singleLine = true,
                            value = city,
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(standardSpace),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Spacer(Modifier.weight(0.5f))

                        TextField(
                            label = {
                                Text(
                                    stringResource(
                                        Res.string.finalize_order_screen_street_input_label
                                    )
                                )
                            },
                            modifier = Modifier.weight(0.5f),
                            onValueChange = { street = it },
                            singleLine = true,
                            value = street,
                        )
                    }
                } else {
                    TextField(
                        label = {
                            Text(
                                stringResource(Res.string.finalize_order_screen_country_input_label)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { country = it },
                        singleLine = true,
                        value = country,
                    )

                    TextField(
                        label = {
                            Text(stringResource(Res.string.finalize_order_screen_city_input_label))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { city = it },
                        singleLine = true,
                        value = city,
                    )

                    TextField(
                        label = {
                            Text(
                                stringResource(Res.string.finalize_order_screen_street_input_label)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { street = it },
                        singleLine = true,
                        value = street,
                    )
                }

                Spacer(Modifier.weight(1.0f))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(standardSpace),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        style = Material3Typography.labelSmall,
                        text = stringResource(Res.string.basket_screen_total_amount_label)
                    )

                    Text(
                        style = Material3Typography.bodyLarge,
                        text = stringResource(
                            Res.string.basket_screen_amount_template,
                            amountAvailableState.totalAmount.formatAmount(),
                        )
                    )
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        component.placeOrder(
                            OrderAddress(
                                country = country,
                                city = city,
                                street = street,
                            )
                        )
                    },
                ) {
                    Icon(
                        contentDescription = null,
                        imageVector = Icons.Default.Check,
                        modifier = Modifier
                            .width(COMMON_BUTTON_ICON_EDGE_SIZE)
                            .height(COMMON_BUTTON_ICON_EDGE_SIZE),
                    )
                    Spacer(Modifier.width(COMMON_BUTTON_CONTENT_SPACE))
                    Text(stringResource(Res.string.finalize_order_screen_cta_button_text))
                }
            }

            PlatformSpecificVerticalListScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                scrollState = verticalScrollState,
            )
        }
    }
}