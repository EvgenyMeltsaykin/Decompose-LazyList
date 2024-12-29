package ru.emeltsaykin.decomposelazylist.router.lazyLists

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.serialization.builtins.serializer
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.ChildLazyLists
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.LazyListNavigation
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.LazyLists
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.LazyListsNavigation
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.childLazyLists
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

abstract class BaseChildLazyListsTest {

    protected val navigation: LazyListsNavigation<Int> = LazyListNavigation()

    protected fun ComponentContext.childLazyLists(
        initialPages: LazyLists<Int> = LazyLists(),
        persistent: Boolean = true,
    ): Value<ChildLazyLists<Int, Component>> = childLazyLists(
        source = navigation,
        serializer = Int.serializer().takeIf { persistent },
        initialLazyLists = { initialPages },
        childFactory = BaseChildLazyListsTest::Component,
    )

    protected fun ChildLazyLists<Int, Component>.assertCreatedLifecycle(index: Int) {
        val item = this.items[index]
        assertIs<Child.Created<Int, Component>>(item)
        assertEquals(Lifecycle.State.CREATED, item.instance.lifecycle.state)
    }

    protected fun ChildLazyLists<Int, Component>.assertResumedLifecycle(index: Int) {
        val item = this.items[index]
        assertIs<Child.Created<Int, Component>>(item)
        assertEquals(Lifecycle.State.RESUMED, item.instance.lifecycle.state)
    }

    protected fun ChildLazyLists<Int, Component>.assertStartedLifecycle(index: Int) {
        val item = this.items[index]
        assertIs<Child.Created<Int, Component>>(item)
        assertEquals(Lifecycle.State.STARTED, item.instance.lifecycle.state)
    }

    protected fun ChildLazyLists<Int, Component>.assertDestroyedLifecycle(index: Int) {
        val item = this.items[index]
        assertIs<Child.Destroyed<Int>>(item)
    }

    protected fun ChildLazyLists<Int, Component>.assertLazyListsEmpty() {
        assertTrue(items.isEmpty())
    }

    protected class Component(
        val config: Int,
        componentContext: ComponentContext,
    ) : ComponentContext by componentContext
}
