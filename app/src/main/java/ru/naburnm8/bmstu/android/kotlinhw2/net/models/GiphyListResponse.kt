package ru.naburnm8.bmstu.android.kotlinhw2.net.models

import kotlinx.serialization.Serializable

@Serializable
data class GiphyListResponse (
    val data: List<GifData>
)