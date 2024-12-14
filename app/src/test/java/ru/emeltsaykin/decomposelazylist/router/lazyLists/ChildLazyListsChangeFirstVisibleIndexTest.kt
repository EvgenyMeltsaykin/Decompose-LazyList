package ru.emeltsaykin.decomposelazylist.router.lazyLists

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.value.getValue
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.LazyLists
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.changeFirstVisibleElementIndex
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

@Suppress("TestFunctionName")
class ChildLazyListsChangeFirstVisibleIndexTest : BaseChildLazyListsTest() {

    private val lifecycle = LifecycleRegistry()
    private val context = DefaultComponentContext(lifecycle = lifecycle)

    @BeforeTest
    fun before() {
        lifecycle.resume()
    }

    @Test
    fun GIVEN_5_lazy_list_items_and_visible_indexes_0_2_WHEN_change_first_visible_index_2() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4),
                firstVisibleIndex = 0,
                lastVisibleIndex = 2
            )
        )

        navigation.changeFirstVisibleElementIndex(index = 2)

        lazyLists.assertLazyLists()
    }

    @Test
    fun GIVEN_5_lazy_list_items_and_visible_indexes_0_2_WHEN_change_first_visible_index_3_THEN_exception() {
        context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4),
                firstVisibleIndex = 0,
                lastVisibleIndex = 2
            )
        )

        assertFailsWith(
            IllegalArgumentException::class,
            "The lastVisibleIndex argument must be greater than firstVisibleIndex: 3. Actual: 2."
        ) { navigation.changeFirstVisibleElementIndex(index = 3) }
    }

    @Test
    fun GIVEN_5_lazy_list_items_and_visible_indexes_1_2_WHEN_change_first_visible_index_1() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4),
                firstVisibleIndex = 1,
                lastVisibleIndex = 2
            )
        )

        navigation.changeFirstVisibleElementIndex(index = 1)

        lazyLists.assertLazyLists()
    }

    @Test
    fun GIVEN_5_lazy_list_items_and_visible_indexes_1_2_WHEN_change_first_visible_index_0() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4),
                firstVisibleIndex = 1,
                lastVisibleIndex = 2
            )
        )

        navigation.changeFirstVisibleElementIndex(index = 0)

        lazyLists.assertLazyLists()
    }
}
