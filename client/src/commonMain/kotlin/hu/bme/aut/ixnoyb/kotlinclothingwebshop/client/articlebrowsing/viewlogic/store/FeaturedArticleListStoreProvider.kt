package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.ArticleRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.ArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.FeaturedArticleListStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.FeaturedArticleListStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FeaturedArticleListStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val articleRepository: ArticleRepository by inject()

    fun create(): FeaturedArticleListStore =
        object : FeaturedArticleListStore,
            Store<Intent, State, Nothing> by storeFactory.create(
                name = STORE_NAME,
                initialState = State.Loading,
                bootstrapper = SimpleBootstrapper<Action>(Action.LoadFirstPage),
                executorFactory = { Executor(articleRepository) },
                reducer = DefaultReducer,
            ) {}

    private sealed interface Action {

        data object LoadFirstPage : Action
    }

    private class Executor(
        private val repository: ArticleRepository,
    ) :
        CoroutineExecutor<Intent, Action, State, Message, Nothing>() {

        private var articlesCollectorJob: Job? = null

        private var loadingJob: Job? = null

        override fun executeAction(action: Action) {
            val currentState = state()
            if (currentState is State.Loading) {

                loadingJob = scope.launch {
                    loadPage(
                        onSuccessfulLoadingAction = { collectArticles() },
                    )
                }
            }
        }

        private suspend fun loadPage(
            onSuccessfulLoadingAction: () -> Unit = {},
            onFailedLoadingAction: () -> Unit = {},
        ) {
            try {
                val articles = repository.loadRecommendedArticlePreviews()

                onSuccessfulLoadingAction()

                dispatch(
                    Message.FinishedLoadingWithSuccess(articles)
                )
            } catch (t: Throwable) {
                onFailedLoadingAction()

                dispatch(Message.FinishLoadingWithFailure)
            }
        }

        private fun collectArticles() {
            articlesCollectorJob?.cancel()

            articlesCollectorJob = scope.launch {
                repository.getSearchedArticlesPreviews().collect {
                    dispatch(Message.ArticlesChanged(it))
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            val state = state()

            when {
                intent is Intent.Refresh && (
                        state is State.ArticleListContainerState.Loaded ||
                                state is State.ArticleListContainerState.RefreshingFailed
                        )
                -> {
                    val characterCollectionAction = { collectArticles() }

                    dispatch(Message.StartedRefreshing)

                    articlesCollectorJob?.cancel()

                    loadingJob = scope.launch {
                        loadPage(
                            onSuccessfulLoadingAction = characterCollectionAction,
                            onFailedLoadingAction = characterCollectionAction,
                        )
                    }
                }

                intent is Intent.Retry && state is State.LoadingFailed -> {
                    dispatch(Message.Loading)

                    loadingJob = scope.launch {
                        loadPage(
                            onSuccessfulLoadingAction = { collectArticles() },
                        )
                    }
                }

                intent is Intent.Retry && (
                        state is State.LoadingFailed
                                || state is State.ArticleListContainerState.RefreshingFailed
                        ) -> {
                    dispatch(Message.StartedRefreshing)

                    loadingJob = scope.launch {
                        loadPage()
                    }
                }
            }
        }
    }

    private sealed class Message {

        data object Loading : Message()

        data object StartedRefreshing : Message()

        data class FinishedLoadingWithSuccess(
            val loadedArticleList: List<ArticlePreview>,
        ) : Message()

        data object FinishLoadingWithFailure : Message()

        data class ArticlesChanged(val articles: List<ArticlePreview>) : Message()
    }

    private object DefaultReducer : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State {
            return when {
                msg is Message.Loading -> {
                    State.Loading
                }

                msg is Message.StartedRefreshing && this is State.ArticleListContainerState && (
                        this is State.ArticleListContainerState.RefreshingFailed ||
                                this is State.ArticleListContainerState.Loaded
                        )
                -> {
                    State.ArticleListContainerState.Refreshing(articles)
                }

                msg is Message.StartedRefreshing && this is State.LoadingFailed -> {
                    State.Loading
                }

                msg is Message.FinishedLoadingWithSuccess &&
                        (this is State.Loading || this is State.ArticleListContainerState.Refreshing)
                -> {
                    State.ArticleListContainerState.Loaded(msg.loadedArticleList)
                }

                msg is Message.FinishLoadingWithFailure && this is State.Loading -> {
                    State.LoadingFailed
                }

                msg is Message.FinishLoadingWithFailure
                        && this is State.ArticleListContainerState.Refreshing -> {
                    State.ArticleListContainerState.RefreshingFailed(articles)
                }

                msg is Message.ArticlesChanged && this is State.ArticleListContainerState -> {
                    getWithChangedArticles(msg.articles)
                }

                else -> this
            }
        }
    }

    companion object {
        private const val STORE_NAME = "RecommendedArticleListStore"
    }
}