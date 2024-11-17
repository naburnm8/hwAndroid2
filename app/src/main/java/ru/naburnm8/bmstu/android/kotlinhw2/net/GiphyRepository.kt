package ru.naburnm8.bmstu.android.kotlinhw2.net

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class GiphyRepository(api: String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(api)
        .addConverterFactory(Json {ignoreUnknownKeys = true}.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()
}