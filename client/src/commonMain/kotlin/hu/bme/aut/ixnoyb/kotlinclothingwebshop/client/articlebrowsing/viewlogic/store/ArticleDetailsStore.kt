package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Store
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.ArticleDetailsStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.ArticleDetailsStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID

interface ArticleDetailsStore : Store<Intent, State, Nothing> {

    sealed interface Intent {

        data object Retry : Intent
    }

    sealed interface State {

        val articleID: ArticleID

        data class Loading(override val articleID: ArticleID) : State

        data class Loaded(val article: Article) : State {

            override val articleID: ArticleID
                get() = article.id
        }

        data class LoadingFailed(override val articleID: ArticleID) : State
    }
}