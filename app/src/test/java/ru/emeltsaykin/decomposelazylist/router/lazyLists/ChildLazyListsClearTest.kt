package ru.emeltsaykin.decomposelazylist.router.lazyLists

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.value.getValue
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.LazyLists
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.clear
import kotlin.test.BeforeTest
import kotlin.test.Test

@Suppress("TestFunctionName")
class ChildLazyListsClearTest : BaseChildLazyListsTest() {

    private val lifecycle = LifecycleRegistry()
    private val context = DefaultComponentContext(lifecycle = lifecycle)

    @BeforeTest
    fun before() {
        lifecycle.resume()
    }

    @Test
    fun GIVEN_lazy_list_items_empty_WHEN_clear_THEN_lazy_lists_items_empty() {
        val lazyLists by context.childLazyLists(initialPages = LazyLists())

        navigation.clear()

        lazyLists.assertLazyListsEmpty()
    }

    @Test
    fun GIVEN_5_lazy_list_items_and_visible_indexes_0_1_WHEN_clear_THEN_lazy_lists_items_empty() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4),
                firstVisibleIndex = 0,
                lastVisibleIndex = 1
            )
        )

        navigation.clear()

        lazyLists.assertLazyListsEmpty()
    }

    @Test
    fun GIVEN_5_lazy_list_items_and_visible_indexes_0_4_WHEN_clear_THEN_lazy_lists_items_empty() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4),
                firstVisibleIndex = 0,
                lastVisibleIndex = 4
            )
        )

        navigation.clear()

        lazyLists.assertLazyListsEmpty()
    }

    @Test
    fun GIVEN_5_lazy_list_items_and_visible_indexes_2_3_WHEN_clear_THEN_lazy_lists_items_empty() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4),
                firstVisibleIndex = 2,
                lastVisibleIndex = 3
            ),
        )

        navigation.clear()

        lazyLists.assertLazyListsEmpty()
    }

    @Test
    fun GIVEN_5_lazy_list_items_and_visible_indexes_1_1_WHEN_clear_THEN_lazy_lists_items_empty() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4),
                firstVisibleIndex = 1,
                lastVisibleIndex = 1
            ),
        )

        navigation.clear()

        lazyLists.assertLazyListsEmpty()
    }

    @Test
    fun GIVEN_empty_lazy_list_and_visible_indexes_1_1_WHEN_clear_THEN_lazy_lists_items_empty() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(),
                firstVisibleIndex = 1,
                lastVisibleIndex = 1
            ),
        )

        navigation.clear()

        lazyLists.assertLazyListsEmpty()
    }
}
