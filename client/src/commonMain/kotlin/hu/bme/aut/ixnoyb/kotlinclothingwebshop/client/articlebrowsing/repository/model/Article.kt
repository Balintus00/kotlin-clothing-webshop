package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Brand
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Color
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Description
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.GarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.GraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Index
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Name
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Price
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Shade
import io.ktor.http.Url

data class ArticlePreview(
    val id: ArticleID,
    val name: Name,
    val brand: Brand,
    val description: Description,
    val imageUrl: Url,
    val price: Price,
)

data class Article(
    val id: ArticleID,
    val name: Name,
    val brand: Brand,
    val description: Description,
    val imageUrl: Url,
    val price: Price,
    val color: Color,
    val garmentGroup: GarmentGroup,
    val graphicalAppearance: GraphicalAppearance,
    val index: Index,
    val shade: Shade,
)