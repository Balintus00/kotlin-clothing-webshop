package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui.RecommendedArticlesScreen
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui.RecommendedArticlesScreenTopAppbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme.KotlinClothingWebshopTheme
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.viewlogic.RootComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.utility.ui.getStandardSpace
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.ic_apparel
import kotlinclothingwebshop.client.generated.resources.navigation_bar_checkout_feature_item_label
import kotlinclothingwebshop.client.generated.resources.navigation_bar_clothing_browser_feature_item_label
import kotlinclothingwebshop.client.generated.resources.navigation_bar_user_management_feature_item_label
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
internal fun UserInterface(component: RootComponent) {
    KotlinClothingWebshopTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            RootScaffold(component, Modifier.fillMaxSize())
        }
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
internal fun RootScaffold(component: RootComponent, modifier: Modifier = Modifier) {
    val rootChild = component.childStack.collectAsState()

    var selectedCustomerId by remember { mutableStateOf("") }   // TODO remove

    val widthSizeClass = calculateWindowSizeClass().widthSizeClass

    Scaffold(
        bottomBar = {
            if (widthSizeClass == Compact) {
                NavigationBar {
                    val child = rootChild.value.items.lastOrNull()?.instance

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.ic_apparel),
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(
                                stringResource(
                                    Res.string.navigation_bar_clothing_browser_feature_item_label
                                )
                            )
                        },
                        selected = child is RootComponent.Child.ArticleBrowserFeature,
                        onClick = component::selectArticleBrowserFeature,
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(
                                stringResource(
                                    Res.string.navigation_bar_checkout_feature_item_label
                                )
                            )
                        },
                        selected = child is RootComponent.Child.CheckoutFeature,
                        onClick = component::selectCheckoutFeature,
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(
                                stringResource(
                                    Res.string.navigation_bar_user_management_feature_item_label
                                )
                            )
                        },
                        selected = child is RootComponent.Child.UserManagementFeature,
                        onClick = component::selectUserManagementFeature,
                    )
                }
            }
        },
        modifier = modifier,
        topBar = {
            Children(
                stack = rootChild.value,
                animation = stackAnimation(fade())
            ) { childContainer ->

                when (val child = childContainer.instance) {
                    is RootComponent.Child.ArticleBrowserFeature -> {
                        RecommendedArticlesScreenTopAppbar(
                            selectCustomerIdAction = { selectedCustomerId = it },
                        )
                    }

                    is RootComponent.Child.CheckoutFeature -> {
                        // TODO
                    }

                    is RootComponent.Child.UserManagementFeature -> {
                        // TODO
                    }
                }
            }
        },
    ) { scaffoldPadding ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(getStandardSpace(widthSizeClass)),
            modifier = Modifier.fillMaxSize().padding(scaffoldPadding),
        ) {
            if ((widthSizeClass == Compact).not()) {
                NavigationRail(
                    modifier = Modifier.fillMaxHeight().padding(vertical = 56.dp),
                ) {
                    val child = rootChild.value.items.lastOrNull()?.instance

                    NavigationRailItem(
                        icon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.ic_apparel),
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(
                                stringResource(
                                    Res.string.navigation_bar_clothing_browser_feature_item_label
                                )
                            )
                        },
                        selected = child is RootComponent.Child.ArticleBrowserFeature,
                        onClick = component::selectArticleBrowserFeature,
                    )
                    NavigationRailItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(
                                stringResource(
                                    Res.string.navigation_bar_checkout_feature_item_label
                                )
                            )
                        },
                        selected = child is RootComponent.Child.CheckoutFeature,
                        onClick = component::selectCheckoutFeature,
                    )
                    NavigationRailItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(
                                stringResource(
                                    Res.string.navigation_bar_user_management_feature_item_label
                                )
                            )
                        },
                        selected = child is RootComponent.Child.UserManagementFeature,
                        onClick = component::selectUserManagementFeature,
                    )
                }
            }

            Children(
                animation = stackAnimation(fade()),
                modifier = Modifier.fillMaxSize(),
                stack = rootChild.value,
            ) { childContainer ->
                when (val child = childContainer.instance) {
                    is RootComponent.Child.ArticleBrowserFeature -> {
                        RecommendedArticlesScreen(
                            customerId = selectedCustomerId,
                            modifier = Modifier.padding(scaffoldPadding).fillMaxSize(),
                        )
                    }

                    is RootComponent.Child.CheckoutFeature -> {
                        TodoScreen(modifier = Modifier.padding(scaffoldPadding).fillMaxSize())
                    }

                    is RootComponent.Child.UserManagementFeature -> {
                        TodoScreen(modifier = Modifier.padding(scaffoldPadding).fillMaxSize())
                    }
                }
            }
        }
    }
}

// TODO remove
@Composable
private fun TodoScreen(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Text("TODO")
    }
}