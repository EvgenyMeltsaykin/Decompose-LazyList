package ru.emeltsaykin.decomposelazylist.decompose.extensions.compose.childItems

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
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
    onFirstIndexVisibleChanged: (index: Int) -> Unit,
    onLastIndexVisibleChanged: (index: Int) -> Unit,
    lazyListContent: LazyListScope.(items: List<Child<C, T>>) -> Unit,
) {
    val state by listItems.subscribeAsState()

    ChildLazyLists(
        modifier = modifier,
        listItems = state,
        lazyList = lazyList,
        onFirstIndexVisibleChanged = onFirstIndexVisibleChanged,
        onLastIndexVisibleChanged = onLastIndexVisibleChanged,
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
    onFirstIndexVisibleChanged: (index: Int) -> Unit,
    onLastIndexVisibleChanged: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    lazyList: LazyList = defaultLazyColumn(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyListContent: LazyListScope.(items: List<Child<C, T>>) -> Unit,
) {
    LaunchedEffect(lazyListState, listItems) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .map { itemInfo ->
                listItems.items.indexOfLast { it.configuration == itemInfo?.contentType }
            }
            .collectLatest { index ->
                onLastIndexVisibleChanged(index)
            }
    }

    LaunchedEffect(lazyListState, listItems) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.firstOrNull() }
            .map { itemInfo ->
                listItems.items.indexOfFirst { it.configuration == itemInfo?.contentType }
            }
            .collectLatest { index ->
                onFirstIndexVisibleChanged(index)
            }
    }
    lazyList(
        modifier,
        lazyListState,
    ) {
        lazyListContent(listItems.items)
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


fun <C : Any, T : Any> LazyListScope.childLazyItems(
    items: List<Child<C, T>>,
    key: ((Child<C, T>) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(item: T) -> Unit,
) = items(
    count = items.size,
    key = if (key != null) { index: Int -> key(items[index]) } else null,
    contentType = { index: Int -> items[index].configuration }
) { index ->
    val instance = items[index].instance
    if (instance != null) {
        itemContent(instance)
    }
}

fun <C : Any, T : Any> LazyListScope.childLazyItemsIndexed(
    items: List<Child<C, T>>,
    key: ((Child<C, T>) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
) = items(
    count = items.size,
    key = if (key != null) { index: Int -> key(items[index]) } else null,
    contentType = { index: Int -> items[index].configuration }
) { index ->
    val instance = items[index].instance
    if (instance != null) {
        itemContent(index, instance)
    }
}
