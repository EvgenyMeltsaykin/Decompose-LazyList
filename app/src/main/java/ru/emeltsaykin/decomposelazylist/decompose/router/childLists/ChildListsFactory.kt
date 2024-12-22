package ru.emeltsaykin.decomposelazylist.decompose.router.childLists

import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.decompose.router.children.ChildNavState
import com.arkivanov.decompose.router.children.ChildNavState.Status
import com.arkivanov.decompose.router.children.NavState
import com.arkivanov.decompose.router.children.NavigationSource
import com.arkivanov.decompose.router.children.SimpleChildNavState
import com.arkivanov.decompose.router.children.children
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.statekeeper.SerializableContainer
import com.arkivanov.essenty.statekeeper.consumeRequired
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable


/**
 * Initializes and manages a list of components with one selected (active) component.
 * The list can be empty.
 *
 * **It is strongly recommended to call this method on the Main thread.**
 *
 * @param source a source of navigation events.
 * @param serializer an optional [KSerializer] to be used for serializing and deserializing configurations.
 * If `null` then the navigation state will not be preserved.
 * @param initialLazyLists an initial state of Child Lazy Lists that should be set
 * if there is no saved state. See [LazyLists] for more information.
 * @param key a key of the navigation, must be unique if there are multiple Child Lazy Lists used in
 * the same component.
 * @param listItemStatus a function that returns the [Status] of the page at the specified index.
 * By default, the index of the element between the first visible element inclusive and the last visible element inclusive
 * is [Status.RESUMED], the element before the first visible and after the last visible
 * is [Status.STARTED], and the rest are [Status.CREATED]. You can implement your own logic
 * @param childFactory a factory function that creates new child instances.
 * @return an observable [Value] of [ChildLazyLists].
 */
fun <Ctx : GenericComponentContext<Ctx>, C : Any, T : Any> Ctx.childLazyLists(
    source: NavigationSource<LazyListsNavigation.Event<C>>,
    serializer: KSerializer<C>?,
    initialLazyLists: () -> LazyLists<C> = { LazyLists() },
    key: String = "DefaultChildLazyLists",
    listItemStatus: (index: Int, LazyLists<C>) -> Status = ::getDefaultListItemStatus,
    childFactory: (configuration: C, Ctx) -> T,
): Value<ChildLazyLists<C, T>> =
    childLazyLists(
        source = source,
        saveLazyLists = { lazyLists ->
            if (serializer != null) {
                SerializableContainer(
                    value = SerializableLazyLists(
                        items = lazyLists.items,
                        firstVisibleIndex = lazyLists.firstVisibleIndex,
                        lastVisibleIndex = lazyLists.lastVisibleIndex,
                    ),
                    strategy = SerializableLazyLists.serializer(serializer),
                )
            } else {
                null
            }
        },
        restoreLazyLists = { container ->
            if (serializer != null) {
                val lazyLists = container.consumeRequired(strategy = SerializableLazyLists.serializer(serializer))
                LazyLists(
                    items = lazyLists.items,
                    firstVisibleIndex = lazyLists.firstVisibleIndex,
                    lastVisibleIndex = lazyLists.lastVisibleIndex,
                )
            } else {
                null
            }
        },
        initialLazyLists = initialLazyLists,
        key = key,
        listItemStatus = listItemStatus,
        childFactory = childFactory,
    )

@Serializable
private class SerializableLazyLists<out C : Any>(
    val items: List<C>,
    val firstVisibleIndex: Int,
    val lastVisibleIndex: Int,
)

/**
 * Initializes and manages a list of components with one selected (active) component.
 * The list can be empty.
 *
 * **It is strongly recommended to call this method on the Main thread.**
 *
 * @param source a source of navigation events.
 * @param initialLazyLists an initial state of Child Lazy Lists that should be set
 * if there is no saved state. See [LazyLists] for more information.
 * @param saveLazyLists a function that saves the provided [LazyLists] state into [SerializableContainer].
 * The navigation state is not saved if `null` is returned.
 * @param restoreLazyLists a function that restores the [LazyLists] state from the provided [SerializableContainer].
 * If `null` is returned then [initialLazyLists] is used instead.
 * The restored [LazyLists] state must have the same amount of configurations and in the same order.
 * @param key a key of the navigation, must be unique if there are multiple Child Lazy Lists used in
 * the same component.
 * @param listItemStatus a function that returns the [Status] of the page at the specified index.
 * By default, the index of the element between the first visible element inclusive and the last visible element inclusive
 * is [Status.RESUMED], the element before the first visible and after the last visible
 * is [Status.STARTED], and the rest are [Status.CREATED]. You can implement your own logic
 * @param childFactory a factory function that creates new child instances.
 * @return an observable [Value] of [ChildLazyLists].
 */
fun <Ctx : GenericComponentContext<Ctx>, C : Any, T : Any> Ctx.childLazyLists(
    source: NavigationSource<LazyListsNavigation.Event<C>>,
    initialLazyLists: () -> LazyLists<C> = { LazyLists() },
    saveLazyLists: (LazyLists<C>) -> SerializableContainer?,
    restoreLazyLists: (SerializableContainer) -> LazyLists<C>?,
    key: String = "DefaultChildLazyLists",
    listItemStatus: (index: Int, LazyLists<C>) -> Status = ::getDefaultListItemStatus,
    childFactory: (configuration: C, Ctx) -> T,
): Value<ChildLazyLists<C, T>> =
    children(
        source = source,
        key = key,
        initialState = {
            LazyListNavState(
                items = initialLazyLists(),
                listItemStatus = listItemStatus,
            )
        },
        saveState = { saveLazyLists(it.items) },
        restoreState = { container ->
            LazyListNavState(
                items = restoreLazyLists(container) ?: initialLazyLists(),
                listItemStatus = listItemStatus
            )
        },
        navTransformer = { state, event ->
            LazyListNavState(
                items = event.transformer(state.items),
                listItemStatus = listItemStatus,
            )
        },
        stateMapper = { state, children ->
            ChildLazyLists(
                items = children,
                firstVisibleIndex = state.items.firstVisibleIndex,
                lastVisibleIndex = state.items.lastVisibleIndex,
            )
        },
        childFactory = childFactory,
    )

@PublishedApi
internal fun getDefaultListItemStatus(index: Int, lists: LazyLists<*>): Status {
    return when (index) {
        in (lists.firstVisibleIndex..lists.lastVisibleIndex) -> Status.RESUMED
        in (lists.firstVisibleIndex - DefaultCreatedThreshold..lists.lastVisibleIndex + DefaultCreatedThreshold) -> Status.STARTED
        in (lists.firstVisibleIndex - DefaultStartedThreshold..lists.lastVisibleIndex + DefaultStartedThreshold) -> Status.CREATED
        else -> Status.DESTROYED
    }
}

@Serializable
private data class LazyListNavState<out C : Any>(
    val items: LazyLists<C>,
    private val listItemStatus: (index: Int, LazyLists<C>) -> Status,
) : NavState<C> {

    override val children: List<ChildNavState<C>> by lazy {
        items.items.mapIndexed { index, config ->
            SimpleChildNavState(
                configuration = config,
                status = listItemStatus(index, items),
            )
        }
    }
}

private const val DefaultCreatedThreshold = 3
private const val DefaultStartedThreshold = 1
