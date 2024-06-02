package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Color as DomainColor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.GarmentGroup as DomainGarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.GraphicalAppearance as DomainGraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Index as DomainIndex
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Shade as DomainShade

data class ArticlePreview(
    val id: String,
    val brand: String,
    val description: String,
    val imageUrl: String,
    val name: String,
)

data class Article(
    val id: String,
    val brand: String,
    val color: Color,
    val description: String,
    val garmentGroup: GarmentGroup,
    val graphicalAppearance: GraphicalAppearance,
    val imageUrl: String,
    val index: Index,
    val name: String,
    val price: Int,
    val shade: Shade,
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
    White,
    Yellow,
    YellowishGreen,
    Other,
}

internal fun DomainColor.toUIColor(): Color = when (this) {
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
    DomainColor.Undefined, DomainColor.Unknown -> Color.Other
    DomainColor.White -> Color.White
    DomainColor.Yellow -> Color.Yellow
    DomainColor.YellowishGreen -> Color.YellowishGreen
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
    WovenJerseyKnittedMixBaby,
    Other,
}

internal fun DomainGarmentGroup.toUIGarmentGroup(): GarmentGroup = when (this) {
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
    DomainGarmentGroup.Unknown -> GarmentGroup.Other
    DomainGarmentGroup.WovenJerseyKnittedMixBaby -> GarmentGroup.WovenJerseyKnittedMixBaby
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
}

internal fun DomainGraphicalAppearance.toUIGraphicalAppearance(): GraphicalAppearance =
    when (this) {
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
        DomainGraphicalAppearance.Unknown -> GraphicalAppearance.OtherPattern
    }

enum class Index {
    BabySizes,
    ChildrenAccessories,
    ChildrenSizes134_170,
    ChildrenSizes92_140,
    Divided,
    LadiesAccessories,
    Ladieswear,
    LingeriesTights,
    Menswear,
    Sport,
    Swimwear,
    Other,
}

internal fun DomainIndex.toUIIndex(): Index = when (this) {
    DomainIndex.BabySizes5098 -> Index.BabySizes
    DomainIndex.ChildrenAccessoriesSwimwear -> Index.ChildrenAccessories
    DomainIndex.ChildrenSizes134170 -> Index.ChildrenSizes134_170
    DomainIndex.ChildrenSizes92140 -> Index.ChildrenSizes92_140
    DomainIndex.Divided -> Index.Divided
    DomainIndex.LadiesAccessories -> Index.LadiesAccessories
    DomainIndex.LadiesWear -> Index.Ladieswear
    DomainIndex.LingeriesTights -> Index.LingeriesTights
    DomainIndex.Menswear -> Index.Menswear
    DomainIndex.Sport -> Index.Sport
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

internal fun DomainShade.toUIShade(): Shade = when (this) {
    DomainShade.Bright -> Shade.Bright
    DomainShade.Dark -> Shade.Dark
    DomainShade.DustyLight -> Shade.DustyLight
    DomainShade.Light -> Shade.Light
    DomainShade.Medium -> Shade.Medium
    DomainShade.MediumDusty -> Shade.MediumDusty
    DomainShade.Other -> Shade.Other
}