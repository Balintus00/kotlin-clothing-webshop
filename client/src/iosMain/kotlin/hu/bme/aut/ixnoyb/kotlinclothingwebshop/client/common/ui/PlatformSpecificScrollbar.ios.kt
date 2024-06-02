package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun PlatformSpecificHorizontalListScrollbar(
    listState: LazyListState,
    modifier: Modifier
) {
    // No-op
}

@Composable
internal actual fun PlatformSpecificHorizontalListScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    // No-op
}

@Composable
internal actual fun PlatformSpecificVerticalGridScrollbar(
    gridState: LazyGridState,
    modifier: Modifier
) {
    // No-op
}

@Composable
internal actual fun PlatformSpecificVerticalListScrollbar(
    listState: LazyListState,
    modifier: Modifier
) {
    // No-op
}

@Composable
internal actual fun PlatformSpecificVerticalListScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    // No-op
}