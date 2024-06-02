package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.ArticleRepository
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.ArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State.ArticleListContainerState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State.ArticleListContainerState.NextPageLoadingFailed
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State.ArticleListContainerState.PageLoadedWithPossibleNextPages
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State.ArticleListContainerState.RefreshingFailed
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.paging.ArticlePageSpecification
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.paging.PageSize
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class CharacterListStoreProvider(
    private val filter: ArticleFilter,
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val articleRepository: ArticleRepository by inject()

    fun create(): SearchArticleListStore =
        object : SearchArticleListStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = STORE_NAME,
            initialState = State.FirstPageLoading,
            bootstrapper = SimpleBootstrapper<Action>(Action.LoadFirstPage),
            executorFactory = { Executor(filter, articleRepository) },
            reducer = DefaultReducer,
        ) {}

    private sealed interface Action {

        data object LoadFirstPage : Action
    }

    private class Executor(
        private val filter: ArticleFilter,
        private val repository: ArticleRepository,
    ) :
        CoroutineExecutor<Intent, Action, State, Message, Nothing>() {

        private var articlesCollectorJob: Job? = null

        private var firstPageLoadingJob: Job? = null
        private var nextPageLoadingJob: Job? = null
        private var refreshingJob: Job? = null

        override fun executeAction(action: Action) {
            val currentState = state()
            if (currentState is State.FirstPageLoading) {

                firstPageLoadingJob = scope.launch {
                    loadPage(
                        filter = filter,
                        lastLoadedArticleID = null,
                        onSuccessfulLoadingAction = { collectArticles() },
                    )
                }
            }
        }

        private suspend fun loadPage(
            lastLoadedArticleID: ArticleID?,
            filter: ArticleFilter,
            onSuccessfulLoadingAction: () -> Unit = {},
            onFailedLoadingAction: () -> Unit = {},
        ) {
            try {
                val articlesWithNextPageExistence = repository.loadSearchedArticlePreviews(
                    filter = filter,
                    pageSpecification = ArticlePageSpecification(
                        lastReceivedArticleID = lastLoadedArticleID,
                        size = PageSize(PAGE_SIZE),
                    ),
                )

                onSuccessfulLoadingAction()

                dispatch(
                    Message.FinishedPageLoadingWithSuccess(
                        loadedArticleList = articlesWithNextPageExistence.first,
                        isNextPageExist = articlesWithNextPageExistence.second,
                    )
                )
            } catch (t: Throwable) {
                onFailedLoadingAction()

                dispatch(Message.FinishPageLoadingWithFailure)
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
                intent is Intent.LoadNextPage && state is ArticleListContainerState && (
                        state is PageLoadedWithPossibleNextPages ||
                                state is NextPageLoadingFailed ||
                                (state is RefreshingFailed && state.isLastPageLoaded.not())
                        )
                -> {
                    dispatch(Message.StartedLoadingNextPage)

                    nextPageLoadingJob = scope.launch {
                        loadPage(
                            filter = filter,
                            lastLoadedArticleID = state.lastLoadedArticleID,
                        )
                    }
                }

                intent is Intent.Refresh && (
                        state is PageLoadedWithPossibleNextPages ||
                                state is ArticleListContainerState.NextPageLoading ||
                                state is NextPageLoadingFailed ||
                                state is ArticleListContainerState.LastPageLoaded ||
                                state is RefreshingFailed
                        )
                -> {
                    val characterCollectionAction = { collectArticles() }

                    dispatch(Message.StartedRefreshing)

                    nextPageLoadingJob?.cancel()
                    articlesCollectorJob?.cancel()

                    refreshingJob = scope.launch {
                        loadPage(
                            filter = filter,
                            lastLoadedArticleID = null,
                            onSuccessfulLoadingAction = characterCollectionAction,
                            onFailedLoadingAction = characterCollectionAction,
                        )
                    }
                }

                intent is Intent.Retry && state is State.FirstPageLoadingFailed -> {
                    dispatch(Message.LoadingFirstPage)

                    firstPageLoadingJob = scope.launch {
                        loadPage(
                            filter = filter,
                            lastLoadedArticleID = null,
                            onSuccessfulLoadingAction = { collectArticles() },
                        )
                    }
                }

                intent is Intent.Retry && state is NextPageLoadingFailed -> {
                    dispatch(Message.StartedLoadingNextPage)

                    nextPageLoadingJob = scope.launch {
                        loadPage(
                            filter = filter,
                            lastLoadedArticleID = state.lastLoadedArticleID,
                        )
                    }
                }

                intent is Intent.Retry && state is RefreshingFailed -> {
                    dispatch(Message.StartedRefreshing)

                    refreshingJob = scope.launch {
                        loadPage(
                            filter = filter,
                            lastLoadedArticleID = null,
                        )
                    }
                }
            }
        }

        companion object {
            private const val PAGE_SIZE = 20
        }
    }

    private sealed class Message {

        data object LoadingFirstPage : Message()

        data object StartedLoadingNextPage : Message()

        data object StartedRefreshing : Message()

        data class FinishedPageLoadingWithSuccess(
            val loadedArticleList: List<ArticlePreview>,
            val isNextPageExist: Boolean,
        ) : Message()

        data object FinishPageLoadingWithFailure : Message()

        data class ArticlesChanged(val articles: List<ArticlePreview>) : Message()
    }

    private object DefaultReducer : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State {
            return when {
                msg is Message.LoadingFirstPage -> {
                    State.FirstPageLoading
                }

                msg is Message.StartedLoadingNextPage && this is ArticleListContainerState -> {
                    ArticleListContainerState.NextPageLoading(
                        articles = articles,
                        lastLoadedArticleID = lastLoadedArticleID,
                    )
                }

                msg is Message.StartedRefreshing && this is ArticleListContainerState && (
                        this is ArticleListContainerState.LastPageLoaded ||
                                (this is RefreshingFailed && this.isLastPageLoaded)
                        )
                -> {
                    ArticleListContainerState.Refreshing(
                        articles = articles,
                        lastLoadedArticleID = lastLoadedArticleID,
                        isLastPageLoaded = true,
                    )
                }

                msg is Message.StartedRefreshing && this is ArticleListContainerState && (
                        this is PageLoadedWithPossibleNextPages ||
                                this is ArticleListContainerState.NextPageLoading ||
                                this is NextPageLoadingFailed ||
                                (this is RefreshingFailed && this.isLastPageLoaded.not())
                        )
                -> {
                    ArticleListContainerState.Refreshing(
                        articles = articles,
                        lastLoadedArticleID = lastLoadedArticleID,
                        isLastPageLoaded = false,
                    )
                }

                msg is Message.FinishedPageLoadingWithSuccess && msg.isNextPageExist
                        && (this is State.FirstPageLoading || this is ArticleListContainerState.Refreshing)
                -> {
                    PageLoadedWithPossibleNextPages(
                        articles = msg.loadedArticleList,
                        lastLoadedArticleID = null,
                    )
                }

                msg is Message.FinishedPageLoadingWithSuccess &&
                        (this is State.FirstPageLoading || this is ArticleListContainerState.Refreshing)
                -> {
                    ArticleListContainerState.LastPageLoaded(
                        articles = msg.loadedArticleList,
                        lastLoadedArticleID = null,
                    )
                }

                msg is Message.FinishedPageLoadingWithSuccess && msg.isNextPageExist
                        && this is ArticleListContainerState.NextPageLoading
                -> {
                    PageLoadedWithPossibleNextPages(
                        articles = articles,
                        lastLoadedArticleID = articles.lastOrNull()?.id,
                    )
                }

                msg is Message.FinishedPageLoadingWithSuccess && this is ArticleListContainerState.NextPageLoading -> {
                    ArticleListContainerState.LastPageLoaded(
                        articles = articles,
                        lastLoadedArticleID = articles.lastOrNull()?.id,
                    )
                }

                msg is Message.FinishPageLoadingWithFailure && this is State.FirstPageLoading -> {
                    State.FirstPageLoadingFailed
                }

                msg is Message.FinishPageLoadingWithFailure && this is ArticleListContainerState.Refreshing -> {
                    RefreshingFailed(
                        articles = articles,
                        lastLoadedArticleID = lastLoadedArticleID,
                        isLastPageLoaded = isLastPageLoaded,
                    )
                }

                msg is Message.FinishPageLoadingWithFailure && this is ArticleListContainerState.NextPageLoading -> {
                    NextPageLoadingFailed(
                        articles = articles,
                        lastLoadedArticleID = lastLoadedArticleID,
                    )
                }

                msg is Message.ArticlesChanged && this is ArticleListContainerState -> {
                    getWithChangedArticles(msg.articles)
                }

                else -> this
            }
        }
    }

    companion object {
        private const val STORE_NAME = "SearchArticleListStore"
    }
}