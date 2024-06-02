package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui.mapper.toDisplayValue
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.ArticleDetailsComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.ArticleDetailsComponent.ViewState.Loaded
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.FLOATING_ACTION_BUTTON_ON_NAVIGATION_RAIL_VERTICAL_SPACE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.LoadingSection
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.PlatformSpecificVerticalListScrollbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.collectAsStateWithLifecycle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.formatAmount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.getStandardSpace
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme.Material3Typography
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.article_details_screen_brand_attribute
import kotlinclothingwebshop.client.generated.resources.article_details_screen_color_label
import kotlinclothingwebshop.client.generated.resources.article_details_screen_description_label
import kotlinclothingwebshop.client.generated.resources.article_details_screen_garment_group_label
import kotlinclothingwebshop.client.generated.resources.article_details_screen_graphical_appearance_label
import kotlinclothingwebshop.client.generated.resources.article_details_screen_index_label
import kotlinclothingwebshop.client.generated.resources.article_details_screen_price_attribute
import kotlinclothingwebshop.client.generated.resources.article_details_screen_shade_label
import kotlinclothingwebshop.client.generated.resources.article_details_screen_top_appbar_title
import kotlinclothingwebshop.client.generated.resources.ic_add_shopping_cart
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
internal fun ArticleDetailsScreenTopAppbar(
    component: ArticleDetailsComponent,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = viewState::navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        scrollBehavior = topAppBarScrollBehavior,
        title = { Text(stringResource(Res.string.article_details_screen_top_appbar_title)) },
    )
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalResourceApi::class)
internal fun ArticleDetailsScreen(
    component: ArticleDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    val verticalScrollState = rememberScrollState()

    val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass

    val standardSpace = getStandardSpace(windowWidthSizeClass)

    (viewState as? Loaded)?.let { loadedState ->
        Box {
            Column(
                modifier = modifier.verticalScroll(verticalScrollState).padding(standardSpace),
                verticalArrangement = Arrangement.spacedBy(standardSpace),
            ) {
                with(loadedState.article) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = Material3Typography.titleLarge,
                        text = name,
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = Material3Typography.labelLarge,
                        text = stringResource(
                            Res.string.article_details_screen_brand_attribute,
                            brand
                        )
                    )
                    // TODO: fillMaxWidth doesn't necessarily work as expected for wide screens.
                    //  Also the sizing is not correct like this.
                    AsyncImage(
                        model = imageUrl,
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = Material3Typography.titleLarge,
                        text = stringResource(
                            Res.string.article_details_screen_price_attribute,
                            price.formatAmount(),
                        ),
                    )
                    ArticleDetailsLabeledSection(
                        label = stringResource(Res.string.article_details_screen_description_label),
                        modifier = Modifier.fillMaxWidth(),
                        value = description,
                        valueTextStyle = Material3Typography.bodyMedium,
                    )

                    when (windowWidthSizeClass) {
                        Compact -> {
                            CompactWidthArticleAttributeSection(
                                article = this,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        Medium -> {
                            MediumWidthArticleAttributeSection(
                                article = this,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        Expanded -> {
                            ExpandedWidthArticleAttributeSection(
                                article = this,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }

            PlatformSpecificVerticalListScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                scrollState = verticalScrollState,
            )
        }
    } ?: run {
        LoadingSection(modifier = modifier)
    }
}

@Composable
internal fun ArticleDetailsLabeledSection(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueTextStyle: TextStyle = Material3Typography.titleMedium,
) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            style = Material3Typography.labelMedium,
            text = label,
        )
        Text(
            style = valueTextStyle,
            text = value,
        )
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun CompactWidthArticleAttributeSection(
    article: Article,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(getStandardSpace(Compact)),
    ) {
        with(article) {
            ArticleDetailsLabeledSection(
                label = stringResource(Res.string.article_details_screen_color_label),
                modifier = Modifier.fillMaxWidth(),
                value = color.toDisplayValue(),
            )
            ArticleDetailsLabeledSection(
                label = stringResource(Res.string.article_details_screen_shade_label),
                modifier = Modifier.fillMaxWidth(),
                value = shade.toDisplayValue(),
            )
            ArticleDetailsLabeledSection(
                label = stringResource(Res.string.article_details_screen_graphical_appearance_label),
                modifier = Modifier.fillMaxWidth(),
                value = graphicalAppearance.toDisplayValue(),
            )
            ArticleDetailsLabeledSection(
                label = stringResource(Res.string.article_details_screen_index_label),
                modifier = Modifier.fillMaxWidth(),
                value = index.toDisplayValue(),
            )
            ArticleDetailsLabeledSection(
                label = stringResource(Res.string.article_details_screen_garment_group_label),
                modifier = Modifier.fillMaxWidth(),
                value = garmentGroup.toDisplayValue(),
            )
        }
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun MediumWidthArticleAttributeSection(
    article: Article,
    modifier: Modifier = Modifier,
) {
    val standardSpace = getStandardSpace(Medium)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(standardSpace),
    ) {
        with(article) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(standardSpace),
                modifier = Modifier.fillMaxWidth(),
            ) {
                ArticleDetailsLabeledSection(
                    label = stringResource(Res.string.article_details_screen_color_label),
                    modifier = Modifier.weight(0.5f),
                    value = color.toDisplayValue(),
                )
                ArticleDetailsLabeledSection(
                    label = stringResource(Res.string.article_details_screen_shade_label),
                    modifier = Modifier.weight(0.5f),
                    value = shade.toDisplayValue(),
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(standardSpace),
                modifier = Modifier.fillMaxWidth(),
            ) {
                ArticleDetailsLabeledSection(
                    label = stringResource(Res.string.article_details_screen_graphical_appearance_label),
                    modifier = Modifier.weight(0.5f),
                    value = graphicalAppearance.toDisplayValue(),
                )
                ArticleDetailsLabeledSection(
                    label = stringResource(Res.string.article_details_screen_index_label),
                    modifier = Modifier.weight(0.5f),
                    value = index.toDisplayValue(),
                )
            }

            ArticleDetailsLabeledSection(
                label = stringResource(Res.string.article_details_screen_garment_group_label),
                modifier = Modifier.fillMaxWidth(0.5f).padding(end = standardSpace / 2),
                value = garmentGroup.toDisplayValue(),
            )
        }
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun ExpandedWidthArticleAttributeSection(
    article: Article,
    modifier: Modifier = Modifier,
) {
    val standardSpace = getStandardSpace(Expanded)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(standardSpace),
    ) {
        with(article) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(standardSpace),
                modifier = Modifier.fillMaxWidth(),
            ) {
                ArticleDetailsLabeledSection(
                    label = stringResource(Res.string.article_details_screen_color_label),
                    modifier = Modifier.weight(1f / 3f),
                    value = color.toDisplayValue(),
                )
                ArticleDetailsLabeledSection(
                    label = stringResource(Res.string.article_details_screen_shade_label),
                    modifier = Modifier.weight(1f / 3f),
                    value = shade.toDisplayValue(),
                )
                ArticleDetailsLabeledSection(
                    label = stringResource(Res.string.article_details_screen_graphical_appearance_label),
                    modifier = Modifier.weight(1f / 3f),
                    value = graphicalAppearance.toDisplayValue(),
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(standardSpace),
                modifier = Modifier.fillMaxWidth(),
            ) {
                ArticleDetailsLabeledSection(
                    label = stringResource(Res.string.article_details_screen_index_label),
                    modifier = Modifier.weight(1f / 3f),
                    value = index.toDisplayValue(),
                )

                ArticleDetailsLabeledSection(
                    label = stringResource(Res.string.article_details_screen_garment_group_label),
                    modifier = Modifier.weight(1f / 3f),
                    value = garmentGroup.toDisplayValue(),
                )
                Spacer(modifier = Modifier.weight(1f / 3f))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun CompactArticleDetailsFloatingActionButton(
    component: ArticleDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    FloatingActionButton(
        modifier = modifier,
        onClick = { (viewState as? Loaded)?.addToBasket() },
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_add_shopping_cart),
            contentDescription = null,
        )
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun MediumAndExpandedArticleDetailsFloatingActionButton(
    component: ArticleDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val viewState by component.viewState.collectAsStateWithLifecycle()

    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = modifier.padding(bottom = FLOATING_ACTION_BUTTON_ON_NAVIGATION_RAIL_VERTICAL_SPACE),
        onClick = { (viewState as? Loaded)?.addToBasket() },
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_add_shopping_cart),
            contentDescription = null,
        )
    }
}