package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.FeaturedArticlesComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.FeaturedArticlesComponent.ViewState.ArticlePreviewsAvailable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.FeaturedArticlesComponent.ViewState.Loading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.ArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.PlatformSpecificVerticalGridScrollbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.PlatformSpecificVerticalListScrollbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.getStandardSpace
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme.Material3Typography
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.article_preview_card_more_details_button_text
import kotlinclothingwebshop.client.generated.resources.featured_articles_screen_top_appbar_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
internal fun FeaturedArticlesScreenTopAppbar(
    component: FeaturedArticlesComponent,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        actions = {
            IconButton(
                onClick = { component.navigateToArticleSearch() },
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                )
            }
        },
        modifier = modifier,
        scrollBehavior = topAppBarScrollBehavior,
        title = {
            Text(stringResource(Res.string.featured_articles_screen_top_appbar_title))
        },
    )
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal fun FeaturedArticlesScreen(
    component: FeaturedArticlesComponent,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass

    val standardSpace = getStandardSpace(windowWidthSizeClass)

    (viewState as? ArticlePreviewsAvailable)?.let { previewsAvailableState ->
        if (windowWidthSizeClass == Expanded) {
            ArticlePreviewCardGrid(
                articleMoreDetailsAction = { component.navigateToArticleDetails(it) },
                articlePreviews = previewsAvailableState.articles,
                isBottomLoadingIndicatorDisplayed = viewState is Loading,
                modifier = modifier.padding(standardSpace),
            )
        } else {
            ArticlePreviewCardList(
                articleMoreDetailsAction = { component.navigateToArticleDetails(it) },
                articlePreviews = previewsAvailableState.articles,
                isBottomLoadingIndicatorDisplayed = viewState is Loading,
                modifier = modifier.padding(standardSpace),
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal fun ArticlePreviewCardGrid(
    articlePreviews: List<ArticlePreview>,
    isBottomLoadingIndicatorDisplayed: Boolean,
    modifier: Modifier = Modifier,
    articleMoreDetailsAction: (String) -> Unit = {},
) {
    val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass

    val standardSpace = getStandardSpace(windowWidthSizeClass)

    Box {
        val gridState = rememberLazyGridState()

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(standardSpace),
            modifier = modifier,
            state = gridState,
            verticalArrangement = Arrangement.spacedBy(standardSpace),
        ) {
            items(articlePreviews) {
                ArticleCard(
                    brand = it.brand,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    moreDetailsAction = { articleMoreDetailsAction(it.id) },
                    name = it.name,
                )
            }

            if (isBottomLoadingIndicatorDisplayed) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Spacer(Modifier.weight(0.5f))
                        CircularProgressIndicator()
                        Spacer(Modifier.weight(0.5f))
                    }
                }
            }
        }

        PlatformSpecificVerticalGridScrollbar(
            gridState = gridState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal fun ArticlePreviewCardList(
    articlePreviews: List<ArticlePreview>,
    isBottomLoadingIndicatorDisplayed: Boolean,
    modifier: Modifier = Modifier,
    articleMoreDetailsAction: (String) -> Unit = {},
) {
    Box {
        val listState = rememberLazyListState()

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier,
            state = listState,
            verticalArrangement = Arrangement.spacedBy(
                getStandardSpace(calculateWindowSizeClass().widthSizeClass)
            ),
        ) {
            items(articlePreviews) {
                ArticleCard(
                    brand = it.brand,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    moreDetailsAction = { articleMoreDetailsAction(it.id) },
                    name = it.name,
                )
            }

            if (isBottomLoadingIndicatorDisplayed) {
                item {
                    CircularProgressIndicator()
                }
            }
        }

        PlatformSpecificVerticalListScrollbar(
            listState = listState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun ArticleCard(
    brand: String,
    description: String,
    imageUrl: String,
    name: String,
    modifier: Modifier = Modifier,
    moreDetailsAction: () -> Unit = {},
) {
    Card(modifier = modifier) {
        // TODO sizing
        AsyncImage(
            contentDescription = null,
            model = imageUrl,
            modifier = Modifier.fillMaxWidth(),
        )

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Column(Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = Material3Typography.bodyLarge,
                    text = name,
                )

                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    style = Material3Typography.bodyMedium,
                    text = brand,
                )
            }

            Text(
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                style = Material3Typography.bodyMedium,
                text = description,
            )

            Button(
                onClick = moreDetailsAction,
            ) {
                Text(stringResource(Res.string.article_preview_card_more_details_button_text))
            }
        }
    }
}