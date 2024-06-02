package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.ArticleRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.ArticleDetailsStore.*
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.ArticleDetailsStoreProvider.Message.FailedLoading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.ArticleDetailsStoreProvider.Message.StartedLoading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.ArticleDetailsStoreProvider.Message.SuccessfulLoading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ArticleDetailsStoreProvider(
    private val storeFactory: StoreFactory,
    private val articleID: ArticleID,
    private val isRecommendedArticle: Boolean,
) : KoinComponent {

    private val articleRepository: ArticleRepository by inject()

    fun create(): ArticleDetailsStore =
        object : ArticleDetailsStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = STORE_NAME,
            initialState = State.Loading(articleID),
            bootstrapper = SimpleBootstrapper<Action>(Action.LoadArticle),
            executorFactory = {
                Executor(
                    articleID = articleID,
                    isRecommendedArticle = isRecommendedArticle,
                    repository = articleRepository,
                )
            },
            reducer = DefaultReducer,
        ) {}

    private sealed interface Action {

        data object LoadArticle : Action
    }

    private class Executor(
        private val articleID: ArticleID,
        private val isRecommendedArticle: Boolean,
        private val repository: ArticleRepository,
    ) : CoroutineExecutor<Intent, Action, State, Message, Nothing>() {

        private var articleCollectingJob: Job? = null

        override fun executeAction(action: Action) {
            if (action is Action.LoadArticle && state() is State.Loading) {
                startCollectingArticle()
            }
        }

        private fun startCollectingArticle() {
            articleCollectingJob?.cancel()

            articleCollectingJob = scope.launch {
                repository.getArticleByID(articleID).collect {
                    it?.let { dispatch(SuccessfulLoading(it)) } ?: dispatch(FailedLoading)
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            when {
                intent is Intent.Retry && state() is State.LoadingFailed -> {
                    dispatch(StartedLoading)

                    articleCollectingJob?.cancel()

                    scope.launch {
                        try {
                            dispatch(
                                SuccessfulLoading(
                                    if (isRecommendedArticle) {
                                        repository.loadRecommendedArticleByID(articleID)
                                    } else {
                                        repository.loadSearchedArticleByID(articleID)
                                    }
                                )
                            )
                        } catch (t: Throwable) {
                            dispatch(FailedLoading)
                        }
                    }
                }
            }
        }
    }

    private sealed interface Message {

        data class SuccessfulLoading(val article: Article) : Message

        data object FailedLoading : Message

        data object StartedLoading : Message
    }

    private object DefaultReducer : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State = when (msg) {
            is SuccessfulLoading -> State.Loaded(article = msg.article)
            is FailedLoading -> State.LoadingFailed(articleID)
            is StartedLoading -> State.Loading(articleID)
        }
    }

    companion object {
        private const val STORE_NAME = "ArticleDetailsStore"
    }
}