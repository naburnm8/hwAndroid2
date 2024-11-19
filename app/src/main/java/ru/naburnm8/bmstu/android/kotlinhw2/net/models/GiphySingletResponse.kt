package ru.naburnm8.bmstu.android.kotlinhw2.net.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GiphySingletResponse (
    val data: GifData
)

@Serializable
data class GifData(
    @SerialName("title") val title: String,
    @SerialName("images") val images: Images
)

@Serializable
data class Images(
    @SerialName("original") val original : OrigImage
)

@Serializable
data class OrigImage(
    @SerialName("url") val url : String
)
