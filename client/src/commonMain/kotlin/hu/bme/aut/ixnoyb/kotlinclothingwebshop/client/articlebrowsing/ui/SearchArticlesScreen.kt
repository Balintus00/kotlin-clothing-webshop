package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RangeSlider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui.mapper.toDisplayValue
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesComponent.Companion.BRAND_ALLOWED_LETTERS
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesComponent.Companion.BRAND_MAXIMUM_LENGTH
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesComponent.Companion.NAME_ALLOWED_LETTERS
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesComponent.Companion.NAME_MAXIMUM_LENGTH
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesComponent.Companion.PRICE_MAXIMUM_VALUE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesComponent.Companion.PRICE_MINIMUM_VALUE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.Color
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.GarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.GraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.Index
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.Shade
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_CONTENT_SPACE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.COMMON_BUTTON_ICON_EDGE_SIZE
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.PlatformSpecificHorizontalListScrollbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.PlatformSpecificVerticalListScrollbar
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.formatAmount
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui.getStandardSpace
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.ui.theme.Material3Typography
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.search_articles_screen_brand_label
import kotlinclothingwebshop.client.generated.resources.search_articles_screen_color_label
import kotlinclothingwebshop.client.generated.resources.search_articles_screen_cta_button_search_text
import kotlinclothingwebshop.client.generated.resources.search_articles_screen_garment_group_label
import kotlinclothingwebshop.client.generated.resources.search_articles_screen_graphical_appearance_label
import kotlinclothingwebshop.client.generated.resources.search_articles_screen_index_label
import kotlinclothingwebshop.client.generated.resources.search_articles_screen_name_label
import kotlinclothingwebshop.client.generated.resources.search_articles_screen_price_label
import kotlinclothingwebshop.client.generated.resources.search_articles_screen_price_range_template
import kotlinclothingwebshop.client.generated.resources.search_articles_screen_shade_label
import kotlinclothingwebshop.client.generated.resources.search_articles_screen_top_appbar_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
internal fun SearchArticlesScreenTopAppbar(
    component: SearchArticlesComponent,
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
        title = { Text(stringResource(Res.string.search_articles_screen_top_appbar_title)) },
    )

}

@Composable
@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
internal fun SearchArticlesScreen(
    component: SearchArticlesComponent,
    modifier: Modifier = Modifier,
) {
    val windowWithSizeClass = calculateWindowSizeClass().widthSizeClass

    val standardSpace = getStandardSpace(windowWithSizeClass)

    Box {
        val verticalScrollState = rememberScrollState()

        Column(
            modifier = modifier.verticalScroll(verticalScrollState).padding(standardSpace),
            verticalArrangement = Arrangement.spacedBy(standardSpace),
        ) {
            var brand by remember { mutableStateOf("") }
            var colors: Set<Color> by remember { mutableStateOf(setOf()) }
            var garmentGroups: Set<GarmentGroup> by remember { mutableStateOf(setOf()) }
            var graphicalAppearances: Set<GraphicalAppearance> by remember {
                mutableStateOf(setOf())
            }
            var articleIndices: Set<Index> by remember { mutableStateOf(setOf()) }
            var priceRange by remember {
                mutableStateOf(PRICE_MINIMUM_VALUE..PRICE_MAXIMUM_VALUE)
            }
            var name by remember { mutableStateOf("") }
            var shades: Set<Shade> by remember { mutableStateOf(setOf()) }

            if (windowWithSizeClass == Expanded) {
                ExpandedNameAndBrandFilterFields(
                    brand = brand,
                    changeBrandAction = {
                        if (it.length <= BRAND_MAXIMUM_LENGTH && it in BRAND_ALLOWED_LETTERS) {
                            brand = it
                        }
                    },
                    changeNameAction = {
                        if (it.length <= NAME_MAXIMUM_LENGTH && it in NAME_ALLOWED_LETTERS) {
                            name = it
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    name = name,
                )
            } else {
                CompactAndMediumNameAndBrandFilterFields(
                    brand = brand,
                    changeBrandAction = {
                        if (it.length <= BRAND_MAXIMUM_LENGTH && it in BRAND_ALLOWED_LETTERS) {
                            brand = it
                        }
                    },
                    changeNameAction = {
                        if (it.length <= NAME_MAXIMUM_LENGTH && it in NAME_ALLOWED_LETTERS) {
                            name = it
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    name = name,
                )
            }

            LabeledSearchFilterAttribute(
                labelValue = stringResource(Res.string.search_articles_screen_price_label),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    RangeSlider(
                        onValueChange = {
                            priceRange = it.start.roundToInt()..it.endInclusive.roundToInt()
                        },
                        value = priceRange.first.toFloat()..priceRange.last.toFloat(),
                        valueRange = PRICE_MINIMUM_VALUE.toFloat()
                                ..PRICE_MAXIMUM_VALUE.toFloat(),
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = Material3Typography.bodyLarge,
                        text = stringResource(
                            Res.string.search_articles_screen_price_range_template,
                            priceRange.first.formatAmount(),
                            priceRange.last.formatAmount(),
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }


            if (windowWithSizeClass == Expanded) {
                LabeledSearchFilterAttribute(
                    labelValue = stringResource(Res.string.search_articles_screen_color_label),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FlowingCategoricalArticleAttributePicker(
                        displayValuesWithIDs = Color.entries.map { it.toDisplayValue() to it.name },
                        selectedIDs = colors.map { it.name }.toSet(),
                        changeSelectionAction = { selectedID ->
                            val selectedColor = Color.entries.first { it.name == selectedID }

                            colors = if (selectedColor in colors) {
                                colors.toMutableSet().apply { remove(selectedColor) }
                            } else {
                                colors.toMutableSet().apply { add(selectedColor) }
                            }
                        },
                    )
                }

                LabeledSearchFilterAttribute(
                    labelValue = stringResource(Res.string.search_articles_screen_shade_label),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FlowingCategoricalArticleAttributePicker(
                        displayValuesWithIDs = Shade.entries.map { it.toDisplayValue() to it.name },
                        selectedIDs = shades.map { it.name }.toSet(),
                        changeSelectionAction = { selectedID ->
                            val selectedShade = Shade.entries.first { it.name == selectedID }

                            shades = if (selectedShade in shades) {
                                shades.toMutableSet().apply { remove(selectedShade) }
                            } else {
                                shades.toMutableSet().apply { add(selectedShade) }
                            }
                        },
                    )
                }

                LabeledSearchFilterAttribute(
                    labelValue = stringResource(
                        Res.string.search_articles_screen_graphical_appearance_label
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FlowingCategoricalArticleAttributePicker(
                        displayValuesWithIDs = GraphicalAppearance.entries.map {
                            it.toDisplayValue() to it.name
                        },
                        selectedIDs = graphicalAppearances.map { it.name }.toSet(),
                        changeSelectionAction = { selectedID ->
                            val selectedGraphicalAppearance = GraphicalAppearance.entries.first {
                                it.name == selectedID
                            }

                            graphicalAppearances =
                                if (selectedGraphicalAppearance in graphicalAppearances) {
                                    graphicalAppearances.toMutableSet()
                                        .apply { remove(selectedGraphicalAppearance) }
                                } else {
                                    graphicalAppearances.toMutableSet()
                                        .apply { add(selectedGraphicalAppearance) }
                                }
                        },
                    )
                }

                LabeledSearchFilterAttribute(
                    labelValue = stringResource(Res.string.search_articles_screen_index_label),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FlowingCategoricalArticleAttributePicker(
                        displayValuesWithIDs = Index.entries.map {
                            it.toDisplayValue() to it.name
                        },
                        selectedIDs = articleIndices.map { it.name }.toSet(),
                        changeSelectionAction = { selectedID ->
                            val selectedIndex = Index.entries.first { it.name == selectedID }

                            articleIndices = if (selectedIndex in articleIndices) {
                                articleIndices.toMutableSet().apply { remove(selectedIndex) }
                            } else {
                                articleIndices.toMutableSet().apply { add(selectedIndex) }
                            }
                        },
                    )
                }

                LabeledSearchFilterAttribute(
                    labelValue = stringResource(Res.string.search_articles_screen_garment_group_label),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FlowingCategoricalArticleAttributePicker(
                        displayValuesWithIDs = GarmentGroup.entries.map {
                            it.toDisplayValue() to it.name
                        },
                        selectedIDs = garmentGroups.map { it.name }.toSet(),
                        changeSelectionAction = { selectedID ->
                            val selectedGarmentGroup = GarmentGroup.entries.first {
                                it.name == selectedID
                            }

                            garmentGroups = if (selectedGarmentGroup in garmentGroups) {
                                garmentGroups.toMutableSet().apply { remove(selectedGarmentGroup) }
                            } else {
                                garmentGroups.toMutableSet().apply { add(selectedGarmentGroup) }
                            }
                        },
                    )
                }
            } else {
                LabeledSearchFilterAttribute(
                    labelValue = stringResource(Res.string.search_articles_screen_color_label),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OverflowingCategoricalArticleAttributePicker(
                        displayValuesWithIDs = Color.entries.map { it.toDisplayValue() to it.name },
                        selectedIDs = colors.map { it.name }.toSet(),
                        changeSelectionAction = { selectedID ->
                            val selectedColor = Color.entries.first { it.name == selectedID }

                            colors = if (selectedColor in colors) {
                                colors.toMutableSet().apply { remove(selectedColor) }
                            } else {
                                colors.toMutableSet().apply { add(selectedColor) }
                            }
                        },
                    )
                }

                LabeledSearchFilterAttribute(
                    labelValue = stringResource(Res.string.search_articles_screen_shade_label),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OverflowingCategoricalArticleAttributePicker(
                        displayValuesWithIDs = Shade.entries.map { it.toDisplayValue() to it.name },
                        selectedIDs = shades.map { it.name }.toSet(),
                        changeSelectionAction = { selectedID ->
                            val selectedShade = Shade.entries.first { it.name == selectedID }

                            shades = if (selectedShade in shades) {
                                shades.toMutableSet().apply { remove(selectedShade) }
                            } else {
                                shades.toMutableSet().apply { add(selectedShade) }
                            }
                        },
                    )
                }

                LabeledSearchFilterAttribute(
                    labelValue = stringResource(
                        Res.string.search_articles_screen_graphical_appearance_label
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OverflowingCategoricalArticleAttributePicker(
                        displayValuesWithIDs = GraphicalAppearance.entries.map {
                            it.toDisplayValue() to it.name
                        },
                        selectedIDs = graphicalAppearances.map { it.name }.toSet(),
                        changeSelectionAction = { selectedID ->
                            val selectedGraphicalAppearance = GraphicalAppearance.entries.first {
                                it.name == selectedID
                            }

                            graphicalAppearances =
                                if (selectedGraphicalAppearance in graphicalAppearances) {
                                    graphicalAppearances.toMutableSet()
                                        .apply { remove(selectedGraphicalAppearance) }
                                } else {
                                    graphicalAppearances.toMutableSet()
                                        .apply { add(selectedGraphicalAppearance) }
                                }
                        },
                    )
                }

                LabeledSearchFilterAttribute(
                    labelValue = stringResource(Res.string.search_articles_screen_index_label),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OverflowingCategoricalArticleAttributePicker(
                        displayValuesWithIDs = Index.entries.map {
                            it.toDisplayValue() to it.name
                        },
                        selectedIDs = articleIndices.map { it.name }.toSet(),
                        changeSelectionAction = { selectedID ->
                            val selectedIndex = Index.entries.first { it.name == selectedID }

                            articleIndices = if (selectedIndex in articleIndices) {
                                articleIndices.toMutableSet().apply { remove(selectedIndex) }
                            } else {
                                articleIndices.toMutableSet().apply { add(selectedIndex) }
                            }
                        },
                    )
                }

                LabeledSearchFilterAttribute(
                    labelValue = stringResource(Res.string.search_articles_screen_garment_group_label),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OverflowingCategoricalArticleAttributePicker(
                        displayValuesWithIDs = GarmentGroup.entries.map {
                            it.toDisplayValue() to it.name
                        },
                        selectedIDs = garmentGroups.map { it.name }.toSet(),
                        changeSelectionAction = { selectedID ->
                            val selectedGarmentGroup = GarmentGroup.entries.first {
                                it.name == selectedID
                            }

                            garmentGroups = if (selectedGarmentGroup in garmentGroups) {
                                garmentGroups.toMutableSet().apply { remove(selectedGarmentGroup) }
                            } else {
                                garmentGroups.toMutableSet().apply { add(selectedGarmentGroup) }
                            }
                        },
                    )
                }
            }

            Spacer(Modifier.weight(1.0f))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    component.navigateToSearchResults(
                        brand = brand,
                        colors = colors,
                        garmentGroups = garmentGroups,
                        graphicalAppearances = graphicalAppearances,
                        indices = articleIndices,
                        maximumPrice = priceRange.last,
                        minimumPrice = priceRange.first,
                        name = name,
                        shades = shades,
                    )
                },
            ) {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Default.Search,
                    modifier = Modifier
                        .width(COMMON_BUTTON_ICON_EDGE_SIZE)
                        .height(COMMON_BUTTON_ICON_EDGE_SIZE),
                )
                Spacer(Modifier.width(COMMON_BUTTON_CONTENT_SPACE))
                Text(stringResource(Res.string.search_articles_screen_cta_button_search_text))
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

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalResourceApi::class)
internal fun CompactAndMediumNameAndBrandFilterFields(
    brand: String,
    name: String,
    modifier: Modifier = Modifier,
    changeBrandAction: (String) -> Unit = {},
    changeNameAction: (String) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            getStandardSpace(calculateWindowSizeClass().widthSizeClass)
        ),
    ) {
        TextField(
            label = {
                Text(stringResource(Res.string.search_articles_screen_name_label))
            },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changeNameAction,
            singleLine = true,
            value = name,
        )

        TextField(
            label = {
                Text(stringResource(Res.string.search_articles_screen_brand_label))
            },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = changeBrandAction,
            singleLine = true,
            value = brand,
        )
    }
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun ExpandedNameAndBrandFilterFields(
    brand: String,
    name: String,
    modifier: Modifier = Modifier,
    changeBrandAction: (String) -> Unit = {},
    changeNameAction: (String) -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(getStandardSpace(Expanded)),
        modifier = modifier,
    ) {
        TextField(
            label = {
                Text(stringResource(Res.string.search_articles_screen_name_label))
            },
            modifier = Modifier.weight(0.5f),
            onValueChange = changeNameAction,
            singleLine = true,
            value = name,
        )

        TextField(
            label = {
                Text(stringResource(Res.string.search_articles_screen_brand_label))
            },
            modifier = Modifier.weight(0.5f),
            onValueChange = changeBrandAction,
            singleLine = true,
            value = brand,
        )
    }
}

@Composable
internal fun LabeledSearchFilterAttribute(
    labelValue: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            style = Material3Typography.titleMedium,
            text = labelValue,
        )
        content()
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
internal fun FlowingCategoricalArticleAttributePicker(
    selectedIDs: Set<String>,
    displayValuesWithIDs: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
    changeSelectionAction: (String) -> Unit = {},
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(
            getStandardSpace(calculateWindowSizeClass().widthSizeClass)
        ),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        displayValuesWithIDs.forEach {
            FilterChip(
                label = { Text(it.first) },
                leadingIcon = {
                    if (it.second in selectedIDs) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                        )
                    }
                },
                onClick = { changeSelectionAction(it.second) },
                selected = it.second in selectedIDs,
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal fun OverflowingCategoricalArticleAttributePicker(
    selectedIDs: Set<String>,
    displayValuesWithIDs: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
    changeSelectionAction: (String) -> Unit = {},
) {
    Box(
        modifier = modifier,
    ) {
        val horizontalScrollState = rememberScrollState()

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                getStandardSpace(calculateWindowSizeClass().widthSizeClass)
            ),
            modifier = Modifier.horizontalScroll(horizontalScrollState),
        ) {
            displayValuesWithIDs.forEach {
                FilterChip(
                    label = { Text(it.first) },
                    leadingIcon = {
                        if (it.second in selectedIDs) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                            )
                        }
                    },
                    onClick = { changeSelectionAction(it.second) },
                    selected = it.second in selectedIDs,
                )
            }
        }

        PlatformSpecificHorizontalListScrollbar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            scrollState = horizontalScrollState,
        )
    }
}