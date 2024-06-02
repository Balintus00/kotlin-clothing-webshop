package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Brand
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Name
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Price
import kotlinx.serialization.Serializable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleFilter as DomainArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Color as DomainColor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.GarmentGroup as DomainGarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.GraphicalAppearance as DomainGraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Index as DomainIndex
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Shade as DomainShade

// TODO is it fine to expose Serializable annotated object on public API?
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

internal fun ArticleFilter.toDomainArticleFilter(): DomainArticleFilter = DomainArticleFilter(
    colors = colors.map { it.toDomainColor() }.toSet(),
    brand = if (brand.isNullOrEmpty().not()) {
        Brand(brand!!)
    } else {
        null
    },
    garmentGroups = garmentGroups.map { it.toDomainGarmentGroup() }.toSet(),
    graphicalAppearances = graphicalAppearances.map { it.toDomainGraphicalAppearance() }.toSet(),
    indices = indices.map { it.toDomainIndex() }.toSet(),
    minimumPrice = minimumPrice?.let { Price(it) },
    maximumPrice = maximumPrice?.let { Price(it) },
    name = if (name.isNullOrEmpty().not()) {
        Name(name!!)
    } else {
        null
    },
    shades = shades.map { it.toDomainShade() }.toSet(),
)

internal fun Color.toDomainColor(): DomainColor = when (this) {
    Color.Beige -> DomainColor.Beige
    Color.Black -> DomainColor.Black
    Color.Blue -> DomainColor.Blue
    Color.BluishGreen -> DomainColor.BluishGreen
    Color.Brown -> DomainColor.Brown
    Color.Green -> DomainColor.Green
    Color.Grey -> DomainColor.Grey
    Color.KhakiGreen -> DomainColor.KhakiGreen
    Color.LilacPurple -> DomainColor.LilacPurple
    Color.Metal -> DomainColor.Metal
    Color.Mole -> DomainColor.Mole
    Color.Orange -> DomainColor.Orange
    Color.Pink -> DomainColor.Pink
    Color.Red -> DomainColor.Red
    Color.Turquoise -> DomainColor.Turquoise
    Color.White -> DomainColor.White
    Color.Yellow -> DomainColor.Yellow
    Color.YellowishGreen -> DomainColor.YellowishGreen
    Color.Other -> DomainColor.Unknown
}

internal fun GarmentGroup.toDomainGarmentGroup(): DomainGarmentGroup = when (this) {
    GarmentGroup.Accessories -> DomainGarmentGroup.Accessories
    GarmentGroup.Blouses -> DomainGarmentGroup.Blouses
    GarmentGroup.Dressed -> DomainGarmentGroup.Dressed
    GarmentGroup.DressesLadies -> DomainGarmentGroup.DressesLadies
    GarmentGroup.DressesSkirtsGirls -> DomainGarmentGroup.DressesSkirtsGirls
    GarmentGroup.JerseyBasic -> DomainGarmentGroup.JerseyBasic
    GarmentGroup.JerseyFancy -> DomainGarmentGroup.JerseyFancy
    GarmentGroup.Knitwear -> DomainGarmentGroup.Knitwear
    GarmentGroup.Outdoor -> DomainGarmentGroup.Outdoor
    GarmentGroup.Shirts -> DomainGarmentGroup.Shirts
    GarmentGroup.Shoes -> DomainGarmentGroup.Shoes
    GarmentGroup.Shorts -> DomainGarmentGroup.Shorts
    GarmentGroup.Skirts -> DomainGarmentGroup.Skirts
    GarmentGroup.SocksAndTights -> DomainGarmentGroup.SocksAndTights
    GarmentGroup.SpecialOffers -> DomainGarmentGroup.SpecialOffers
    GarmentGroup.Swimwear -> DomainGarmentGroup.Swimwear
    GarmentGroup.Trousers -> DomainGarmentGroup.Trousers
    GarmentGroup.TrousersDenim -> DomainGarmentGroup.TrousersDenim
    GarmentGroup.UnderNightwear -> DomainGarmentGroup.UnderNightwear
    GarmentGroup.WovenJerseyKnittedMixBaby -> DomainGarmentGroup.WovenJerseyKnittedMixBaby
    GarmentGroup.Other -> DomainGarmentGroup.Unknown
}

internal fun GraphicalAppearance.toDomainGraphicalAppearance(): DomainGraphicalAppearance = when (this) {
    GraphicalAppearance.AllOverPattern -> DomainGraphicalAppearance.AllOverPattern
    GraphicalAppearance.Application3D -> DomainGraphicalAppearance.Application3D
    GraphicalAppearance.Argyle -> DomainGraphicalAppearance.Argyle
    GraphicalAppearance.Chambray -> DomainGraphicalAppearance.Chambray
    GraphicalAppearance.Check -> DomainGraphicalAppearance.Check
    GraphicalAppearance.ColourBlocking -> DomainGraphicalAppearance.ColourBlocking
    GraphicalAppearance.Contrast -> DomainGraphicalAppearance.Contrast
    GraphicalAppearance.Denim -> DomainGraphicalAppearance.Denim
    GraphicalAppearance.Dot -> DomainGraphicalAppearance.Dot
    GraphicalAppearance.Embroidery -> DomainGraphicalAppearance.Embroidery
    GraphicalAppearance.FrontPrint -> DomainGraphicalAppearance.FrontPrint
    GraphicalAppearance.GlitteringMetallic -> DomainGraphicalAppearance.GlitteringMetallic
    GraphicalAppearance.Hologram -> DomainGraphicalAppearance.Hologram
    GraphicalAppearance.Jacquard -> DomainGraphicalAppearance.Jacquard
    GraphicalAppearance.Lace -> DomainGraphicalAppearance.Lace
    GraphicalAppearance.Melange -> DomainGraphicalAppearance.Melange
    GraphicalAppearance.Mesh -> DomainGraphicalAppearance.Mesh
    GraphicalAppearance.Metallic -> DomainGraphicalAppearance.Metallic
    GraphicalAppearance.MixedSolidPattern -> DomainGraphicalAppearance.MixedSolidPattern
    GraphicalAppearance.Neps -> DomainGraphicalAppearance.Neps
    GraphicalAppearance.OtherPattern -> DomainGraphicalAppearance.OtherPattern
    GraphicalAppearance.OtherStructure -> DomainGraphicalAppearance.OtherStructure
    GraphicalAppearance.PlacementPrint -> DomainGraphicalAppearance.PlacementPrint
    GraphicalAppearance.Sequin -> DomainGraphicalAppearance.Sequin
    GraphicalAppearance.Slub -> DomainGraphicalAppearance.Slub
    GraphicalAppearance.Solid -> DomainGraphicalAppearance.Solid
    GraphicalAppearance.Stripe -> DomainGraphicalAppearance.Stripe
    GraphicalAppearance.Transparent -> DomainGraphicalAppearance.Transparent
    GraphicalAppearance.Treatment -> DomainGraphicalAppearance.Treatment
}

internal fun Index.toDomainIndex(): DomainIndex = when (this) {
    Index.BabySizes -> DomainIndex.BabySizes5098
    Index.ChildrenAccessories -> DomainIndex.ChildrenAccessoriesSwimwear
    Index.ChildrenSizes134_170 -> DomainIndex.ChildrenSizes134170
    Index.ChildrenSizes92_140 -> DomainIndex.ChildrenSizes92140
    Index.Divided -> DomainIndex.Divided
    Index.LadiesAccessories -> DomainIndex.LadiesAccessories
    Index.Ladieswear -> DomainIndex.LadiesWear
    Index.LingeriesTights -> DomainIndex.LingeriesTights
    Index.Menswear -> DomainIndex.Menswear
    Index.Sport -> DomainIndex.Sport
    Index.Swimwear -> DomainIndex.ChildrenAccessoriesSwimwear
    Index.Other -> DomainIndex.Divided // TODO
}

internal fun Shade.toDomainShade(): DomainShade = when (this) {
    Shade.Bright -> DomainShade.Bright
    Shade.Dark -> DomainShade.Dark
    Shade.DustyLight -> DomainShade.DustyLight
    Shade.Light -> DomainShade.Light
    Shade.Medium -> DomainShade.Medium
    Shade.MediumDusty -> DomainShade.MediumDusty
    Shade.Other -> DomainShade.Other
}