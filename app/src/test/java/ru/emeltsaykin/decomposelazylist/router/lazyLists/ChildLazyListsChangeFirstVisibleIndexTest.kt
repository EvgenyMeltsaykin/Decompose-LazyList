package ru.emeltsaykin.decomposelazylist.router.lazyLists

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.value.getValue
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.LazyLists
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.changeFirstVisibleElementIndex
import kotlin.test.BeforeTest
import kotlin.test.Test

@Suppress("TestFunctionName")
class ChildLazyListsChangeFirstVisibleIndexTest : BaseChildLazyListsTest() {

    private val lifecycle = LifecycleRegistry()
    private val context = DefaultComponentContext(lifecycle = lifecycle)

    @BeforeTest
    fun before() {
        lifecycle.resume()
    }

    @Test
    fun GIVEN_6_lazy_list_items_and_visible_indexes_0_2_WHEN_change_first_visible_index_2() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4, 5),
                firstVisibleIndex = 0,
                lastVisibleIndex = 2
            )
        )

        navigation.changeFirstVisibleElementIndex(index = 2)

        with(lazyLists) {
            assertCreatedLifecycle(0)
            assertStartedLifecycle(1)
            assertResumedLifecycle(2)
            assertStartedLifecycle(3)
            assertCreatedLifecycle(4)
            assertCreatedLifecycle(5)
        }
    }

    @Test
    fun GIVEN_6_lazy_list_items_and_visible_indexes_0_2_WHEN_change_first_visible_index_3_THEN_exception() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4, 5),
                firstVisibleIndex = 0,
                lastVisibleIndex = 2
            )
        )

        navigation.changeFirstVisibleElementIndex(index = 3)

        with(lazyLists) {
            assertCreatedLifecycle(0)
            assertStartedLifecycle(1)
            assertResumedLifecycle(2)
            assertResumedLifecycle(3)
            assertStartedLifecycle(4)
            assertCreatedLifecycle(5)
        }
    }

    @Test
    fun GIVEN_6_lazy_list_items_and_visible_indexes_1_2_WHEN_change_first_visible_index_1() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4, 5),
                firstVisibleIndex = 1,
                lastVisibleIndex = 2
            )
        )

        navigation.changeFirstVisibleElementIndex(index = 1)

        with(lazyLists) {
            assertStartedLifecycle(0)
            assertResumedLifecycle(1)
            assertResumedLifecycle(2)
            assertStartedLifecycle(3)
            assertCreatedLifecycle(4)
            assertCreatedLifecycle(5)
        }
    }

    @Test
    fun GIVEN_6_lazy_list_items_and_visible_indexes_1_2_WHEN_change_first_visible_index_0() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4, 5),
                firstVisibleIndex = 1,
                lastVisibleIndex = 2
            )
        )

        navigation.changeFirstVisibleElementIndex(index = 0)

        with(lazyLists) {
            assertResumedLifecycle(0)
            assertResumedLifecycle(1)
            assertResumedLifecycle(2)
            assertStartedLifecycle(3)
            assertCreatedLifecycle(4)
            assertCreatedLifecycle(5)
        } 
    }

    @Test
    fun GIVEN_6_lazy_list_items_and_visible_indexes_1_2_WHEN_change_first_element_not_visible() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4, 5),
                firstVisibleIndex = 1,
                lastVisibleIndex = 2
            )
        )

        navigation.changeFirstVisibleElementIndex(index = -1)

        with(lazyLists) {
            assertResumedLifecycle(0)
            assertResumedLifecycle(1)
            assertResumedLifecycle(2)
            assertStartedLifecycle(3)
            assertCreatedLifecycle(4)
            assertCreatedLifecycle(5)
        }
    }
}
