package ru.emeltsaykin.decomposelazylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import ru.emeltsaykin.decomposelazylist.components.childItems.DefaultLazyListsComponent
import ru.emeltsaykin.decomposelazylist.screens.ChildItemsComponent
import ru.emeltsaykin.decomposelazylist.ui.theme.DecomposeLazyListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootComponent = DefaultLazyListsComponent(
            componentContext = defaultComponentContext()
        )
        setContent {
            DecomposeLazyListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChildItemsComponent(component = rootComponent)
                }
            }
        }
    }
}