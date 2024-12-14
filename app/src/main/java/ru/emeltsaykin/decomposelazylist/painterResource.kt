package ru.emeltsaykin.decomposelazylist

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import ru.emeltsaykin.decomposelazylist.components.kitten.ImageResourceId

@Composable
internal fun painterResource(imageResourceId: ImageResourceId): Painter =
    androidx.compose.ui.res.painterResource(
        when (imageResourceId) {
            ImageResourceId.CAT_1 -> R.drawable.cat1
            ImageResourceId.CAT_2 -> R.drawable.cat2
            ImageResourceId.CAT_3 -> R.drawable.cat3
            ImageResourceId.CAT_4 -> R.drawable.cat4
            ImageResourceId.CAT_5 -> R.drawable.cat5
        }
    )