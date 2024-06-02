package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ArticleFilter(
    val colors: Set<Color>,
    val brand: String?,
    val garmentGroups: Set<GarmentGroup>,
    val graphicalAppearances: Set<GraphicalAppearance>,
    val indices: Set<Index>,
    val minimumPrice: Int?,
    val maximumPrice: Int?,
    val name: String?,
    val shades: Set<Shade>,
)