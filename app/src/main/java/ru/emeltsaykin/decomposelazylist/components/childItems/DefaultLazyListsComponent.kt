package ru.emeltsaykin.decomposelazylist.components.childItems

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.emeltsaykin.decomposelazylist.components.kitten.DefaultKittenComponent
import ru.emeltsaykin.decomposelazylist.components.kitten.ImageResourceId
import ru.emeltsaykin.decomposelazylist.components.kitten.KittenComponent
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.ChildLazyLists
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.LazyListNavigation
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.LazyLists
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.changeVisibleElementIndexes
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.childLazyLists
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.navigate

class DefaultLazyListsComponent(
    componentContext: ComponentContext,
) : LazyListsComponent, ComponentContext by componentContext {

    private val listsNavigation = LazyListNavigation<Config>()
    private val _children: Value<ChildLazyLists<Config, KittenComponent>> =
        childLazyLists(
            source = listsNavigation,
            serializer = Config.serializer(),
            initialLazyLists = {
                val configs = buildList {
                    repeat(12) {
                        add(Config(imageResourceId = safeGetImageSource(it), index = it))
                    }
                }
                LazyLists(
                    items = configs,
                )
            }
        ) { config, componentContext ->
            DefaultKittenComponent(
                componentContext = componentContext,
                imageResourceId = config.imageResourceId
            )
        }


    private fun safeGetImageSource(index: Int): ImageResourceId {
        return ImageResourceId.entries[index % ImageResourceId.entries.size]
    }

    override val children: Value<ChildLazyLists<*, KittenComponent>> = _children

    override fun onVisibleElementChange(firstVisibleIndex: Int, lastVisibleIndex: Int) {
        listsNavigation.changeVisibleElementIndexes(firstIndex = firstVisibleIndex, lastIndex = lastVisibleIndex)
    }

    override fun onAddClick() {
        listsNavigation.navigate(
            transformer = {
                it.copy(items = it.items + Config(imageResourceId = safeGetImageSource(it.items.size), it.items.maxOf { item -> item.index } + 1))
            }
        )
    }

    override fun onDeleteClick() {
        listsNavigation.navigate(
            transformer = {
                it.copy(items = it.items.dropLast(1))
            },
        )
    }

    override fun onShuffleClick() {
        listsNavigation.navigate(
            transformer = {
                it.copy(items = it.items.shuffled())
            },
        )
    }

    @Serializable
    private data class Config(val imageResourceId: ImageResourceId, val index: Int)
}
