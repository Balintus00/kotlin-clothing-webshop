package hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.HUNGARIAN_ABC_LETTERS
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.NEW_LINE_SEPARATORS
import kotlin.jvm.JvmInline

private const val ARTICLES_ALLOWED_SPECIAL_CHARACTERS = ",#&@()-+%=/.'\";.?!"

data class ArticleFilter(
    val name: Name?,
    val brand: Brand?,
    val minimumPrice: Price?,
    val maximumPrice: Price?,
    val colors: Set<Color>,
    val garmentGroups: Set<GarmentGroup>,
    val graphicalAppearances: Set<GraphicalAppearance>,
    val indices: Set<Index>,
    val shades: Set<Shade>,
)

@JvmInline
@Suppress("unused")
value class ArticleID(val value: String)

@JvmInline
@Suppress("unused")
value class Name(val value: String) {

    init {
        require(value.length >= MINIMUM_LENGTH) { ERROR_MESSAGE_TOO_LONG }
        require(value.length <= MAXIMUM_LENGTH) { ERROR_MESSAGE_TOO_SHORT }
        require(
            value.all {
                it in ALLOWED_LETTERS || it.isDigit()
            }
        ) {
            Brand.ERROR_MESSAGE_INVALID_CHARACTER
        }
        require(value.firstOrNull { it in HUNGARIAN_ABC_LETTERS || it.isDigit() } != null) {
            Brand.ERROR_MESSAGE_EMPTY
        }
    }

    companion object {

        private const val MINIMUM_LENGTH = 1
        const val MAXIMUM_LENGTH = 64

        const val ALLOWED_LETTERS = "$HUNGARIAN_ABC_LETTERS$ARTICLES_ALLOWED_SPECIAL_CHARACTERS "

        const val ERROR_MESSAGE_TOO_SHORT = "Name must be at least $MINIMUM_LENGTH long!"
        const val ERROR_MESSAGE_TOO_LONG = "Name can be maximum $MAXIMUM_LENGTH long!"

        const val ERROR_MESSAGE_INVALID_CHARACTER =
            "Name can contain only Hungarian ABC letters, numbers, whitespaces and the following " +
                    "special characters: $ARTICLES_ALLOWED_SPECIAL_CHARACTERS!"
        const val ERROR_MESSAGE_EMPTY =
            "Name must contain at least one non-whitespace character!"
    }
}

@JvmInline
@Suppress("unused")
value class Brand(val value: String) {

    init {
        require(value.length >= MINIMUM_LENGTH) { ERROR_MESSAGE_TOO_LONG }
        require(value.length <= MAXIMUM_LENGTH) { ERROR_MESSAGE_TOO_SHORT }
        require(value.all { it in ALLOWED_LETTERS }) {
            ERROR_MESSAGE_INVALID_CHARACTER
        }
        require(value.firstOrNull { it in HUNGARIAN_ABC_LETTERS || it.isDigit() } != null) {
            ERROR_MESSAGE_EMPTY
        }
    }

    companion object {

        private const val MINIMUM_LENGTH = 1
        const val MAXIMUM_LENGTH = 64

        const val ALLOWED_LETTERS = "$HUNGARIAN_ABC_LETTERS 0123456789"

        const val ERROR_MESSAGE_TOO_SHORT = "Brand must be at least $MINIMUM_LENGTH long!"
        const val ERROR_MESSAGE_TOO_LONG = "Brand can be maximum $MAXIMUM_LENGTH long!"

        const val ERROR_MESSAGE_INVALID_CHARACTER =
            "Brand can contain only Hungarian ABC letters, numbers and whitespaces!"
        const val ERROR_MESSAGE_EMPTY =
            "Brand must contain at least non-whitespace character!"
    }
}

@JvmInline
@Suppress("unused")
value class Price(val value: Int) {

    init {
        require(value >= MINIMUM_VALUE) { ERROR_MESSAGE_TOO_LOW }
        require(value <= MAXIMUM_VALUE) { ERROR_MESSAGE_TO_HIGH }
    }

    companion object {

        const val MINIMUM_VALUE = 500
        const val MAXIMUM_VALUE = 250000

        const val ERROR_MESSAGE_TOO_LOW = "Price must be at least $MINIMUM_VALUE!"
        const val ERROR_MESSAGE_TO_HIGH = "Price can be maximum $MAXIMUM_VALUE!"
    }
}

@JvmInline
@Suppress("unused")
value class Description(val value: String) {

    init {
        require(value.length >= MINIMUM_LENGTH) { ERROR_MESSAGE_TOO_LONG }
        require(value.length <= MAXIMUM_LENGTH) { ERROR_MESSAGE_TOO_SHORT }
        require(
            value.all {
                it in "$HUNGARIAN_ABC_LETTERS$ARTICLES_ALLOWED_SPECIAL_CHARACTERS"
                        || it in NEW_LINE_SEPARATORS
                        || it.isDigit()
            }
        ) { ERROR_MESSAGE_INVALID_CHARACTER }
    }

    companion object {

        private const val MINIMUM_LENGTH = 1
        private const val MAXIMUM_LENGTH = 4096

        const val ERROR_MESSAGE_TOO_SHORT = "Description must be at least $MINIMUM_LENGTH long!"
        const val ERROR_MESSAGE_TOO_LONG = "Description can be maximum $MAXIMUM_LENGTH long!"

        const val ERROR_MESSAGE_INVALID_CHARACTER =
            "Description can contain Hungarian letters, numbers, whitespaces and line breaks " +
                    "and the following special characters: $ARTICLES_ALLOWED_SPECIAL_CHARACTERS"
    }
}

@Suppress("unused")
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
    YellowishGreen,
}

@Suppress("unused")
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
    WovenJerseyKnittedMixBaby,
}

@Suppress("unused")
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
    Unknown,
}

@Suppress("unused")
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
    Sport,
}

@Suppress("unused")
enum class Shade {
    Bright,
    Dark,
    DustyLight,
    Light,
    Medium,
    MediumDusty,
    Other,
}