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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_CONTENT_SPACE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_ICON_EDGE_SIZE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.LoadingSection
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.PlatformSpecificVerticalListScrollbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.formatAmount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.getStandardSpace
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.BasketComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.BasketComponent.ViewState.BasketItemsAvailable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.BasketComponent.ViewState.Loading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.model.OrderItem
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme.Material3Typography
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.basket_screen_amount_template
import kotlinclothingwebshop.client.generated.resources.basket_screen_content_label
import kotlinclothingwebshop.client.generated.resources.basket_screen_cta_button_text
import kotlinclothingwebshop.client.generated.resources.basket_screen_top_appbar_title
import kotlinclothingwebshop.client.generated.resources.basket_screen_total_amount_label
import kotlinclothingwebshop.client.generated.resources.ic_shopping_cart_checkout
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
internal fun BasketScreenTopAppbar(
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        scrollBehavior = topAppBarScrollBehavior,
        title = { Text(stringResource(Res.string.basket_screen_top_appbar_title)) },
    )
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalResourceApi::class)
internal fun BasketScreen(component: BasketComponent, modifier: Modifier = Modifier) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    val standardSpace = getStandardSpace(calculateWindowSizeClass().widthSizeClass)

    (viewState as? Loading)?.let {
        LoadingSection(modifier)
    } ?: (viewState as? BasketItemsAvailable)?.let { basketItemsAvailableState ->
        Box {
            val lazyListState = rememberLazyListState()

            LazyColumn(
                modifier = modifier.padding(standardSpace),
                state = lazyListState,
                verticalArrangement = object : Arrangement.Vertical {
                    override fun Density.arrange(
                        totalSize: Int,
                        sizes: IntArray,
                        outPositions: IntArray
                    ) {
                        // https://stackoverflow.com/questions/68959841
                        var currentOffset = 0

                        sizes.forEachIndexed { index, size ->
                            if (index == sizes.lastIndex) {
                                outPositions[index] = totalSize - size
                            } else {
                                outPositions[index] = currentOffset
                                currentOffset += size
                            }
                        }
                    }
                },
            ) {
                item {
                    Text(
                        style = Material3Typography.labelMedium,
                        text = stringResource(Res.string.basket_screen_content_label),
                    )

                    Spacer(Modifier.height(8.dp))
                }

                items(basketItemsAvailableState.items, key = { it.id }) { basketItem ->
                    BasketItemListItem(
                        orderItem = basketItem,
                        deleteItemAction = { itemID -> component.deleteBasketItem(itemID) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                item {
                    Spacer(Modifier.height(standardSpace))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(standardSpace),
                    ) {
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
                                    basketItemsAvailableState
                                        .items
                                        .fold(0) { acc, basketItem -> acc + basketItem.price }
                                        .formatAmount()
                                )
                            )
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { component.navigateToOrderFinalization() },
                        ) {
                            Icon(
                                contentDescription = null,
                                modifier = Modifier
                                    .width(COMMON_BUTTON_ICON_EDGE_SIZE)
                                    .height(COMMON_BUTTON_ICON_EDGE_SIZE),
                                painter = painterResource(Res.drawable.ic_shopping_cart_checkout),
                            )
                            Spacer(Modifier.width(COMMON_BUTTON_CONTENT_SPACE))
                            Text(stringResource(Res.string.basket_screen_cta_button_text))
                        }
                    }
                }
            }

            PlatformSpecificVerticalListScrollbar(
                listState = lazyListState,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
            )
        }
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun BasketItemListItem(
    orderItem: OrderItem,
    modifier: Modifier = Modifier,
    deleteItemAction: (String) -> Unit = {},
) {
    Column(modifier) {
        with(orderItem) {
            ListItem(
                headlineContent = {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = Material3Typography.bodyLarge,
                        text = name,
                    )
                },
                leadingContent = {
                    AsyncImage(
                        contentDescription = null,
                        model = imageUrl,
                        modifier = Modifier.size(56.dp),
                    )
                },
                supportingContent = {
                    Text(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = Material3Typography.bodyMedium,
                        text = stringResource(
                            Res.string.basket_screen_amount_template,
                            price.formatAmount(),
                        ),
                    )
                },
                trailingContent = {
                    IconButton(
                        onClick = { deleteItemAction(id) }
                    ) {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Default.Delete,
                        )
                    }
                },
            )
        }
        HorizontalDivider()
    }
}