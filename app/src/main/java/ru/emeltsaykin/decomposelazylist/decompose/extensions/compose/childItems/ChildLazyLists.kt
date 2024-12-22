package ru.emeltsaykin.decomposelazylist.decompose.extensions.compose.childItems

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.ChildLazyLists

/**
 * Displays a list of lazyList represented by [ChildLazyLists].
 */
@Composable
fun <C : Any, T : Any> ChildLazyLists(
    listItems: Value<ChildLazyLists<C, T>>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    lazyList: LazyList = defaultLazyColumn(),
    onIndexesVisibleChanged: (firstVisibleIndex: Int, lastVisibleIndex: Int) -> Unit,
    lazyListContent: LazyListScope.(items: List<T?>) -> Unit,
) {
    val state by listItems.subscribeAsState()

    ChildLazyLists(
        modifier = modifier,
        listItems = state,
        lazyList = lazyList,
        onIndexesVisibleChanged = onIndexesVisibleChanged,
        lazyListState = lazyListState,
        lazyListContent = lazyListContent,
    )
}

/**
 * Displays a list of lazyList represented by [ChildLazyLists].
 */
@Composable
fun <C : Any, T : Any> ChildLazyLists(
    listItems: ChildLazyLists<C, T>,
    onIndexesVisibleChanged: (firstVisibleIndex: Int, lastVisibleIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
    lazyList: LazyList = defaultLazyColumn(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyListContent: LazyListScope.(items: List<T?>) -> Unit,
) {

    LaunchedEffect(
        lazyListState
    ) {
        combine(
            snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index },
            snapshotFlow { lazyListState.firstVisibleItemIndex }
        ) { lastIndex, firstIndex ->
            (lastIndex ?: 0) to firstIndex
        }.collectLatest { (lastIndex, firstIndex) ->
            onIndexesVisibleChanged(firstIndex, lastIndex)
        }
    }

    lazyList(
        modifier,
        lazyListState,
    ) {
        lazyListContent(listItems.items.map { it.instance })
    }
}

fun defaultLazyRow(): LazyList =
    {
            modifier,
            state,
            content,
        ->
        LazyRow(
            modifier = modifier,
            state = state,
            content = content,
        )
    }

fun defaultLazyColumn(): LazyList =
    {
            modifier,
            state,
            content,
        ->
        LazyColumn(
            modifier = modifier,
            state = state,
            content = content,
        )
    }

internal typealias LazyList =
    @Composable (
        modifier: Modifier,
        state: LazyListState,
        content: LazyListScope.() -> Unit,
    ) -> Unit
