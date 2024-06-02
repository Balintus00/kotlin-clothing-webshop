package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun PlatformSpecificHorizontalListScrollbar(
    listState: LazyListState,
    modifier: Modifier = Modifier,
)

@Composable
internal expect fun PlatformSpecificHorizontalListScrollbar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
)

@Composable
internal expect fun PlatformSpecificVerticalGridScrollbar(
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
)

@Composable
internal expect fun PlatformSpecificVerticalListScrollbar(
    listState: LazyListState,
    modifier: Modifier = Modifier,
)

@Composable
internal expect fun PlatformSpecificVerticalListScrollbar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
)