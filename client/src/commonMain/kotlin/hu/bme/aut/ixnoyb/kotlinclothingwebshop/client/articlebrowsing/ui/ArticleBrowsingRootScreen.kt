package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.ArticleBrowsingRootComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArticleBrowsingRootScreenTopAppBar(
    component: ArticleBrowsingRootComponent,
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
            is ArticleBrowsingRootComponent.Child.ArticleDetails -> {
                ArticleDetailsScreenTopAppbar(
                    component = child.component,
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                )
            }

            is ArticleBrowsingRootComponent.Child.FeaturedArticles -> {
                FeaturedArticlesScreenTopAppbar(
                    component = child.component,
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                )
            }

            is ArticleBrowsingRootComponent.Child.SearchArticles -> {
                SearchArticlesScreenTopAppbar(
                    component = child.component,
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                )
            }

            is ArticleBrowsingRootComponent.Child.SearchArticlesResult -> {
                SearchArticlesResultScreenTopAppbar(
                    component = child.component,
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                )
            }
        }
    }
}

@Composable
internal fun ArticleBrowsingRootScreen(
    component: ArticleBrowsingRootComponent,
    modifier: Modifier = Modifier,
) {
    val rootChild by component.childStack.collectAsStateWithLifecycle()

    Children(
        animation = stackAnimation(slide()),
        modifier = modifier,
        stack = rootChild,
    ) { childContainer ->
        when (val child = childContainer.instance) {
            is ArticleBrowsingRootComponent.Child.ArticleDetails -> {
                ArticleDetailsScreen(child.component, Modifier.fillMaxSize())
            }

            is ArticleBrowsingRootComponent.Child.FeaturedArticles -> {
                FeaturedArticlesScreen(child.component, Modifier.fillMaxSize())
            }

            is ArticleBrowsingRootComponent.Child.SearchArticles -> {
                SearchArticlesScreen(child.component, Modifier.fillMaxSize())
            }

            is ArticleBrowsingRootComponent.Child.SearchArticlesResult -> {
                SearchArticlesResultScreen(child.component, Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
internal fun CompactArticleBrowsingRootFloatingActionButton(
    component: ArticleBrowsingRootComponent,
    modifier: Modifier = Modifier,
) {
    val rootChild by component.childStack.collectAsStateWithLifecycle()

    Children(
        animation = stackAnimation(fade()),
        modifier = modifier,
        stack = rootChild,
    ) { childContainer ->
        val child = childContainer.instance
        if (child is ArticleBrowsingRootComponent.Child.ArticleDetails) {
            CompactArticleDetailsFloatingActionButton(child.component)
        }
    }
}

@Composable
internal fun MediumAndExpandedArticleBrowsingRootFloatingActionButton(
    component: ArticleBrowsingRootComponent,
    modifier: Modifier = Modifier,
) {
    val rootChild by component.childStack.collectAsStateWithLifecycle()

    Children(
        animation = stackAnimation(fade()),
        modifier = modifier,
        stack = rootChild,
    ) { childContainer ->
        val child = childContainer.instance
        if (child is ArticleBrowsingRootComponent.Child.ArticleDetails) {
            MediumAndExpandedArticleDetailsFloatingActionButton(child.component)
        }
    }
}