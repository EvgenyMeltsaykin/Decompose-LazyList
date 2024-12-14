package ru.emeltsaykin.decomposelazylist.components.kitten

import com.arkivanov.decompose.value.Value

interface KittenComponent {

    val model: Value<Model>

    data class Model(
        val imageResourceId: ImageResourceId,
        val text: String,
    )
}
