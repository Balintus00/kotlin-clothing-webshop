package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun PlatformSpecificHorizontalListScrollbar(
    listState: LazyListState,
    modifier: Modifier
) {
    HorizontalScrollbar(
        modifier = modifier,
        adapter = rememberScrollbarAdapter(
            scrollState = listState,
        )
    )
}

@Composable
internal actual fun PlatformSpecificHorizontalListScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    HorizontalScrollbar(
        modifier = modifier,
        adapter = rememberScrollbarAdapter(
            scrollState = scrollState,
        )
    )
}

@Composable
internal actual fun PlatformSpecificVerticalGridScrollbar(
    gridState: LazyGridState,
    modifier: Modifier
) {
    VerticalScrollbar(
        modifier = modifier,
        adapter = rememberScrollbarAdapter(gridState),
    )
}

@Composable
internal actual fun PlatformSpecificVerticalListScrollbar(
    listState: LazyListState,
    modifier: Modifier
) {
    VerticalScrollbar(
        modifier = modifier,
        adapter = rememberScrollbarAdapter(
            scrollState = listState,
        )
    )
}

@Composable
internal actual fun PlatformSpecificVerticalListScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    VerticalScrollbar(
        modifier = modifier,
        adapter = rememberScrollbarAdapter(
            scrollState = scrollState,
        )
    )
}