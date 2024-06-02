package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic

import com.arkivanov.decompose.ComponentContext
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.ArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.Color
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.GarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.GraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.Index
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.Shade
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Brand
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Name
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Price

interface SearchArticlesComponent {

    fun navigateBack()

    fun navigateToSearchResults(
        colors: Set<Color>,
        brand: String,
        garmentGroups: Set<GarmentGroup>,
        graphicalAppearances: Set<GraphicalAppearance>,
        indices: Set<Index>,
        minimumPrice: Int,
        maximumPrice: Int,
        name: String,
        shades: Set<Shade>,
    )

    companion object {
        const val BRAND_MAXIMUM_LENGTH = Brand.MAXIMUM_LENGTH
        const val NAME_MAXIMUM_LENGTH = Name.MAXIMUM_LENGTH

        const val PRICE_MINIMUM_VALUE = Price.MINIMUM_VALUE
        const val PRICE_MAXIMUM_VALUE = Price.MAXIMUM_VALUE

        const val BRAND_ALLOWED_LETTERS = Brand.ALLOWED_LETTERS
        const val NAME_ALLOWED_LETTERS = Name.ALLOWED_LETTERS
    }
}

internal class DefaultSearchArticlesComponent(
    componentContext: ComponentContext,
    private val navigateBackAction: () -> Unit = {},
    private val navigateToSearchResultsAction: (ArticleFilter) -> Unit = {},
) : SearchArticlesComponent, ComponentContext by componentContext {

    override fun navigateBack() {
        navigateBackAction()
    }

    override fun navigateToSearchResults(
        colors: Set<Color>,
        brand: String,
        garmentGroups: Set<GarmentGroup>,
        graphicalAppearances: Set<GraphicalAppearance>,
        indices: Set<Index>,
        minimumPrice: Int,
        maximumPrice: Int,
        name: String,
        shades: Set<Shade>,
    ) {
        navigateToSearchResultsAction(
            ArticleFilter(
                colors = colors,
                brand = brand,
                garmentGroups = garmentGroups,
                graphicalAppearances = graphicalAppearances,
                indices = indices,
                minimumPrice = minimumPrice,
                maximumPrice = maximumPrice,
                name = name,
                shades = shades,
            )
        )
    }
}