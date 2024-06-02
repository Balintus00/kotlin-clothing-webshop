package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource.entity

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Brand
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Description
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Name
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Price
import org.komapper.annotation.EnumType
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperEnum
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperTable
import java.time.OffsetDateTime
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.model.Article as DomainArticle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Color as DomainColor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.GarmentGroup as DomainGarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.GraphicalAppearance as DomainGraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Index as DomainIndex
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Shade as DomainShade

@KomapperEntity
@KomapperTable("articles")
data class Article(
    @KomapperId
    val id: String,
    val name: String,
    val price: Int,
    val brand: String,
    val description: String,
    @KomapperEnum(EnumType.ORDINAL)
    val color: Color,
    @KomapperEnum(EnumType.ORDINAL) @KomapperColumn("garment_group")
    val garmentGroup: GarmentGroup,
    @KomapperEnum(EnumType.ORDINAL) @KomapperColumn("graphical_appearance")
    val graphicalAppearance: GraphicalAppearance,
    @KomapperEnum(EnumType.ORDINAL)
    val index: Index,
    @KomapperEnum(EnumType.ORDINAL)
    val shade: Shade,
    @KomapperColumn("creation_date_time")
    val creationDateTime: OffsetDateTime,
)

fun Article.toDomainArticle(): DomainArticle = DomainArticle(
    id = ArticleID(id),
    name = Name(name),
    price = Price(price),
    brand = Brand(brand),
    description = Description(description),
    color = color.toDomainColor(),
    garmentGroup = garmentGroup.toDomainGarmentGroup(),
    graphicalAppearance = graphicalAppearance.toDomainGraphicalAppearance(),
    index = index.toDomainIndex(),
    shade = shade.toDomainShade(),
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

    fun toDomainColor(): DomainColor = when (this) {
        Beige -> DomainColor.Beige
        Black -> DomainColor.Black
        Blue -> DomainColor.Blue
        BluishGreen -> DomainColor.BluishGreen
        Brown -> DomainColor.Brown
        Green -> DomainColor.Green
        Grey -> DomainColor.Grey
        KhakiGreen -> DomainColor.KhakiGreen
        LilacPurple -> DomainColor.LilacPurple
        Metal -> DomainColor.Metal
        Mole -> DomainColor.Mole
        Orange -> DomainColor.Orange
        Pink -> DomainColor.Pink
        Red -> DomainColor.Red
        Turquoise -> DomainColor.Turquoise
        Undefined -> DomainColor.Undefined
        Unknown -> DomainColor.Unknown
        White -> DomainColor.White
        Yellow -> DomainColor.Yellow
        YellowishGreen -> DomainColor.YellowishGreen
    }
}

fun DomainColor.toEntityColor(): Color = when (this) {
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

    fun toDomainGarmentGroup(): DomainGarmentGroup = when (this) {
        Accessories -> DomainGarmentGroup.Accessories
        Blouses -> DomainGarmentGroup.Blouses
        Dressed -> DomainGarmentGroup.Dressed
        DressesLadies -> DomainGarmentGroup.DressesLadies
        DressesSkirtsGirls -> DomainGarmentGroup.DressesSkirtsGirls
        JerseyBasic -> DomainGarmentGroup.JerseyBasic
        JerseyFancy -> DomainGarmentGroup.JerseyFancy
        Knitwear -> DomainGarmentGroup.Knitwear
        Outdoor -> DomainGarmentGroup.Outdoor
        Shirts -> DomainGarmentGroup.Shirts
        Shoes -> DomainGarmentGroup.Shoes
        Shorts -> DomainGarmentGroup.Shorts
        Skirts -> DomainGarmentGroup.Skirts
        SocksAndTights -> DomainGarmentGroup.SocksAndTights
        SpecialOffers -> DomainGarmentGroup.SpecialOffers
        Swimwear -> DomainGarmentGroup.Swimwear
        Trousers -> DomainGarmentGroup.Trousers
        TrousersDenim -> DomainGarmentGroup.TrousersDenim
        UnderNightwear -> DomainGarmentGroup.UnderNightwear
        Unknown -> DomainGarmentGroup.Unknown
        WovenJerseyKnittedMixBaby -> DomainGarmentGroup.WovenJerseyKnittedMixBaby
    }
}

fun DomainGarmentGroup.toEntityGarmentGroup(): GarmentGroup = when (this) {
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

    fun toDomainGraphicalAppearance(): DomainGraphicalAppearance = when (this) {
        AllOverPattern -> DomainGraphicalAppearance.AllOverPattern
        Application3D -> DomainGraphicalAppearance.Application3D
        Argyle -> DomainGraphicalAppearance.Argyle
        Chambray -> DomainGraphicalAppearance.Chambray
        Check -> DomainGraphicalAppearance.Check
        ColourBlocking -> DomainGraphicalAppearance.ColourBlocking
        Contrast -> DomainGraphicalAppearance.Contrast
        Denim -> DomainGraphicalAppearance.Denim
        Dot -> DomainGraphicalAppearance.Dot
        Embroidery -> DomainGraphicalAppearance.Embroidery
        FrontPrint -> DomainGraphicalAppearance.FrontPrint
        GlitteringMetallic -> DomainGraphicalAppearance.GlitteringMetallic
        Hologram -> DomainGraphicalAppearance.Hologram
        Jacquard -> DomainGraphicalAppearance.Jacquard
        Lace -> DomainGraphicalAppearance.Lace
        Melange -> DomainGraphicalAppearance.Melange
        Mesh -> DomainGraphicalAppearance.Mesh
        Metallic -> DomainGraphicalAppearance.Metallic
        MixedSolidPattern -> DomainGraphicalAppearance.MixedSolidPattern
        Neps -> DomainGraphicalAppearance.Neps
        OtherPattern -> DomainGraphicalAppearance.OtherPattern
        OtherStructure -> DomainGraphicalAppearance.OtherStructure
        PlacementPrint -> DomainGraphicalAppearance.PlacementPrint
        Sequin -> DomainGraphicalAppearance.Sequin
        Slub -> DomainGraphicalAppearance.Slub
        Solid -> DomainGraphicalAppearance.Solid
        Stripe -> DomainGraphicalAppearance.Stripe
        Transparent -> DomainGraphicalAppearance.Transparent
        Treatment -> DomainGraphicalAppearance.Treatment
        Unknown -> DomainGraphicalAppearance.Unknown
    }
}

fun DomainGraphicalAppearance.toEntityGraphicalAppearance(): GraphicalAppearance = when (this) {
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

    fun toDomainIndex(): DomainIndex = when (this) {
        BabySizes5098 -> DomainIndex.BabySizes5098
        ChildrenAccessoriesSwimwear -> DomainIndex.ChildrenAccessoriesSwimwear
        ChildrenSizes134170 -> DomainIndex.ChildrenSizes134170
        ChildrenSizes92140 -> DomainIndex.ChildrenSizes92140
        Divided -> DomainIndex.Divided
        LadiesAccessories -> DomainIndex.LadiesAccessories
        LadiesWear -> DomainIndex.LadiesWear
        LingeriesTights -> DomainIndex.LingeriesTights
        Menswear -> DomainIndex.Menswear
        Sport -> DomainIndex.Sport
    }
}

fun DomainIndex.toEntityIndex(): Index = when (this) {
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

enum class Shade {
    Bright,
    Dark,
    DustyLight,
    Light,
    Medium,
    MediumDusty,
    Other;

    fun toDomainShade(): DomainShade = when (this) {
        Bright -> DomainShade.Bright
        Dark -> DomainShade.Dark
        DustyLight -> DomainShade.DustyLight
        Light -> DomainShade.Light
        Medium -> DomainShade.Medium
        MediumDusty -> DomainShade.MediumDusty
        Other -> DomainShade.Other
    }
}

fun DomainShade.toEntityShade(): Shade = when (this) {
    DomainShade.Bright -> Shade.Bright
    DomainShade.Dark -> Shade.Dark
    DomainShade.DustyLight -> Shade.DustyLight
    DomainShade.Light -> Shade.Light
    DomainShade.Medium -> Shade.Medium
    DomainShade.MediumDusty -> Shade.MediumDusty
    DomainShade.Other -> Shade.Other
}