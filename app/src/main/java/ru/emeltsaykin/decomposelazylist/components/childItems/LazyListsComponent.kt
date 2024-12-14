package ru.emeltsaykin.decomposelazylist.components.childItems

import com.arkivanov.decompose.value.Value
import ru.emeltsaykin.decomposelazylist.components.kitten.KittenComponent
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.ChildLazyLists

interface LazyListsComponent {

    val children: Value<ChildLazyLists<*, KittenComponent>>

    fun onFirstVisibleElementChange(index: Int)
    fun onLastVisibleElementChange(index: Int)
    fun onAddClick()
    fun onDeleteClick()
    fun onShuffleClick()
}
