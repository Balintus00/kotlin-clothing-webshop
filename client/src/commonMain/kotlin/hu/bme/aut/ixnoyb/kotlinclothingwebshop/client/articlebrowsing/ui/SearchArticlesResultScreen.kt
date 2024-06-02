package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesResultComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesResultComponent.ViewState.ArticlePreviewsAvailable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesResultComponent.ViewState.Loading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_CONTENT_SPACE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_ICON_EDGE_SIZE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.getStandardSpace
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme.Material3Typography
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.image_nothing_found
import kotlinclothingwebshop.client.generated.resources.search_articles_result_screen_empty_results_change_filter_button_text
import kotlinclothingwebshop.client.generated.resources.search_articles_result_screen_empty_results_title
import kotlinclothingwebshop.client.generated.resources.search_articles_result_screen_top_appbar_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
internal fun SearchArticlesResultScreenTopAppbar(
    component: SearchArticlesResultComponent,
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
        title = { Text(stringResource(Res.string.search_articles_result_screen_top_appbar_title)) },
    )
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal fun SearchArticlesResultScreen(
    component: SearchArticlesResultComponent,
    modifier: Modifier = Modifier
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass

    val standardSpace = getStandardSpace(windowWidthSizeClass)

    (viewState as? ArticlePreviewsAvailable)?.let { previewsAvailableState ->
        when {
            previewsAvailableState.articles.isEmpty() -> {
                EmptySearchResultsScreen(
                    changeFilterAction = component::navigateBack,
                    modifier = modifier.padding(standardSpace),
                )
            }

            windowWidthSizeClass == WindowWidthSizeClass.Expanded -> {
                ArticlePreviewCardGrid(
                    articleMoreDetailsAction = { component.navigateToArticleDetails(it) },
                    articlePreviews = previewsAvailableState.articles,
                    isBottomLoadingIndicatorDisplayed = viewState is Loading,
                    modifier = modifier.padding(standardSpace),
                )
            }

            else -> {
                ArticlePreviewCardList(
                    articleMoreDetailsAction = { component.navigateToArticleDetails(it) },
                    articlePreviews = previewsAvailableState.articles,
                    isBottomLoadingIndicatorDisplayed = viewState is Loading,
                    modifier = modifier.padding(standardSpace),
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalResourceApi::class)
internal fun EmptySearchResultsScreen(
    modifier: Modifier = Modifier,
    changeFilterAction: () -> Unit = {},
) {
    val windowSizeClass = calculateWindowSizeClass()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            getStandardSpace(windowSizeClass.widthSizeClass)
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(
                if (windowSizeClass.widthSizeClass == Compact) {
                    24.dp
                } else {
                    36.dp
                }
            ),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = when (windowSizeClass.widthSizeClass) {
                    Compact -> Material3Typography.titleSmall
                    Medium -> Material3Typography.titleMedium
                    else -> Material3Typography.titleLarge
                },
                text = stringResource(Res.string.search_articles_result_screen_empty_results_title),
                textAlign = TextAlign.Center,
            )

            Image(
                contentDescription = null,
                modifier = Modifier.size(
                    when (windowSizeClass.heightSizeClass) {
                        WindowHeightSizeClass.Compact -> 200.dp
                        WindowHeightSizeClass.Medium -> 280.dp
                        else -> 360.dp
                    }
                ).weight(1.0f),
                painter = painterResource(Res.drawable.image_nothing_found),
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { changeFilterAction() },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                modifier = Modifier
                    .width(COMMON_BUTTON_ICON_EDGE_SIZE)
                    .height(COMMON_BUTTON_ICON_EDGE_SIZE),
                contentDescription = null,
            )
            Spacer(Modifier.width(COMMON_BUTTON_CONTENT_SPACE))
            Text(
                stringResource(
                    Res.string.search_articles_result_screen_empty_results_change_filter_button_text
                )
            )
        }
    }
}