package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.datasource.ArticleLocalDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.ArticlePreview
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
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource.Article as SqlDelightArticle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource.SqlDelightArticleDatasource
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource.Color as SqlDelightColor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource.GarmentGroup as SqlDelightGarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource.GraphicalAppearance as SqlDelightGraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource.Index as SqlDelightIndex
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.structuredPersistentDatasource.Shade as SqlDelightShade

internal class SqlDelightAdapterArticleLocalDatasource(
    private val sqlDelightArticleDatasource: SqlDelightArticleDatasource,
) : ArticleLocalDatasource {

    override fun getAllPreviews(): Flow<List<ArticlePreview>> =
        sqlDelightArticleDatasource.getAllPreviews().map { sqlDelightArticleList ->
            sqlDelightArticleList.map { it.toArticlePreview() }
        }

    private inline fun SqlDelightArticle.toArticlePreview(): ArticlePreview = ArticlePreview(
        id = ArticleID(id),
        brand = Brand(brand),
        description = Description(description),
        imageUrl = Url(image_url),
        name = Name(name),
        price = Price(price.toInt()),
    )

    override fun getByID(articleID: ArticleID): Flow<Article?> =
        sqlDelightArticleDatasource.getArticleByID(articleID).map { it?.toArticle() }

    private inline fun SqlDelightArticle.toArticle(): Article? = if (
        setOf(article_index, color, garment_group, graphical_appearance, shade).all {
            it != null
        }
    ) {
        Article(
            id = ArticleID(id),
            brand = Brand(brand),
            color = color!!.toDomainColor(),
            description = Description(description),
            garmentGroup = garment_group!!.toDomainGarmentGroup(),
            graphicalAppearance = graphical_appearance!!.toDomainGraphicalAppearance(),
            imageUrl = Url(image_url),
            index = article_index!!.toDomainIndex(),
            name = Name(name),
            price = Price(price.toInt()),
            shade = shade!!.toDomainShade(),
        )
    } else {
        null
    }

    private fun SqlDelightColor.toDomainColor(): Color = when (this) {
        SqlDelightColor.Beige -> Color.Beige
        SqlDelightColor.Black -> Color.Black
        SqlDelightColor.Blue -> Color.Blue
        SqlDelightColor.BluishGreen -> Color.BluishGreen
        SqlDelightColor.Brown -> Color.Brown
        SqlDelightColor.Green -> Color.Green
        SqlDelightColor.Grey -> Color.Grey
        SqlDelightColor.KhakiGreen -> Color.KhakiGreen
        SqlDelightColor.LilacPurple -> Color.LilacPurple
        SqlDelightColor.Metal -> Color.Metal
        SqlDelightColor.Mole -> Color.Mole
        SqlDelightColor.Orange -> Color.Orange
        SqlDelightColor.Pink -> Color.Pink
        SqlDelightColor.Red -> Color.Red
        SqlDelightColor.Turquoise -> Color.Turquoise
        SqlDelightColor.Undefined -> Color.Undefined
        SqlDelightColor.Unknown -> Color.Unknown
        SqlDelightColor.White -> Color.White
        SqlDelightColor.Yellow -> Color.Yellow
        SqlDelightColor.YellowishGreen -> Color.YellowishGreen
    }

    private fun SqlDelightGarmentGroup.toDomainGarmentGroup(): GarmentGroup = when (this) {
        SqlDelightGarmentGroup.Accessories -> GarmentGroup.Accessories
        SqlDelightGarmentGroup.Blouses -> GarmentGroup.Blouses
        SqlDelightGarmentGroup.Dressed -> GarmentGroup.Dressed
        SqlDelightGarmentGroup.DressesLadies -> GarmentGroup.DressesLadies
        SqlDelightGarmentGroup.DressesSkirtsGirls -> GarmentGroup.DressesSkirtsGirls
        SqlDelightGarmentGroup.JerseyBasic -> GarmentGroup.JerseyBasic
        SqlDelightGarmentGroup.JerseyFancy -> GarmentGroup.JerseyFancy
        SqlDelightGarmentGroup.Knitwear -> GarmentGroup.Knitwear
        SqlDelightGarmentGroup.Outdoor -> GarmentGroup.Outdoor
        SqlDelightGarmentGroup.Shirts -> GarmentGroup.Shirts
        SqlDelightGarmentGroup.Shoes -> GarmentGroup.Shoes
        SqlDelightGarmentGroup.Shorts -> GarmentGroup.Shorts
        SqlDelightGarmentGroup.Skirts -> GarmentGroup.Skirts
        SqlDelightGarmentGroup.SocksAndTights -> GarmentGroup.SocksAndTights
        SqlDelightGarmentGroup.SpecialOffers -> GarmentGroup.SpecialOffers
        SqlDelightGarmentGroup.Swimwear -> GarmentGroup.Swimwear
        SqlDelightGarmentGroup.Trousers -> GarmentGroup.Trousers
        SqlDelightGarmentGroup.TrousersDenim -> GarmentGroup.TrousersDenim
        SqlDelightGarmentGroup.UnderNightwear -> GarmentGroup.UnderNightwear
        SqlDelightGarmentGroup.Unknown -> GarmentGroup.Unknown
        SqlDelightGarmentGroup.WovenJerseyKnittedMixBaby -> GarmentGroup.WovenJerseyKnittedMixBaby
    }

    private fun SqlDelightGraphicalAppearance.toDomainGraphicalAppearance(): GraphicalAppearance =
        when (this) {
            SqlDelightGraphicalAppearance.AllOverPattern -> GraphicalAppearance.AllOverPattern
            SqlDelightGraphicalAppearance.Application3D -> GraphicalAppearance.Application3D
            SqlDelightGraphicalAppearance.Argyle -> GraphicalAppearance.Argyle
            SqlDelightGraphicalAppearance.Chambray -> GraphicalAppearance.Chambray
            SqlDelightGraphicalAppearance.Check -> GraphicalAppearance.Check
            SqlDelightGraphicalAppearance.ColourBlocking -> GraphicalAppearance.ColourBlocking
            SqlDelightGraphicalAppearance.Contrast -> GraphicalAppearance.Contrast
            SqlDelightGraphicalAppearance.Denim -> GraphicalAppearance.Denim
            SqlDelightGraphicalAppearance.Dot -> GraphicalAppearance.Dot
            SqlDelightGraphicalAppearance.Embroidery -> GraphicalAppearance.Embroidery
            SqlDelightGraphicalAppearance.FrontPrint -> GraphicalAppearance.FrontPrint
            SqlDelightGraphicalAppearance.GlitteringMetallic -> GraphicalAppearance.GlitteringMetallic
            SqlDelightGraphicalAppearance.Hologram -> GraphicalAppearance.Hologram
            SqlDelightGraphicalAppearance.Jacquard -> GraphicalAppearance.Jacquard
            SqlDelightGraphicalAppearance.Lace -> GraphicalAppearance.Lace
            SqlDelightGraphicalAppearance.Melange -> GraphicalAppearance.Melange
            SqlDelightGraphicalAppearance.Mesh -> GraphicalAppearance.Mesh
            SqlDelightGraphicalAppearance.Metallic -> GraphicalAppearance.Metallic
            SqlDelightGraphicalAppearance.MixedSolidPattern -> GraphicalAppearance.MixedSolidPattern
            SqlDelightGraphicalAppearance.Neps -> GraphicalAppearance.Neps
            SqlDelightGraphicalAppearance.OtherPattern -> GraphicalAppearance.OtherPattern
            SqlDelightGraphicalAppearance.OtherStructure -> GraphicalAppearance.OtherStructure
            SqlDelightGraphicalAppearance.PlacementPrint -> GraphicalAppearance.PlacementPrint
            SqlDelightGraphicalAppearance.Sequin -> GraphicalAppearance.Sequin
            SqlDelightGraphicalAppearance.Slub -> GraphicalAppearance.Slub
            SqlDelightGraphicalAppearance.Solid -> GraphicalAppearance.Solid
            SqlDelightGraphicalAppearance.Stripe -> GraphicalAppearance.Stripe
            SqlDelightGraphicalAppearance.Transparent -> GraphicalAppearance.Transparent
            SqlDelightGraphicalAppearance.Treatment -> GraphicalAppearance.Treatment
            SqlDelightGraphicalAppearance.Unknown -> GraphicalAppearance.Unknown
        }

    private fun SqlDelightIndex.toDomainIndex(): Index = when (this) {
        SqlDelightIndex.BabySizes5098 -> Index.BabySizes5098
        SqlDelightIndex.ChildrenAccessoriesSwimwear -> Index.ChildrenAccessoriesSwimwear
        SqlDelightIndex.ChildrenSizes134170 -> Index.ChildrenSizes134170
        SqlDelightIndex.ChildrenSizes92140 -> Index.ChildrenSizes92140
        SqlDelightIndex.Divided -> Index.Divided
        SqlDelightIndex.LadiesAccessories -> Index.LadiesAccessories
        SqlDelightIndex.LadiesWear -> Index.LadiesWear
        SqlDelightIndex.LingeriesTights -> Index.LingeriesTights
        SqlDelightIndex.Menswear -> Index.Menswear
        SqlDelightIndex.Sport -> Index.Sport
    }

    private fun SqlDelightShade.toDomainShade(): Shade = when (this) {
        SqlDelightShade.Bright -> Shade.Bright
        SqlDelightShade.Dark -> Shade.Dark
        SqlDelightShade.DustyLight -> Shade.DustyLight
        SqlDelightShade.Light -> Shade.Light
        SqlDelightShade.Medium -> Shade.Medium
        SqlDelightShade.MediumDusty -> Shade.MediumDusty
        SqlDelightShade.Other -> Shade.Other
    }

    override suspend fun clear() {
        sqlDelightArticleDatasource.clear()
    }

    override suspend fun insertPreviews(vararg articlePreviews: ArticlePreview) {
        sqlDelightArticleDatasource.insertPreviews(
            *articlePreviews.map { it.toSqlDelightArticle() }.toTypedArray()
        )
    }

    private inline fun ArticlePreview.toSqlDelightArticle(): SqlDelightArticle = SqlDelightArticle(
        id = id.value,
        article_index = null,
        brand = brand.value,
        color = null,
        description = description.value,
        garment_group = null,
        graphical_appearance = null,
        image_url = imageUrl.toString(),
        name = name.value,
        price = price.value.toLong(),
        shade = null,
    )

    override suspend fun update(vararg articles: Article) {
        sqlDelightArticleDatasource.updateArticles(
            *articles.map { it.toSqlDelightArticle() }.toTypedArray()
        )
    }

    private inline fun Article.toSqlDelightArticle(): SqlDelightArticle = SqlDelightArticle(
        id = id.value,
        article_index = index.toSqlDelightIndex(),
        brand = brand.value,
        color = color.toSqlDelightColor(),
        description = description.value,
        garment_group = garmentGroup.toSqlDelightGarmentGroup(),
        graphical_appearance = graphicalAppearance.toSqlDelightGraphicalAppearance(),
        image_url = imageUrl.toString(),
        name = name.value,
        price = price.value.toLong(),
        shade = shade.toSqlDelightShade(),
    )

    private fun Color.toSqlDelightColor(): SqlDelightColor = when (this) {
        Color.Beige -> SqlDelightColor.Beige
        Color.Black -> SqlDelightColor.Black
        Color.Blue -> SqlDelightColor.Blue
        Color.BluishGreen -> SqlDelightColor.BluishGreen
        Color.Brown -> SqlDelightColor.Brown
        Color.Green -> SqlDelightColor.Green
        Color.Grey -> SqlDelightColor.Grey
        Color.KhakiGreen -> SqlDelightColor.KhakiGreen
        Color.LilacPurple -> SqlDelightColor.LilacPurple
        Color.Metal -> SqlDelightColor.Metal
        Color.Mole -> SqlDelightColor.Mole
        Color.Orange -> SqlDelightColor.Orange
        Color.Pink -> SqlDelightColor.Pink
        Color.Red -> SqlDelightColor.Red
        Color.Turquoise -> SqlDelightColor.Turquoise
        Color.Undefined -> SqlDelightColor.Undefined
        Color.Unknown -> SqlDelightColor.Unknown
        Color.White -> SqlDelightColor.White
        Color.Yellow -> SqlDelightColor.Yellow
        Color.YellowishGreen -> SqlDelightColor.YellowishGreen
    }

    private fun GarmentGroup.toSqlDelightGarmentGroup(): SqlDelightGarmentGroup = when (this) {
        GarmentGroup.Accessories -> SqlDelightGarmentGroup.Accessories
        GarmentGroup.Blouses -> SqlDelightGarmentGroup.Blouses
        GarmentGroup.Dressed -> SqlDelightGarmentGroup.Dressed
        GarmentGroup.DressesLadies -> SqlDelightGarmentGroup.DressesLadies
        GarmentGroup.DressesSkirtsGirls -> SqlDelightGarmentGroup.DressesSkirtsGirls
        GarmentGroup.JerseyBasic -> SqlDelightGarmentGroup.JerseyBasic
        GarmentGroup.JerseyFancy -> SqlDelightGarmentGroup.JerseyFancy
        GarmentGroup.Knitwear -> SqlDelightGarmentGroup.Knitwear
        GarmentGroup.Outdoor -> SqlDelightGarmentGroup.Outdoor
        GarmentGroup.Shirts -> SqlDelightGarmentGroup.Shirts
        GarmentGroup.Shoes -> SqlDelightGarmentGroup.Shoes
        GarmentGroup.Shorts -> SqlDelightGarmentGroup.Shorts
        GarmentGroup.Skirts -> SqlDelightGarmentGroup.Skirts
        GarmentGroup.SocksAndTights -> SqlDelightGarmentGroup.SocksAndTights
        GarmentGroup.SpecialOffers -> SqlDelightGarmentGroup.SpecialOffers
        GarmentGroup.Swimwear -> SqlDelightGarmentGroup.Swimwear
        GarmentGroup.Trousers -> SqlDelightGarmentGroup.Trousers
        GarmentGroup.TrousersDenim -> SqlDelightGarmentGroup.TrousersDenim
        GarmentGroup.UnderNightwear -> SqlDelightGarmentGroup.UnderNightwear
        GarmentGroup.Unknown -> SqlDelightGarmentGroup.Unknown
        GarmentGroup.WovenJerseyKnittedMixBaby -> SqlDelightGarmentGroup.WovenJerseyKnittedMixBaby
    }

    private fun GraphicalAppearance.toSqlDelightGraphicalAppearance(): SqlDelightGraphicalAppearance =
        when (this) {
            GraphicalAppearance.AllOverPattern -> SqlDelightGraphicalAppearance.AllOverPattern
            GraphicalAppearance.Application3D -> SqlDelightGraphicalAppearance.Application3D
            GraphicalAppearance.Argyle -> SqlDelightGraphicalAppearance.Argyle
            GraphicalAppearance.Chambray -> SqlDelightGraphicalAppearance.Chambray
            GraphicalAppearance.Check -> SqlDelightGraphicalAppearance.Check
            GraphicalAppearance.ColourBlocking -> SqlDelightGraphicalAppearance.ColourBlocking
            GraphicalAppearance.Contrast -> SqlDelightGraphicalAppearance.Contrast
            GraphicalAppearance.Denim -> SqlDelightGraphicalAppearance.Denim
            GraphicalAppearance.Dot -> SqlDelightGraphicalAppearance.Dot
            GraphicalAppearance.Embroidery -> SqlDelightGraphicalAppearance.Embroidery
            GraphicalAppearance.FrontPrint -> SqlDelightGraphicalAppearance.FrontPrint
            GraphicalAppearance.GlitteringMetallic -> SqlDelightGraphicalAppearance.GlitteringMetallic
            GraphicalAppearance.Hologram -> SqlDelightGraphicalAppearance.Hologram
            GraphicalAppearance.Jacquard -> SqlDelightGraphicalAppearance.Jacquard
            GraphicalAppearance.Lace -> SqlDelightGraphicalAppearance.Lace
            GraphicalAppearance.Melange -> SqlDelightGraphicalAppearance.Melange
            GraphicalAppearance.Mesh -> SqlDelightGraphicalAppearance.Mesh
            GraphicalAppearance.Metallic -> SqlDelightGraphicalAppearance.Metallic
            GraphicalAppearance.MixedSolidPattern -> SqlDelightGraphicalAppearance.MixedSolidPattern
            GraphicalAppearance.Neps -> SqlDelightGraphicalAppearance.Neps
            GraphicalAppearance.OtherPattern -> SqlDelightGraphicalAppearance.OtherPattern
            GraphicalAppearance.OtherStructure -> SqlDelightGraphicalAppearance.OtherStructure
            GraphicalAppearance.PlacementPrint -> SqlDelightGraphicalAppearance.PlacementPrint
            GraphicalAppearance.Sequin -> SqlDelightGraphicalAppearance.Sequin
            GraphicalAppearance.Slub -> SqlDelightGraphicalAppearance.Slub
            GraphicalAppearance.Solid -> SqlDelightGraphicalAppearance.Solid
            GraphicalAppearance.Stripe -> SqlDelightGraphicalAppearance.Stripe
            GraphicalAppearance.Transparent -> SqlDelightGraphicalAppearance.Transparent
            GraphicalAppearance.Treatment -> SqlDelightGraphicalAppearance.Treatment
            GraphicalAppearance.Unknown -> SqlDelightGraphicalAppearance.Unknown
        }

    private fun Index.toSqlDelightIndex(): SqlDelightIndex = when (this) {
        Index.BabySizes5098 -> SqlDelightIndex.BabySizes5098
        Index.ChildrenAccessoriesSwimwear -> SqlDelightIndex.ChildrenAccessoriesSwimwear
        Index.ChildrenSizes134170 -> SqlDelightIndex.ChildrenSizes134170
        Index.ChildrenSizes92140 -> SqlDelightIndex.ChildrenSizes92140
        Index.Divided -> SqlDelightIndex.Divided
        Index.LadiesAccessories -> SqlDelightIndex.LadiesAccessories
        Index.LadiesWear -> SqlDelightIndex.LadiesWear
        Index.LingeriesTights -> SqlDelightIndex.LingeriesTights
        Index.Menswear -> SqlDelightIndex.Menswear
        Index.Sport -> SqlDelightIndex.Sport
    }

    private fun Shade.toSqlDelightShade(): SqlDelightShade = when (this) {
        Shade.Bright -> SqlDelightShade.Bright
        Shade.Dark -> SqlDelightShade.Dark
        Shade.DustyLight -> SqlDelightShade.DustyLight
        Shade.Light -> SqlDelightShade.Light
        Shade.Medium -> SqlDelightShade.Medium
        Shade.MediumDusty -> SqlDelightShade.MediumDusty
        Shade.Other -> SqlDelightShade.Other
    }
}