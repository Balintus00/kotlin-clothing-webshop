package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.dto

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.model.Article as DomainArticle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Brand
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Name
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Price
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleFilter as DomainArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Color as DomainColor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.GarmentGroup as DomainGarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.GraphicalAppearance as DomainGraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Index as DomainIndex
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Shade as DomainShade

@GraphQLDescription("Clothing Article")
data class Article(
    @GraphQLDescription("article ID") val id: String,
    @GraphQLDescription("article name") val name: String,
    @GraphQLDescription("article brand") val brand: String,
    @GraphQLDescription("article price") val price: Int,
    @GraphQLDescription("article description") val description: String,
    @GraphQLDescription("article color categorical value identifier") val color: Color,
    @GraphQLDescription("article garment group categorical value identifier")
    val garmentGroup: GarmentGroup,
    @GraphQLDescription("article graphical appearance categorical value identifier")
    val graphicalAppearance: GraphicalAppearance,
    @GraphQLDescription("article index categorical value identifier") val index: Index,
    @GraphQLDescription("article shade categorical value identifier") val shade: Shade,
)

fun DomainArticle.toGraphQLArticle() = Article(
    id = id.value,
    name = name.value,
    brand =  brand.value,
    price = price.value,
    description = description.value,
    color = color.toGraphQLColor(),
    garmentGroup = garmentGroup.toGraphQLGarmentGroup(),
    graphicalAppearance = graphicalAppearance.toGraphQLGraphicalAppearance(),
    index = index.toGraphQLIndex(),
    shade = shade.toGraphQLShade(),
)

@GraphQLDescription("Clothing Article Filter")
data class ArticleFilter(
    val name: String?,
    val brand: String?,
    val minimumPrice: Int?,
    val maximumPrice: Int?,
    val colors: List<Color>,
    val garmentGroups: List<GarmentGroup>,
    val graphicalAppearances: List<GraphicalAppearance>,
    val indices: List<Index>,
    val shades: List<Shade>,
)

fun ArticleFilter.toDomainArticleFilter() = DomainArticleFilter(
    name = name?.let { Name(it) },
    brand = brand?.let { Brand(it) },
    minimumPrice = minimumPrice?.let { Price(it) },
    maximumPrice = maximumPrice?.let { Price(it) },
    colors = colors.map { it.toDomainColor() }.toSet(),
    garmentGroups = garmentGroups.map { it.toDomainGarmentGroup() }.toSet(),
    graphicalAppearances = graphicalAppearances.map { it.toDomainGraphicalAppearance() }.toSet(),
    indices = indices.map { it.toDomainIndex() }.toSet(),
    shades = shades.map { it.toDomainShade() }.toSet(),
)

enum class Color {
    Beige,
    Black,
    Blue,
    BluishGreen,
    Brown,
    Green,
    Grey,
    KhakiGreen,
    LilacPurple,
    Metal,
    Mole,
    Orange,
    Pink,
    Red,
    Turquoise,
    Undefined,
    Unknown,
    White,
    Yellow,
    YellowishGreen;
}

fun DomainColor.toGraphQLColor(): Color = when (this) {
    DomainColor.Beige -> Color.Beige
    DomainColor.Black -> Color.Black
    DomainColor.Blue -> Color.Blue
    DomainColor.BluishGreen -> Color.BluishGreen
    DomainColor.Brown -> Color.Brown
    DomainColor.Green -> Color.Green
    DomainColor.Grey -> Color.Grey
    DomainColor.KhakiGreen -> Color.KhakiGreen
    DomainColor.LilacPurple -> Color.LilacPurple
    DomainColor.Metal -> Color.Metal
    DomainColor.Mole -> Color.Mole
    DomainColor.Orange -> Color.Orange
    DomainColor.Pink -> Color.Pink
    DomainColor.Red -> Color.Red
    DomainColor.Turquoise -> Color.Turquoise
    DomainColor.Undefined -> Color.Undefined
    DomainColor.Unknown -> Color.Unknown
    DomainColor.White -> Color.White
    DomainColor.Yellow -> Color.Yellow
    DomainColor.YellowishGreen -> Color.YellowishGreen
}

fun Color.toDomainColor(): DomainColor = when (this) {
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
    Color.Undefined -> DomainColor.Undefined
    Color.Unknown -> DomainColor.Unknown
    Color.White -> DomainColor.White
    Color.Yellow -> DomainColor.Yellow
    Color.YellowishGreen -> DomainColor.YellowishGreen
}

enum class GarmentGroup {
    Accessories,
    Blouses,
    Dressed,
    DressesLadies,
    DressesSkirtsGirls,
    JerseyBasic,
    JerseyFancy,
    Knitwear,
    Outdoor,
    Shirts,
    Shoes,
    Shorts,
    Skirts,
    SocksAndTights,
    SpecialOffers,
    Swimwear,
    Trousers,
    TrousersDenim,
    UnderNightwear,
    Unknown,
    WovenJerseyKnittedMixBaby;
}

fun DomainGarmentGroup.toGraphQLGarmentGroup(): GarmentGroup = when (this) {
    DomainGarmentGroup.Accessories -> GarmentGroup.Accessories
    DomainGarmentGroup.Blouses -> GarmentGroup.Blouses
    DomainGarmentGroup.Dressed -> GarmentGroup.Dressed
    DomainGarmentGroup.DressesLadies -> GarmentGroup.DressesLadies
    DomainGarmentGroup.DressesSkirtsGirls -> GarmentGroup.DressesSkirtsGirls
    DomainGarmentGroup.JerseyBasic -> GarmentGroup.JerseyBasic
    DomainGarmentGroup.JerseyFancy -> GarmentGroup.JerseyFancy
    DomainGarmentGroup.Knitwear -> GarmentGroup.Knitwear
    DomainGarmentGroup.Outdoor -> GarmentGroup.Outdoor
    DomainGarmentGroup.Shirts -> GarmentGroup.Shirts
    DomainGarmentGroup.Shoes -> GarmentGroup.Shoes
    DomainGarmentGroup.Shorts -> GarmentGroup.Shorts
    DomainGarmentGroup.Skirts -> GarmentGroup.Skirts
    DomainGarmentGroup.SocksAndTights -> GarmentGroup.SocksAndTights
    DomainGarmentGroup.SpecialOffers -> GarmentGroup.SpecialOffers
    DomainGarmentGroup.Swimwear -> GarmentGroup.Swimwear
    DomainGarmentGroup.Trousers -> GarmentGroup.Trousers
    DomainGarmentGroup.TrousersDenim -> GarmentGroup.TrousersDenim
    DomainGarmentGroup.UnderNightwear -> GarmentGroup.UnderNightwear
    DomainGarmentGroup.Unknown -> GarmentGroup.Unknown
    DomainGarmentGroup.WovenJerseyKnittedMixBaby -> GarmentGroup.WovenJerseyKnittedMixBaby
}

fun GarmentGroup.toDomainGarmentGroup(): DomainGarmentGroup = when (this) {
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
    GarmentGroup.Unknown -> DomainGarmentGroup.Unknown
    GarmentGroup.WovenJerseyKnittedMixBaby -> DomainGarmentGroup.WovenJerseyKnittedMixBaby
}

enum class GraphicalAppearance {
    AllOverPattern,
    Application3D,
    Argyle,
    Chambray,
    Check,
    ColourBlocking,
    Contrast,
    Denim,
    Dot,
    Embroidery,
    FrontPrint,
    GlitteringMetallic,
    Hologram,
    Jacquard,
    Lace,
    Melange,
    Mesh,
    Metallic,
    MixedSolidPattern,
    Neps,
    OtherPattern,
    OtherStructure,
    PlacementPrint,
    Sequin,
    Slub,
    Solid,
    Stripe,
    Transparent,
    Treatment,
    Unknown;
}

fun DomainGraphicalAppearance.toGraphQLGraphicalAppearance(): GraphicalAppearance = when (this) {
    DomainGraphicalAppearance.AllOverPattern -> GraphicalAppearance.AllOverPattern
    DomainGraphicalAppearance.Application3D -> GraphicalAppearance.Application3D
    DomainGraphicalAppearance.Argyle -> GraphicalAppearance.Argyle
    DomainGraphicalAppearance.Chambray -> GraphicalAppearance.Chambray
    DomainGraphicalAppearance.Check -> GraphicalAppearance.Check
    DomainGraphicalAppearance.ColourBlocking -> GraphicalAppearance.ColourBlocking
    DomainGraphicalAppearance.Contrast -> GraphicalAppearance.Contrast
    DomainGraphicalAppearance.Denim -> GraphicalAppearance.Denim
    DomainGraphicalAppearance.Dot -> GraphicalAppearance.Dot
    DomainGraphicalAppearance.Embroidery -> GraphicalAppearance.Embroidery
    DomainGraphicalAppearance.FrontPrint -> GraphicalAppearance.FrontPrint
    DomainGraphicalAppearance.GlitteringMetallic -> GraphicalAppearance.GlitteringMetallic
    DomainGraphicalAppearance.Hologram -> GraphicalAppearance.Hologram
    DomainGraphicalAppearance.Jacquard -> GraphicalAppearance.Jacquard
    DomainGraphicalAppearance.Lace -> GraphicalAppearance.Lace
    DomainGraphicalAppearance.Melange -> GraphicalAppearance.Melange
    DomainGraphicalAppearance.Mesh -> GraphicalAppearance.Mesh
    DomainGraphicalAppearance.Metallic -> GraphicalAppearance.Metallic
    DomainGraphicalAppearance.MixedSolidPattern -> GraphicalAppearance.MixedSolidPattern
    DomainGraphicalAppearance.Neps -> GraphicalAppearance.Neps
    DomainGraphicalAppearance.OtherPattern -> GraphicalAppearance.OtherPattern
    DomainGraphicalAppearance.OtherStructure -> GraphicalAppearance.OtherStructure
    DomainGraphicalAppearance.PlacementPrint -> GraphicalAppearance.PlacementPrint
    DomainGraphicalAppearance.Sequin -> GraphicalAppearance.Sequin
    DomainGraphicalAppearance.Slub -> GraphicalAppearance.Slub
    DomainGraphicalAppearance.Solid -> GraphicalAppearance.Solid
    DomainGraphicalAppearance.Stripe -> GraphicalAppearance.Stripe
    DomainGraphicalAppearance.Transparent -> GraphicalAppearance.Transparent
    DomainGraphicalAppearance.Treatment -> GraphicalAppearance.Treatment
    DomainGraphicalAppearance.Unknown -> GraphicalAppearance.Unknown
}

fun GraphicalAppearance.toDomainGraphicalAppearance(): DomainGraphicalAppearance = when (this) {
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
    GraphicalAppearance.Unknown -> DomainGraphicalAppearance.Unknown
}

enum class Index {
    BabySizes5098,
    ChildrenAccessoriesSwimwear,
    ChildrenSizes134170,
    ChildrenSizes92140,
    Divided,
    LadiesAccessories,
    LadiesWear,
    LingeriesTights,
    Menswear,
    Sport;
}

fun DomainIndex.toGraphQLIndex(): Index = when (this) {
    DomainIndex.BabySizes5098 -> Index.BabySizes5098
    DomainIndex.ChildrenAccessoriesSwimwear -> Index.ChildrenAccessoriesSwimwear
    DomainIndex.ChildrenSizes134170 -> Index.ChildrenSizes134170
    DomainIndex.ChildrenSizes92140 -> Index.ChildrenSizes92140
    DomainIndex.Divided -> Index.Divided
    DomainIndex.LadiesAccessories -> Index.LadiesAccessories
    DomainIndex.LadiesWear -> Index.LadiesWear
    DomainIndex.LingeriesTights -> Index.LingeriesTights
    DomainIndex.Menswear -> Index.Menswear
    DomainIndex.Sport -> Index.Sport
}

fun Index.toDomainIndex(): DomainIndex = when (this) {
    Index.BabySizes5098 -> DomainIndex.BabySizes5098
    Index.ChildrenAccessoriesSwimwear -> DomainIndex.ChildrenAccessoriesSwimwear
    Index.ChildrenSizes134170 -> DomainIndex.ChildrenSizes134170
    Index.ChildrenSizes92140 -> DomainIndex.ChildrenSizes92140
    Index.Divided -> DomainIndex.Divided
    Index.LadiesAccessories -> DomainIndex.LadiesAccessories
    Index.LadiesWear -> DomainIndex.LadiesWear
    Index.LingeriesTights -> DomainIndex.LingeriesTights
    Index.Menswear -> DomainIndex.Menswear
    Index.Sport -> DomainIndex.Sport
}

enum class Shade {
    Bright,
    Dark,
    DustyLight,
    Light,
    Medium,
    MediumDusty,
    Other,
}

fun DomainShade.toGraphQLShade(): Shade = when (this) {
    DomainShade.Bright -> Shade.Bright
    DomainShade.Dark -> Shade.Dark
    DomainShade.DustyLight -> Shade.DustyLight
    DomainShade.Light -> Shade.Light
    DomainShade.Medium -> Shade.Medium
    DomainShade.MediumDusty -> Shade.MediumDusty
    DomainShade.Other -> Shade.Other
}

fun Shade.toDomainShade(): DomainShade = when (this) {
    Shade.Bright -> DomainShade.Bright
    Shade.Dark -> DomainShade.Dark
    Shade.DustyLight -> DomainShade.DustyLight
    Shade.Light -> DomainShade.Light
    Shade.Medium -> DomainShade.Medium
    Shade.MediumDusty -> DomainShade.MediumDusty
    Shade.Other -> DomainShade.Other
}