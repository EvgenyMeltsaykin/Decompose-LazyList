package ru.emeltsaykin.decomposelazylist.lazyLists

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.arkivanov.decompose.Child
import org.junit.Rule
import org.junit.Test
import ru.emeltsaykin.decomposelazylist.decompose.extensions.compose.childItems.ChildLazyLists
import ru.emeltsaykin.decomposelazylist.decompose.router.childLists.ChildLazyLists

@Suppress("TestFunctionName")
class ChildLazyListsTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun GIVEN_lazy_list_items_displayed_WHEN_add_item_THEN_all_items_exists() {
        val state = mutableStateOf(
            ChildLazyLists<Config, Config>(
                items = listOf(
                    Child.Created(Config.Config1, Config.Config1),
                    Child.Created(Config.Config2, Config.Config2),
                )
            ),
        )

        setContent(state)
        composeRule.onNodeWithText(text = "ListItem0", substring = true).assertExists()
        composeRule.onNodeWithText(text = "ListItem1", substring = true).assertExists()

        state.updateOnIdle {
            ChildLazyLists(
                items = listOf(
                    Child.Created(Config.Config1, Config.Config1),
                    Child.Created(Config.Config2, Config.Config1),
                    Child.Created(Config.Config3, Config.Config3),
                ),
            )
        }
        composeRule.onNodeWithText(text = "ListItem0", substring = true).assertExists()
        composeRule.onNodeWithText(text = "ListItem1", substring = true).assertExists()
        composeRule.onNodeWithText(text = "ListItem2", substring = true).assertExists()
    }

    @Test
    fun GIVEN_lazy_list_items_displayed_WHEN_delete_all_items_THEN_all_items_not_exists() {
        val state = mutableStateOf(
            ChildLazyLists<Config, Config>(
                items = listOf(
                    Child.Created(Config.Config1, Config.Config1),
                    Child.Created(Config.Config2, Config.Config2),
                )
            ),
        )

        setContent(state)
        composeRule.onNodeWithText(text = "ListItem0", substring = true).assertExists()
        composeRule.onNodeWithText(text = "ListItem1", substring = true).assertExists()

        state.updateOnIdle {
            ChildLazyLists(
                items = listOf(),
            )
        }
        composeRule.onNodeWithText(text = "ListItem0", substring = true).assertDoesNotExist()
        composeRule.onNodeWithText(text = "ListItem1", substring = true).assertDoesNotExist()
    }

    private fun setContent(
        lazyLists: State<ChildLazyLists<Config, Config>>,
    ) {
        composeRule.setContent {
            ChildLazyLists(
                listItems = lazyLists.value,
                onFirstIndexVisibleChanged = {},
                onLastIndexVisibleChanged = {},
            ) {
                itemsIndexed(it) { index, item ->
                    ListItem(index = index, listItem = item.instance)
                }
            }
        }

        composeRule.runOnIdle {}
    }

    @Composable
    private fun ListItem(index: Int, listItem: Config) {
        BasicText(text = "ListItem$index=$listItem")
    }

    private fun <T> MutableState<T>.updateOnIdle(func: (T) -> T) {
        composeRule.runOnIdle { value = func(value) }
        composeRule.runOnIdle {}
    }

    private sealed class Config {
        data object Config1 : Config()
        data object Config2 : Config()
        data object Config3 : Config()
    }
}
