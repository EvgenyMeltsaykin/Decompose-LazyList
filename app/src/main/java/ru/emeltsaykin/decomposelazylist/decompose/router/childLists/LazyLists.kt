package ru.emeltsaykin.decomposelazylist.decompose.router.childLists

import kotlinx.serialization.Serializable

/**
 * Represents a state of Child Lazy Lists navigation model.
 *
 * @param items a list of child configurations, must be unique, can be empty.
 * @param firstVisibleIndex an index of the first visible item in the list.
 * @param lastVisibleIndex an index of the last visible item in the list.
 */
@Serializable
data class LazyLists<out C : Any> internal constructor(
    val items: List<C>,
    val firstVisibleIndex: Int,
    val lastVisibleIndex: Int,
) {

    constructor(items: List<C>) : this(items = items, firstVisibleIndex = -1, lastVisibleIndex = -1)

    /**
     * Creates empty [ChildLazyLists].
     */
    constructor() : this(items = emptyList(), firstVisibleIndex = -1, lastVisibleIndex = -1)
}
