package ru.emeltsaykin.decomposelazylist.decompose.router.childLists

import com.arkivanov.decompose.Cancellation
import ru.emeltsaykin.decomposelazylist.decompose.Relay

internal class DefaultLazyListsNavigation<C : Any> : LazyListsNavigation<C> {

    private val relay = Relay<LazyListsNavigation.Event<C>>()

    override fun subscribe(observer: (LazyListsNavigation.Event<C>) -> Unit): Cancellation = relay.subscribe(observer)

    override fun navigate(
        transformer: (LazyLists<C>) -> LazyLists<C>,
        onComplete: (newLazyLists: LazyLists<C>, oldLazyLists: LazyLists<C>) -> Unit,
    ) {
        relay.accept(LazyListsNavigation.Event(transformer, onComplete))
    }
}
