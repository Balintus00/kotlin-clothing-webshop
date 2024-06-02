package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.OrderingRootComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OrderingRootScreenTopAppbar(
    component: OrderingRootComponent,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    val rootChild by component.childStack.collectAsStateWithLifecycle()

    Children(
        animation = stackAnimation(fade()),
        modifier = modifier,
        stack = rootChild,
    ) { childContainer ->
        when (val child = childContainer.instance) {
            is OrderingRootComponent.Child.Basket -> {
                BasketScreenTopAppbar(
                    modifier = modifier,
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                )
            }

            is OrderingRootComponent.Child.OrderFinalization -> {
                FinalizeOrderScreenTopAppbar(
                    component = child.component,
                    modifier = modifier,
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                )
            }
        }
    }
}

@Composable
internal fun OrderingRootScreen(component: OrderingRootComponent, modifier: Modifier = Modifier) {
    val rootChild by component.childStack.collectAsStateWithLifecycle()

    Children(
        animation = stackAnimation(slide()),
        modifier = modifier,
        stack = rootChild,
    ) { childContainer ->
        when (val child = childContainer.instance) {
            is OrderingRootComponent.Child.Basket -> {
                BasketScreen(child.component, modifier)
            }

            is OrderingRootComponent.Child.OrderFinalization -> {
                FinalizeOrderScreen(child.component, modifier)
            }
        }
    }

}