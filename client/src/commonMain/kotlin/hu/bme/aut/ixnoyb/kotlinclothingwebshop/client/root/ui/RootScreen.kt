package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.ChildStack
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui.ArticleBrowsingRootScreen
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui.ArticleBrowsingRootScreenTopAppBar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui.CompactArticleBrowsingRootFloatingActionButton
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui.MediumAndExpandedArticleBrowsingRootFloatingActionButton
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.getStandardSpace
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.ui.OrderHistoryScreen
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.ui.OrderHistoryScreenTopAppbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.ui.OrderingRootScreen
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.ui.OrderingRootScreenTopAppbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme.KotlinClothingWebshopTheme
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.viewlogic.RootComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.UserManagementRootScreen
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.UserManagementRootScreenTopAppBar
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.ic_apparel
import kotlinclothingwebshop.client.generated.resources.navigation_bar_checkout_feature_item_label
import kotlinclothingwebshop.client.generated.resources.navigation_bar_clothing_browser_feature_item_label
import kotlinclothingwebshop.client.generated.resources.navigation_bar_user_management_feature_item_label
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
internal fun RootScreen(component: RootComponent) {
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
@OptIn(
    ExperimentalResourceApi::class, ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalMaterial3Api::class
)
internal fun RootScaffold(component: RootComponent, modifier: Modifier = Modifier) {
    val rootChild by component.childStack.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val widthSizeClass = calculateWindowSizeClass().widthSizeClass

    Scaffold(
        bottomBar = {
            if (widthSizeClass == Compact) {
                NavigationBar {
                    val child = rootChild.items.lastOrNull()?.instance

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
                        selected = child is RootComponent.Child.OrderingFeature,
                        onClick = component::selectOrderingFeature,
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
        floatingActionButton = {
            if (widthSizeClass == Compact) {
                CompactApplicationFloatingActionButton(rootChild)
            }
        },
        modifier = modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Children(
                stack = rootChild,
                animation = stackAnimation(fade())
            ) { childContainer ->

                when (val child = childContainer.instance) {
                    is RootComponent.Child.ArticleBrowserFeature -> {
                        ArticleBrowsingRootScreenTopAppBar(child.component, topAppBarScrollBehavior)
                    }

                    is RootComponent.Child.OrderHistoryFeature -> {
                        OrderHistoryScreenTopAppbar(child.component, topAppBarScrollBehavior)
                    }

                    is RootComponent.Child.OrderingFeature -> {
                        OrderingRootScreenTopAppbar(child.component, topAppBarScrollBehavior)
                    }

                    is RootComponent.Child.UserManagementFeature -> {
                        UserManagementRootScreenTopAppBar(
                            component = child.component,
                            topAppBarScrollBehavior = topAppBarScrollBehavior,
                        )
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
                    val child = rootChild.items.lastOrNull()?.instance

                    MediumAndExpandedApplicationFloatingActionButton(rootChild)

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
                        selected = child is RootComponent.Child.OrderingFeature,
                        onClick = component::selectOrderingFeature,
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
                stack = rootChild,
            ) { childContainer ->
                when (val child = childContainer.instance) {
                    is RootComponent.Child.ArticleBrowserFeature -> {
                        ArticleBrowsingRootScreen(
                            component = child.component,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    is RootComponent.Child.OrderHistoryFeature -> {
                        OrderHistoryScreen(
                            component = child.component,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    is RootComponent.Child.OrderingFeature -> {
                        OrderingRootScreen(
                            component = child.component,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    is RootComponent.Child.UserManagementFeature -> {
                        UserManagementRootScreen(
                            component = child.component,
                            snackbarHostState = snackbarHostState,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun CompactApplicationFloatingActionButton(
    childStack: ChildStack<*, RootComponent.Child>,
    modifier: Modifier = Modifier,
) {
    Children(
        animation = stackAnimation(fade()),
        modifier = modifier,
        stack = childStack,
    ) { childContainer ->
        val child = childContainer.instance
        if (child is RootComponent.Child.ArticleBrowserFeature) {
            CompactArticleBrowsingRootFloatingActionButton(child.component)
        }
    }
}

@Composable
internal fun MediumAndExpandedApplicationFloatingActionButton(
    childStack: ChildStack<*, RootComponent.Child>,
    modifier: Modifier = Modifier,
) {
    Children(
        animation = stackAnimation(fade()),
        modifier = modifier,
        stack = childStack,
    ) { childContainer ->
        val child = childContainer.instance
        if (child is RootComponent.Child.ArticleBrowserFeature) {
            MediumAndExpandedArticleBrowsingRootFloatingActionButton(child.component)
        }
    }
}