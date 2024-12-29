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
class ChildLazyListsInitialStateTest : BaseChildLazyListsTest() {

    private val lifecycle = LifecycleRegistry()
    private val context = DefaultComponentContext(lifecycle = lifecycle)

    @BeforeTest
    fun before() {
        lifecycle.resume()
    }

    @Test
    fun GIVEN_lazy_list_items_emptyTHEN_lazy_lists_items_empty() {
        val lazyLists by context.childLazyLists(initialPages = LazyLists())

        lazyLists.assertLazyListsEmpty()
    }

    @Test
    fun GIVEN_5_lazy_list_items_and_not_visible_indexes_THEN_first_element_created_other_destroyed() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4),
                firstVisibleIndex = -1,
                lastVisibleIndex = -1
            )
        )
        with(lazyLists) {
            assertCreatedLifecycle(0)
            assertDestroyedLifecycle(1)
            assertDestroyedLifecycle(2)
            assertDestroyedLifecycle(3)
            assertDestroyedLifecycle(4)
        }
    }

    @Test
    fun GIVEN_6_lazy_list_items_and_first_element_not_visible() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4, 5),
                firstVisibleIndex = -1,
                lastVisibleIndex = 1
            )
        )
        with(lazyLists) {
            assertResumedLifecycle(0)
            assertResumedLifecycle(1)
            assertStartedLifecycle(2)
            assertCreatedLifecycle(3)
            assertCreatedLifecycle(4)
            assertDestroyedLifecycle(5)
        }
    }

    @Test
    fun GIVEN_6_lazy_list_items_and_last_element_not_visible() {
        val lazyLists by context.childLazyLists(
            initialPages = LazyLists(
                items = listOf(0, 1, 2, 3, 4, 5),
                firstVisibleIndex = 1,
                lastVisibleIndex = -1
            )
        )
        with(lazyLists) {
            assertResumedLifecycle(0)
            assertResumedLifecycle(1)
            assertStartedLifecycle(2)
            assertCreatedLifecycle(3)
            assertCreatedLifecycle(4)
            assertDestroyedLifecycle(5)
        }
    }
}
