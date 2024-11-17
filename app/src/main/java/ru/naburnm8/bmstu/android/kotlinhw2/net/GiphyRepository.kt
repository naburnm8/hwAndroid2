package ru.naburnm8.bmstu.android.kotlinhw2.net

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.naburnm8.bmstu.android.kotlinhw2.net.models.GiphyListResponse
import ru.naburnm8.bmstu.android.kotlinhw2.net.models.GiphySingletResponse

class GiphyRepository(api: String) {

    private val retrofit = Retrofit.Builder()
        .baseUrl(api)
        .addConverterFactory(Json {ignoreUnknownKeys = true}.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()

    private val giphyApi = retrofit.create(GiphyApi::class.java)

    suspend fun requestRandomGif(apiKey: String, tag: String): GiphySingletResponse? {
        val response = giphyApi.getRandomGif(apiKey, tag)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body
            }
        }
        return null
    }

    suspend fun requestRandomGif(apiKey: String, count: Int): GiphyListResponse? {
        val response = giphyApi.getNTrendingGifs(apiKey, count)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body
            }
        }
        return null
    }
}