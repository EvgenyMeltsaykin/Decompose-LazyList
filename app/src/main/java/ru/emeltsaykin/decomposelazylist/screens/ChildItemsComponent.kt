package ru.emeltsaykin.decomposelazylist.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.emeltsaykin.decomposelazylist.components.childItems.LazyListsComponent
import ru.emeltsaykin.decomposelazylist.decompose.extensions.compose.childItems.ChildLazyLists

@Composable
fun ChildItemsComponent(
    component: LazyListsComponent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.navigationBarsPadding()
    ) {
        ChildLazyLists(
            modifier = Modifier.weight(1f),
            listItems = component.children,
            onFirstIndexVisibleChanged = component::onFirstVisibleElementChange,
            onLastIndexVisibleChanged = component::onLastVisibleElementChange,
        ) { childItems ->
            items(childItems) {
                KittenContent(
                    modifier = modifier.fillMaxWidth(),
                    component = it.instance,
                    textStyle = TextStyle.Default
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = component::onDeleteClick
            ) {
                Text(text = "Delete")
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = component::onAddClick
            ) {
                Text(text = "Add")
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = component::onShuffleClick
            ) {
                Text(text = "Shuffle")
            }
        }
    }
}
