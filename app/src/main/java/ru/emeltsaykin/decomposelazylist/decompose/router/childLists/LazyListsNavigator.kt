package ru.emeltsaykin.decomposelazylist.decompose.router.childLists

interface LazyListsNavigator<C : Any> {

    /**
     * Transforms the current [LazyLists] state to a new one.
     *
     * During the navigation process, the Child Lazy Lists navigation model compares the new [LazyLists] state
     * with the previous one. The navigation model ensures that all removed components are destroyed,
     * and updates lifecycles of the existing components to match the new state.
     *
     * The navigation is usually performed synchronously, which means that by the time
     * the `navigate` method returns, the navigation is finished and all component lifecycles are
     * moved into required states.
     *
     * Should be called on the main thread.
     *
     * @param transformer transforms the current [LazyLists] state to a new one.
     * @param onComplete called when the navigation is finished (either synchronously or asynchronously).
     */
    fun navigate(
        transformer: (LazyLists<C>) -> LazyLists<C>,
        onComplete: (newLazyLists: LazyLists<C>, oldLazyLists: LazyLists<C>) -> Unit,
    )
}
