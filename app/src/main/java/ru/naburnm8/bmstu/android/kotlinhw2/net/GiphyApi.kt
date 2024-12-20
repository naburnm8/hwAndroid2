package ru.naburnm8.bmstu.android.kotlinhw2.net

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.naburnm8.bmstu.android.kotlinhw2.net.models.GiphyListResponse
import ru.naburnm8.bmstu.android.kotlinhw2.net.models.GiphySingletResponse

interface GiphyApi {

    @GET("v1/gifs/random")
    suspend fun getRandomGif(@Query("api_key") apiKey: String, @Query("tag") tag : String): Response<GiphySingletResponse>

    @GET("v1/gifs/trending")
    suspend fun getNTrendingGifs(@Query("api_key") apiKey: String, @Query("limit") limit: Int): Response<GiphyListResponse>

    @GET("v1/gifs/search")
    suspend fun getNQueryGifs(@Query("api_key") apiKey: String, @Query("limit") limit: Int, @Query("q") query: String): Response<GiphyListResponse>

}