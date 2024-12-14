package ru.emeltsaykin.decomposelazylist.decompose.router.childLists

import com.arkivanov.decompose.router.children.NavigationSource

/**
 * Represents [LazyListsNavigator] and [NavigationSource] at the same time.
 */
interface LazyListsNavigation<C : Any> : LazyListsNavigator<C>, NavigationSource<LazyListsNavigation.Event<C>> {

    class Event<C : Any>(
        val transformer: (LazyLists<C>) -> LazyLists<C>,
        val onComplete: (newLazyLists: LazyLists<C>, oldLazyLists: LazyLists<C>) -> Unit = { _, _ -> },
    )
}

/**
 * Returns a default implementation of [LazyListsNavigation].
 * Broadcasts navigation events to all subscribed observers.
 */
fun <C : Any> LazyListNavigation(): LazyListsNavigation<C> = DefaultLazyListsNavigation()
