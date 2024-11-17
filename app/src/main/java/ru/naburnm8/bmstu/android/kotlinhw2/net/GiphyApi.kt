package ru.naburnm8.bmstu.android.kotlinhw2.net

import retrofit2.http.GET
import retrofit2.http.Query
import ru.naburnm8.bmstu.android.kotlinhw2.net.models.GiphyListResponse
import ru.naburnm8.bmstu.android.kotlinhw2.net.models.GiphySingletResponse

interface GiphyApi {

    @GET("v1/gifs/random")
    suspend fun getRandomGif(@Query("api-key") apiKey: String, @Query("tag") tag: String): GiphySingletResponse

    @GET("v1/gifs/trending")
    suspend fun getNTrendingGifs(@Query("api-key") apiKey: String, @Query("limit") limit: Int): GiphyListResponse

}