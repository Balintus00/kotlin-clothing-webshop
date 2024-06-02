package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.LoadingSection
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.PlatformSpecificVerticalGridScrollbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.PlatformSpecificVerticalListScrollbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.formatAmount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.toDisplayDateTimeFormat
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.getStandardSpace
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.OrderHistoryComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.OrderHistoryComponent.ViewState.HistoryLoaded
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.OrderHistoryComponent.ViewState.Loading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.model.Order
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.model.OrderItem
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme.Material3Typography
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.basket_screen_amount_template
import kotlinclothingwebshop.client.generated.resources.ic_expand_less
import kotlinclothingwebshop.client.generated.resources.ic_expand_more
import kotlinclothingwebshop.client.generated.resources.order_history_screen_order_history_card_item_count_template
import kotlinclothingwebshop.client.generated.resources.order_history_screen_top_appbar_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
internal fun OrderHistoryScreenTopAppbar(
    component: OrderHistoryComponent,
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
        title = { Text(stringResource(Res.string.order_history_screen_top_appbar_title)) },
    )

}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal fun OrderHistoryScreen(component: OrderHistoryComponent, modifier: Modifier = Modifier) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    (viewState as? Loading)?.let {
        LoadingSection(modifier)
    } ?: (viewState as? HistoryLoaded)?.let { historyLoadedState ->

        val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass
        val standardSpace = getStandardSpace(windowWidthSizeClass)

        if (windowWidthSizeClass == Expanded) {
            Box {
                val gridState = rememberLazyGridState()

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(standardSpace),
                    modifier = modifier.padding(standardSpace),
                    state = gridState,
                    verticalArrangement = Arrangement.spacedBy(standardSpace),
                ) {
                    items(historyLoadedState.orderHistory) {
                        OrderHistoryOrderCard(
                            modifier = Modifier.fillMaxWidth(),
                            order = it,
                        )
                    }
                }

                PlatformSpecificVerticalGridScrollbar(
                    gridState = gridState,
                    modifier = Modifier.fillMaxHeight().align(CenterEnd),
                )
            }
        } else {
            Box {
                val listState = rememberLazyListState()

                LazyColumn(
                    modifier = modifier.padding(standardSpace),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(standardSpace),
                ) {
                    items(historyLoadedState.orderHistory) {
                        OrderHistoryOrderCard(
                            modifier = Modifier.fillMaxWidth(),
                            order = it,
                        )
                    }
                }

                PlatformSpecificVerticalListScrollbar(
                    listState = listState,
                    modifier = Modifier.fillMaxHeight().align(CenterEnd),
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
internal fun OrderHistoryOrderCard(
    order: Order,
    modifier: Modifier = Modifier,
) {
    val standardSpace = getStandardSpace(calculateWindowSizeClass().widthSizeClass)

    OutlinedCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(standardSpace),
        ) {
            var areItemsVisible by remember { mutableStateOf(false) }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1.0f)
                ) {
                    Text(
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = Ellipsis,
                        style = Material3Typography.bodyLarge,
                        text = order.dateTime.toDisplayDateTimeFormat(),
                    )

                    Text(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = Ellipsis,
                        style = Material3Typography.bodyMedium,
                        text = stringResource(
                            Res.string.order_history_screen_order_history_card_item_count_template,
                            order.items.size,
                        ),
                    )
                }

                IconButton(
                    onClick = { areItemsVisible = areItemsVisible.not() }
                ) {
                    Icon(
                        contentDescription = null,
                        painter = painterResource(
                            if (areItemsVisible) {
                                Res.drawable.ic_expand_less
                            } else {
                                Res.drawable.ic_expand_more
                            }
                        )
                    )
                }
            }

            AnimatedVisibility(areItemsVisible) {
                HorizontalDivider()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 73.dp, max = (20 * 73).dp) // TODO
                        .padding(standardSpace),
                ) {
                    items(order.items) {
                        OrderHistoryOrderItemListItem(
                            modifier = Modifier.fillMaxWidth(),
                            orderItem = it,
                        )
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun OrderHistoryOrderItemListItem(
    orderItem: OrderItem,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        with(orderItem) {
            ListItem(
                headlineContent = {
                    Text(
                        maxLines = 1,
                        overflow = Ellipsis,
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
            )
        }
        HorizontalDivider()
    }
}