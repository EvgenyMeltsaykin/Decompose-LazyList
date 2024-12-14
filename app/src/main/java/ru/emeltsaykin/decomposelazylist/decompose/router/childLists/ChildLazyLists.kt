package ru.emeltsaykin.decomposelazylist.decompose.router.childLists

import com.arkivanov.decompose.Child

/**
 * A state holder for Child Lazy Lists.
 *
 * @param items a list of child components.
 * @param firstVisibleIndex an index of the first visible item in the list.
 * @param lastVisibleIndex an index of the last visible item in the list. Must be greater than [firstVisibleIndex]
 */
data class ChildLazyLists<out C : Any, out T : Any> internal constructor(
    val items: List<Child.Created<C, T>>,
    val firstVisibleIndex: Int,
    val lastVisibleIndex: Int,
) {

    constructor(items: List<Child.Created<C, T>>) : this(items = items, firstVisibleIndex = 0, lastVisibleIndex = 0)

    /**
     * Creates empty [ChildLazyLists].
     */
    constructor() : this(items = emptyList(), firstVisibleIndex = 0, lastVisibleIndex = 0)

    init {
        require(lastVisibleIndex >= firstVisibleIndex) {
            "The lastVisibleIndex argument must be greater than firstVisibleIndex: $firstVisibleIndex. Actual: $lastVisibleIndex."
        }
    }
}
