package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.datasource

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.GetArticleByIDQuery
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.GetArticlesQuery
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.GetRecommendedArticlesQuery
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.datasource.ArticleRemoteDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.ArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.datasource.getServerBaseUrl
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.AuthenticationToken
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleFilter
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
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.paging.ArticlePageSpecification
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.type.ArticleFilterInput
import io.ktor.http.Url
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.type.Color as GraphQLColor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.type.GarmentGroup as GraphQLGarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.type.GraphicalAppearance as GraphQLGraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.type.Index as GraphQLIndex
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.type.Shade as GraphQLShade

internal class ApolloArticleRemoteDatasource(
    private val apolloClient: ApolloClient,
) : ArticleRemoteDatasource {

    override suspend fun getArticlePreviews(
        filter: ArticleFilter,
        pageSpecification: ArticlePageSpecification
    ): List<ArticlePreview> {
        val response = apolloClient
            .query(
                GetArticlesQuery(
                    filter = filter.toGraphQLFilter(),
                    lastReadArticleID = pageSpecification.lastReceivedArticleID?.value
                        .toApolloOptional(),
                    pageSize = pageSpecification.size.value,
                )
            )
            .execute()

        return response.dataOrThrow().getArticles.map { it.toArticlePreview() }
    }

    private inline fun ArticleFilter.toGraphQLFilter(): ArticleFilterInput = ArticleFilterInput(
        brand = brand?.value.toApolloOptional(),
        colors = colors.map { it.toGraphQLColor() }.toList(),
        garmentGroups = garmentGroups.map { it.toGraphQLGarmentGroup() }.toList(),
        graphicalAppearances = graphicalAppearances.map {
            it.toGraphQLGraphicalAppearance()
        }.toList(),
        indices = indices.map { it.toGraphQLIndex() }.toList(),
        maximumPrice = maximumPrice?.value.toApolloOptional(),
        minimumPrice = minimumPrice?.value.toApolloOptional(),
        name = name?.value.toApolloOptional(),
        shades = shades.map { it.toGraphQLShade() }.toList(),
    )

    private fun Color.toGraphQLColor(): GraphQLColor = when (this) {
        Color.Beige -> GraphQLColor.Beige
        Color.Black -> GraphQLColor.Black
        Color.Blue -> GraphQLColor.Blue
        Color.BluishGreen -> GraphQLColor.BluishGreen
        Color.Brown -> GraphQLColor.Brown
        Color.Green -> GraphQLColor.Green
        Color.Grey -> GraphQLColor.Grey
        Color.KhakiGreen -> GraphQLColor.KhakiGreen
        Color.LilacPurple -> GraphQLColor.LilacPurple
        Color.Metal -> GraphQLColor.Metal
        Color.Mole -> GraphQLColor.Mole
        Color.Orange -> GraphQLColor.Orange
        Color.Pink -> GraphQLColor.Pink
        Color.Red -> GraphQLColor.Red
        Color.Turquoise -> GraphQLColor.Turquoise
        Color.Undefined -> GraphQLColor.Undefined
        Color.Unknown -> GraphQLColor.Unknown
        Color.White -> GraphQLColor.White
        Color.Yellow -> GraphQLColor.Yellow
        Color.YellowishGreen -> GraphQLColor.YellowishGreen
    }

    private fun GarmentGroup.toGraphQLGarmentGroup(): GraphQLGarmentGroup = when (this) {
        GarmentGroup.Accessories -> GraphQLGarmentGroup.Accessories
        GarmentGroup.Blouses -> GraphQLGarmentGroup.Blouses
        GarmentGroup.Dressed -> GraphQLGarmentGroup.Dressed
        GarmentGroup.DressesLadies -> GraphQLGarmentGroup.DressesLadies
        GarmentGroup.DressesSkirtsGirls -> GraphQLGarmentGroup.DressesSkirtsGirls
        GarmentGroup.JerseyBasic -> GraphQLGarmentGroup.JerseyBasic
        GarmentGroup.JerseyFancy -> GraphQLGarmentGroup.JerseyFancy
        GarmentGroup.Knitwear -> GraphQLGarmentGroup.Knitwear
        GarmentGroup.Outdoor -> GraphQLGarmentGroup.Outdoor
        GarmentGroup.Shirts -> GraphQLGarmentGroup.Shirts
        GarmentGroup.Shoes -> GraphQLGarmentGroup.Shoes
        GarmentGroup.Shorts -> GraphQLGarmentGroup.Shorts
        GarmentGroup.Skirts -> GraphQLGarmentGroup.Skirts
        GarmentGroup.SocksAndTights -> GraphQLGarmentGroup.SocksAndTights
        GarmentGroup.SpecialOffers -> GraphQLGarmentGroup.SpecialOffers
        GarmentGroup.Swimwear -> GraphQLGarmentGroup.Swimwear
        GarmentGroup.Trousers -> GraphQLGarmentGroup.Trousers
        GarmentGroup.TrousersDenim -> GraphQLGarmentGroup.TrousersDenim
        GarmentGroup.UnderNightwear -> GraphQLGarmentGroup.UnderNightwear
        GarmentGroup.Unknown -> GraphQLGarmentGroup.Unknown
        GarmentGroup.WovenJerseyKnittedMixBaby -> GraphQLGarmentGroup.WovenJerseyKnittedMixBaby
    }

    private fun GraphicalAppearance.toGraphQLGraphicalAppearance(): GraphQLGraphicalAppearance =
        when (this) {
            GraphicalAppearance.AllOverPattern -> GraphQLGraphicalAppearance.AllOverPattern
            GraphicalAppearance.Application3D -> GraphQLGraphicalAppearance.Application3D
            GraphicalAppearance.Argyle -> GraphQLGraphicalAppearance.Argyle
            GraphicalAppearance.Chambray -> GraphQLGraphicalAppearance.Chambray
            GraphicalAppearance.Check -> GraphQLGraphicalAppearance.Check
            GraphicalAppearance.ColourBlocking -> GraphQLGraphicalAppearance.ColourBlocking
            GraphicalAppearance.Contrast -> GraphQLGraphicalAppearance.Contrast
            GraphicalAppearance.Denim -> GraphQLGraphicalAppearance.Denim
            GraphicalAppearance.Dot -> GraphQLGraphicalAppearance.Dot
            GraphicalAppearance.Embroidery -> GraphQLGraphicalAppearance.Embroidery
            GraphicalAppearance.FrontPrint -> GraphQLGraphicalAppearance.FrontPrint
            GraphicalAppearance.GlitteringMetallic -> GraphQLGraphicalAppearance.GlitteringMetallic
            GraphicalAppearance.Hologram -> GraphQLGraphicalAppearance.Hologram
            GraphicalAppearance.Jacquard -> GraphQLGraphicalAppearance.Jacquard
            GraphicalAppearance.Lace -> GraphQLGraphicalAppearance.Lace
            GraphicalAppearance.Melange -> GraphQLGraphicalAppearance.Melange
            GraphicalAppearance.Mesh -> GraphQLGraphicalAppearance.Mesh
            GraphicalAppearance.Metallic -> GraphQLGraphicalAppearance.Metallic
            GraphicalAppearance.MixedSolidPattern -> GraphQLGraphicalAppearance.MixedSolidPattern
            GraphicalAppearance.Neps -> GraphQLGraphicalAppearance.Neps
            GraphicalAppearance.OtherPattern -> GraphQLGraphicalAppearance.OtherPattern
            GraphicalAppearance.OtherStructure -> GraphQLGraphicalAppearance.OtherStructure
            GraphicalAppearance.PlacementPrint -> GraphQLGraphicalAppearance.PlacementPrint
            GraphicalAppearance.Sequin -> GraphQLGraphicalAppearance.Sequin
            GraphicalAppearance.Slub -> GraphQLGraphicalAppearance.Slub
            GraphicalAppearance.Solid -> GraphQLGraphicalAppearance.Solid
            GraphicalAppearance.Stripe -> GraphQLGraphicalAppearance.Stripe
            GraphicalAppearance.Transparent -> GraphQLGraphicalAppearance.Transparent
            GraphicalAppearance.Treatment -> GraphQLGraphicalAppearance.Treatment
            GraphicalAppearance.Unknown -> GraphQLGraphicalAppearance.Unknown
        }

    private fun Index.toGraphQLIndex(): GraphQLIndex = when (this) {
        Index.BabySizes5098 -> GraphQLIndex.BabySizes5098
        Index.ChildrenAccessoriesSwimwear -> GraphQLIndex.ChildrenAccessoriesSwimwear
        Index.ChildrenSizes134170 -> GraphQLIndex.ChildrenSizes134170
        Index.ChildrenSizes92140 -> GraphQLIndex.ChildrenSizes92140
        Index.Divided -> GraphQLIndex.Divided
        Index.LadiesAccessories -> GraphQLIndex.LadiesAccessories
        Index.LadiesWear -> GraphQLIndex.LadiesWear
        Index.LingeriesTights -> GraphQLIndex.LingeriesTights
        Index.Menswear -> GraphQLIndex.Menswear
        Index.Sport -> GraphQLIndex.Sport
    }

    private fun Shade.toGraphQLShade(): GraphQLShade = when (this) {
        Shade.Bright -> GraphQLShade.Bright
        Shade.Dark -> GraphQLShade.Dark
        Shade.DustyLight -> GraphQLShade.DustyLight
        Shade.Light -> GraphQLShade.Light
        Shade.Medium -> GraphQLShade.Medium
        Shade.MediumDusty -> GraphQLShade.MediumDusty
        Shade.Other -> GraphQLShade.Other
    }

    private inline fun <reified T> T?.toApolloOptional(): Optional<T> = this?.let {
        Optional.present(it)
    } ?: Optional.absent()

    private inline fun GetArticlesQuery.GetArticle.toArticlePreview(): ArticlePreview {
        val articleID = ArticleID(id)

        return ArticlePreview(
            id = articleID,
            brand = Brand(brand),
            description = Description(description),
            imageUrl = articleID.toImageUrl(),
            name = Name(name),
            price = Price(price),
        )
    }

    private inline fun ArticleID.toImageUrl(): Url = Url(
        "${getServerBaseUrl()}/article/image/${value}"
    )

    override suspend fun getByID(articleID: ArticleID): Article {
        val response = apolloClient
            .query(GetArticleByIDQuery(articleID.value))
            .execute()

        return if (response.errors?.any {
                it.message.contains(BACKEND_ERROR_ARTICLE_NOT_FOUND)
            } == true
        ) {
            throw IllegalArgumentException(ArticleRemoteDatasource.ERROR_MESSAGE_NOT_FOUND)
        } else {
            response.dataOrThrow().getArticleById.toArticle()
        }
    }

    private inline fun GetArticleByIDQuery.GetArticleById.toArticle(): Article {
        val articleID = ArticleID(id)

        return Article(
            id = articleID,
            brand = Brand(brand),
            color = color.toDomainColor(),
            description = Description(description),
            garmentGroup = garmentGroup.toDomainGarmentGroup(),
            graphicalAppearance = graphicalAppearance.toDomainGraphicalAppearance(),
            imageUrl = articleID.toImageUrl(),
            index = index.toDomainIndex(),
            name = Name(name),
            price = Price(price),
            shade = shade.toDomainShade(),
        )
    }

    private fun GraphQLColor.toDomainColor(): Color = when (this) {
        GraphQLColor.Beige -> Color.Beige
        GraphQLColor.Black -> Color.Black
        GraphQLColor.Blue -> Color.Blue
        GraphQLColor.BluishGreen -> Color.BluishGreen
        GraphQLColor.Brown -> Color.Brown
        GraphQLColor.Green -> Color.Green
        GraphQLColor.Grey -> Color.Grey
        GraphQLColor.KhakiGreen -> Color.KhakiGreen
        GraphQLColor.LilacPurple -> Color.LilacPurple
        GraphQLColor.Metal -> Color.Metal
        GraphQLColor.Mole -> Color.Mole
        GraphQLColor.Orange -> Color.Orange
        GraphQLColor.Pink -> Color.Pink
        GraphQLColor.Red -> Color.Red
        GraphQLColor.Turquoise -> Color.Turquoise
        GraphQLColor.Undefined -> Color.Undefined
        GraphQLColor.Unknown, GraphQLColor.UNKNOWN__ -> Color.Unknown
        GraphQLColor.White -> Color.White
        GraphQLColor.Yellow -> Color.Yellow
        GraphQLColor.YellowishGreen -> Color.YellowishGreen
    }

    private fun GraphQLGarmentGroup.toDomainGarmentGroup(): GarmentGroup = when (this) {
        GraphQLGarmentGroup.Accessories -> GarmentGroup.Accessories
        GraphQLGarmentGroup.Blouses -> GarmentGroup.Blouses
        GraphQLGarmentGroup.Dressed -> GarmentGroup.Dressed
        GraphQLGarmentGroup.DressesLadies -> GarmentGroup.DressesLadies
        GraphQLGarmentGroup.DressesSkirtsGirls -> GarmentGroup.DressesSkirtsGirls
        GraphQLGarmentGroup.JerseyBasic -> GarmentGroup.JerseyBasic
        GraphQLGarmentGroup.JerseyFancy -> GarmentGroup.JerseyFancy
        GraphQLGarmentGroup.Knitwear -> GarmentGroup.Knitwear
        GraphQLGarmentGroup.Outdoor -> GarmentGroup.Outdoor
        GraphQLGarmentGroup.Shirts -> GarmentGroup.Shirts
        GraphQLGarmentGroup.Shoes -> GarmentGroup.Shoes
        GraphQLGarmentGroup.Shorts -> GarmentGroup.Shorts
        GraphQLGarmentGroup.Skirts -> GarmentGroup.Skirts
        GraphQLGarmentGroup.SocksAndTights -> GarmentGroup.SocksAndTights
        GraphQLGarmentGroup.SpecialOffers -> GarmentGroup.SpecialOffers
        GraphQLGarmentGroup.Swimwear -> GarmentGroup.Swimwear
        GraphQLGarmentGroup.Trousers -> GarmentGroup.Trousers
        GraphQLGarmentGroup.TrousersDenim -> GarmentGroup.TrousersDenim
        GraphQLGarmentGroup.UnderNightwear -> GarmentGroup.UnderNightwear
        GraphQLGarmentGroup.Unknown, GraphQLGarmentGroup.UNKNOWN__ -> GarmentGroup.Unknown
        GraphQLGarmentGroup.WovenJerseyKnittedMixBaby -> GarmentGroup.WovenJerseyKnittedMixBaby
    }

    private fun GraphQLGraphicalAppearance.toDomainGraphicalAppearance(): GraphicalAppearance =
        when (this) {
            GraphQLGraphicalAppearance.AllOverPattern -> {
                GraphicalAppearance.AllOverPattern
            }

            GraphQLGraphicalAppearance.Application3D -> {
                GraphicalAppearance.Application3D
            }

            GraphQLGraphicalAppearance.Argyle -> {
                GraphicalAppearance.Argyle
            }

            GraphQLGraphicalAppearance.Chambray -> {
                GraphicalAppearance.Chambray
            }

            GraphQLGraphicalAppearance.Check -> {
                GraphicalAppearance.Check
            }

            GraphQLGraphicalAppearance.ColourBlocking -> {
                GraphicalAppearance.ColourBlocking
            }

            GraphQLGraphicalAppearance.Contrast -> {
                GraphicalAppearance.Contrast
            }

            GraphQLGraphicalAppearance.Denim -> {
                GraphicalAppearance.Denim
            }

            GraphQLGraphicalAppearance.Dot -> {
                GraphicalAppearance.Dot
            }

            GraphQLGraphicalAppearance.Embroidery -> {
                GraphicalAppearance.Embroidery
            }

            GraphQLGraphicalAppearance.FrontPrint -> {
                GraphicalAppearance.FrontPrint
            }

            GraphQLGraphicalAppearance.GlitteringMetallic -> {
                GraphicalAppearance.GlitteringMetallic
            }

            GraphQLGraphicalAppearance.Hologram -> {
                GraphicalAppearance.Hologram
            }

            GraphQLGraphicalAppearance.Jacquard -> {
                GraphicalAppearance.Jacquard
            }

            GraphQLGraphicalAppearance.Lace -> {
                GraphicalAppearance.Lace
            }

            GraphQLGraphicalAppearance.Melange -> {
                GraphicalAppearance.Melange
            }

            GraphQLGraphicalAppearance.Mesh -> {
                GraphicalAppearance.Mesh
            }

            GraphQLGraphicalAppearance.Metallic -> {
                GraphicalAppearance.Metallic
            }

            GraphQLGraphicalAppearance.MixedSolidPattern -> {
                GraphicalAppearance.MixedSolidPattern
            }

            GraphQLGraphicalAppearance.Neps -> {
                GraphicalAppearance.Neps
            }

            GraphQLGraphicalAppearance.OtherPattern -> {
                GraphicalAppearance.OtherPattern
            }

            GraphQLGraphicalAppearance.OtherStructure -> {
                GraphicalAppearance.OtherStructure
            }

            GraphQLGraphicalAppearance.PlacementPrint -> {
                GraphicalAppearance.PlacementPrint
            }

            GraphQLGraphicalAppearance.Sequin -> {
                GraphicalAppearance.Sequin
            }

            GraphQLGraphicalAppearance.Slub -> {
                GraphicalAppearance.Slub
            }

            GraphQLGraphicalAppearance.Solid -> {
                GraphicalAppearance.Solid
            }

            GraphQLGraphicalAppearance.Stripe -> {
                GraphicalAppearance.Stripe
            }

            GraphQLGraphicalAppearance.Transparent -> {
                GraphicalAppearance.Transparent
            }

            GraphQLGraphicalAppearance.Treatment -> {
                GraphicalAppearance.Treatment
            }

            GraphQLGraphicalAppearance.Unknown, GraphQLGraphicalAppearance.UNKNOWN__ -> {
                GraphicalAppearance.Unknown
            }
        }

    private fun GraphQLIndex.toDomainIndex(): Index = when (this) {
        GraphQLIndex.BabySizes5098 -> Index.BabySizes5098
        GraphQLIndex.ChildrenAccessoriesSwimwear -> Index.ChildrenAccessoriesSwimwear
        GraphQLIndex.ChildrenSizes134170 -> Index.ChildrenSizes134170
        GraphQLIndex.ChildrenSizes92140 -> Index.ChildrenSizes92140
        GraphQLIndex.Divided, GraphQLIndex.UNKNOWN__ -> Index.Divided
        GraphQLIndex.LadiesAccessories -> Index.LadiesAccessories
        GraphQLIndex.LadiesWear -> Index.LadiesWear
        GraphQLIndex.LingeriesTights -> Index.LingeriesTights
        GraphQLIndex.Menswear -> Index.Menswear
        GraphQLIndex.Sport -> Index.Sport
    }

    private fun GraphQLShade.toDomainShade(): Shade = when (this) {
        GraphQLShade.Bright -> Shade.Bright
        GraphQLShade.Dark -> Shade.Dark
        GraphQLShade.DustyLight -> Shade.DustyLight
        GraphQLShade.Light -> Shade.Light
        GraphQLShade.Medium -> Shade.Medium
        GraphQLShade.MediumDusty -> Shade.MediumDusty
        GraphQLShade.Other, GraphQLShade.UNKNOWN__ -> Shade.Other
    }

    override suspend fun getRecommendedArticlePreviews(
        authenticationToken: AuthenticationToken?,
    ): List<ArticlePreview> {
        val response = apolloClient
            .query(GetRecommendedArticlesQuery())
            .apply {
                authenticationToken?.let {
                    addHttpHeader(
                        name = HTTP_HEADER_NAME_AUTHENTICATION,
                        value = it.toAuthenticationHeaderBearerToken()
                    )
                }
            }
            .execute()

        return response.dataOrThrow().getRecommendedArticles.map { it.toArticlePreview() }
    }

    private fun AuthenticationToken.toAuthenticationHeaderBearerToken(): String = "Bearer $value"

    private inline fun GetRecommendedArticlesQuery
    .GetRecommendedArticle.toArticlePreview(): ArticlePreview {
        val articleID = ArticleID(id)

        return ArticlePreview(
            id = articleID,
            brand = Brand(brand),
            description = Description(description),
            imageUrl = articleID.toImageUrl(),
            name = Name(name),
            price = Price(price),
        )
    }

    companion object {

        private const val HTTP_HEADER_NAME_AUTHENTICATION = "Authentication"

        private const val BACKEND_ERROR_ARTICLE_NOT_FOUND = "ERROR_MESSAGE_NOT_FOUND"
    }
}