package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesResultComponent.ViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesResultComponent.ViewState.ArticlePreviewsAvailable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesResultComponent.ViewState.Loading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.ArticlePreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface SearchArticlesResultComponent {

    val viewState: StateFlow<ViewState>

    fun navigateBack()

    fun navigateToArticleDetails(articleID: String)

    sealed interface ViewState {

        interface Loading : ViewState

        interface ArticlePreviewsAvailable : ViewState {

            val articles: List<ArticlePreview>
        }
    }
}

internal class DefaultSearchArticlesResultComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val navigateBackAction: () -> Unit = {},
    private val navigateToArticleDetailsAction: (String) -> Unit = {},
) : SearchArticlesResultComponent, ComponentContext by componentContext {

    override val viewState: StateFlow<ViewState> = MutableStateFlow(
        object : ArticlePreviewsAvailable, Loading {
            override val articles: List<ArticlePreview> = listOf(
                /*ArticlePreview(
                    id = "1",
                    brand = "THE NORTH FACE",
                    description = "Step into the realm of unmatched style and power with our exclusive Drip Jacket, a homage to the iconic attire worn by the legendary Goku. Crafted with the finest materials and infused with the spirit of the Saiyan warrior, this jacket embodies strength, agility, and unmatched fashion flair.\n" +
                            "Designed with meticulous attention to detail, the Drip Jacket features a sleek silhouette and bold, vibrant colors reminiscent of Goku's aura during his most epic battles. Its lightweight yet durable construction ensures comfort and freedom of movement, allowing you to channel your inner Super Saiyan with ease.\n" +
                            "The jacket's signature elements include intricate embroidery and emblems inspired by Goku's iconic symbols, paying tribute to his journey from a humble Saiyan to the defender of the universe. Whether you're training at the gym, hanging out with friends, or simply conquering everyday challenges, this jacket will elevate your style to legendary heights.\n" +
                            "Unlock the power of the Ultra Instinct and make a statement like never before with our Drip Jacket. Join Goku on his quest for greatness and let your fashion sense reach new levels of mastery. Embrace the fusion of style and strength and become the ultimate trendsetter in the world of fashion.",
                    imageUrl = "https://i.kym-cdn.com/entries/icons/facebook/000/036/141/Drip_Goku.jpg",
                    name = "The Legendary Drip Jacker Inspired by Goku",
                ),
                ArticlePreview(
                    id = "2",
                    brand = "THE NORTH FACE",
                    description = "Step into the realm of unmatched style and power with our exclusive Drip Jacket, a homage to the iconic attire worn by the legendary Goku. Crafted with the finest materials and infused with the spirit of the Saiyan warrior, this jacket embodies strength, agility, and unmatched fashion flair.\n" +
                            "Designed with meticulous attention to detail, the Drip Jacket features a sleek silhouette and bold, vibrant colors reminiscent of Goku's aura during his most epic battles. Its lightweight yet durable construction ensures comfort and freedom of movement, allowing you to channel your inner Super Saiyan with ease.\n" +
                            "The jacket's signature elements include intricate embroidery and emblems inspired by Goku's iconic symbols, paying tribute to his journey from a humble Saiyan to the defender of the universe. Whether you're training at the gym, hanging out with friends, or simply conquering everyday challenges, this jacket will elevate your style to legendary heights.\n" +
                            "Unlock the power of the Ultra Instinct and make a statement like never before with our Drip Jacket. Join Goku on his quest for greatness and let your fashion sense reach new levels of mastery. Embrace the fusion of style and strength and become the ultimate trendsetter in the world of fashion.",
                    imageUrl = "https://i.kym-cdn.com/entries/icons/facebook/000/036/141/Drip_Goku.jpg",
                    name = "The Legendary Drip Jacker Inspired by Goku",
                ),
                ArticlePreview(
                    id = "3",
                    brand = "THE NORTH FACE",
                    description = "Step into the realm of unmatched style and power with our exclusive Drip Jacket, a homage to the iconic attire worn by the legendary Goku. Crafted with the finest materials and infused with the spirit of the Saiyan warrior, this jacket embodies strength, agility, and unmatched fashion flair.\n" +
                            "Designed with meticulous attention to detail, the Drip Jacket features a sleek silhouette and bold, vibrant colors reminiscent of Goku's aura during his most epic battles. Its lightweight yet durable construction ensures comfort and freedom of movement, allowing you to channel your inner Super Saiyan with ease.\n" +
                            "The jacket's signature elements include intricate embroidery and emblems inspired by Goku's iconic symbols, paying tribute to his journey from a humble Saiyan to the defender of the universe. Whether you're training at the gym, hanging out with friends, or simply conquering everyday challenges, this jacket will elevate your style to legendary heights.\n" +
                            "Unlock the power of the Ultra Instinct and make a statement like never before with our Drip Jacket. Join Goku on his quest for greatness and let your fashion sense reach new levels of mastery. Embrace the fusion of style and strength and become the ultimate trendsetter in the world of fashion.",
                    imageUrl = "https://i.kym-cdn.com/entries/icons/facebook/000/036/141/Drip_Goku.jpg",
                    name = "The Legendary Drip Jacker Inspired by Goku",
                ),
                ArticlePreview(
                    id = "4",
                    brand = "THE NORTH FACE",
                    description = "Step into the realm of unmatched style and power with our exclusive Drip Jacket, a homage to the iconic attire worn by the legendary Goku. Crafted with the finest materials and infused with the spirit of the Saiyan warrior, this jacket embodies strength, agility, and unmatched fashion flair.\n" +
                            "Designed with meticulous attention to detail, the Drip Jacket features a sleek silhouette and bold, vibrant colors reminiscent of Goku's aura during his most epic battles. Its lightweight yet durable construction ensures comfort and freedom of movement, allowing you to channel your inner Super Saiyan with ease.\n" +
                            "The jacket's signature elements include intricate embroidery and emblems inspired by Goku's iconic symbols, paying tribute to his journey from a humble Saiyan to the defender of the universe. Whether you're training at the gym, hanging out with friends, or simply conquering everyday challenges, this jacket will elevate your style to legendary heights.\n" +
                            "Unlock the power of the Ultra Instinct and make a statement like never before with our Drip Jacket. Join Goku on his quest for greatness and let your fashion sense reach new levels of mastery. Embrace the fusion of style and strength and become the ultimate trendsetter in the world of fashion.",
                    imageUrl = "https://i.kym-cdn.com/entries/icons/facebook/000/036/141/Drip_Goku.jpg",
                    name = "The Legendary Drip Jacker Inspired by Goku",
                ),*/
            )
        }
    )

    override fun navigateBack() {
        navigateBackAction()
    }

    override fun navigateToArticleDetails(articleID: String) {
        navigateToArticleDetailsAction(articleID)
    }
}