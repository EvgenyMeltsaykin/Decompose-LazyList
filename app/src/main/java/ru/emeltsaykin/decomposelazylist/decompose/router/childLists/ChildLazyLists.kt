package ru.emeltsaykin.decomposelazylist.decompose.router.childLists

import com.arkivanov.decompose.Child

/**
 * A state holder for Child Lazy Lists.
 *
 * @param items a list of child components.
 * @param firstVisibleIndex an index of the first visible item in the list. -1 if there is no first visible element
 * @param lastVisibleIndex an index of the last visible item in the list. -1 if there is no last visible element
 */
data class ChildLazyLists<out C : Any, out T : Any> internal constructor(
    val items: List<Child<C, T>>,
    val firstVisibleIndex: Int,
    val lastVisibleIndex: Int,
) {

    constructor(items: List<Child<C, T>>) : this(items = items, firstVisibleIndex = -1, lastVisibleIndex = -1)

    /**
     * Creates empty [ChildLazyLists].
     */
    constructor() : this(items = emptyList(), firstVisibleIndex = -1, lastVisibleIndex = -1)
}
