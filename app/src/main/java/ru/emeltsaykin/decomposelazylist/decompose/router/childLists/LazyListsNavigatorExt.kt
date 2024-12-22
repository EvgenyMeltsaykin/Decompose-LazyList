package ru.emeltsaykin.decomposelazylist.decompose.router.childLists

/**
 * A convenience method for [LazyListsNavigator.navigate].
 */
fun <C : Any> LazyListsNavigator<C>.navigate(transformer: (LazyLists<C>) -> LazyLists<C>) {
    navigate(transformer = transformer, onComplete = { _, _ -> })
}

/**
 * Changes the index of the first visible element and last visible element
 *
 * @param firstIndex index of the first visible element
 * @param lastIndex index of the last visible element
 * @param onComplete called when the navigation is finished (either synchronously or asynchronously).
 */
fun <C : Any> LazyListsNavigator<C>.changeVisibleElementIndexes(
    firstIndex: Int,
    lastIndex: Int,
    onComplete: (newLazyLists: LazyLists<C>, oldLazyLists: LazyLists<C>) -> Unit = { _, _ -> },
) {
    navigate(
        transformer = { lazyLists ->
            lazyLists.copy(
                firstVisibleIndex = firstIndex,
                lastVisibleIndex = lastIndex,
            )
        },
        onComplete = onComplete,
    )
}

/**
 * Changes the index of the first visible element
 *
 * @param index index of the first visible element
 * @param onComplete called when the navigation is finished (either synchronously or asynchronously).
 */
fun <C : Any> LazyListsNavigator<C>.changeFirstVisibleElementIndex(
    index: Int,
    onComplete: (newLazyLists: LazyLists<C>, oldLazyLists: LazyLists<C>) -> Unit = { _, _ -> },
) {
    navigate(
        transformer = { lazyLists ->
            lazyLists.copy(
                firstVisibleIndex = index
            )
        },
        onComplete = onComplete,
    )
}

/**
 * Changes the index of the last visible element
 *
 * @param index index of the last visible element
 * @param onComplete called when the navigation is finished (either synchronously or asynchronously).
 */
fun <C : Any> LazyListsNavigator<C>.changeLastVisibleElementIndex(
    index: Int,
    onComplete: (newLazyLists: LazyLists<C>, oldLazyLists: LazyLists<C>) -> Unit = { _, _ -> },
) {
    navigate(
        transformer = { lazyLists ->

            lazyLists.copy(
                lastVisibleIndex = index
            )
        },
        onComplete = onComplete,
    )
}

/**
 * Clears the current [LazyLists] state, i.e. removes all components.
 *
 * @param onComplete called when the navigation is finished (either synchronously or asynchronously).
 */
fun <C : Any> LazyListsNavigator<C>.clear(onComplete: (newLazyLists: LazyLists<C>, oldLazyLists: LazyLists<C>) -> Unit = { _, _ -> }) {
    navigate(
        transformer = { LazyLists() },
        onComplete = onComplete,
    )
}
