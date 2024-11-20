package ru.naburnm8.bmstu.android.kotlinhw2.net

import android.util.Log
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.naburnm8.bmstu.android.kotlinhw2.net.models.GiphyListResponse
import ru.naburnm8.bmstu.android.kotlinhw2.net.models.GiphySingletResponse
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class GiphyRepository(api: String) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://$api")
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

    suspend fun requestNTrendingGifs(apiKey: String, count: Int): GiphyListResponse? {
        val response = giphyApi.getNTrendingGifs(apiKey, count)
        //Log.println(Log.INFO, "GiphyRepository: request", response.isSuccessful.toString())
        if (response.isSuccessful) {
            val body = response.body()
            //Log.println(Log.INFO, "GiphyRepository: request", body.toString())
            if (body != null) {
                return body
            }
        }
        //Log.println(Log.ERROR, "GiphyRepository: request", response.message() + response.raw())
        return null
    }

    suspend fun requestNQueryGifs(apiKey: String, count: Int, query: String): GiphyListResponse?{
        val response = giphyApi.getNQueryGifs(apiKey, count, query)
        if(response.isSuccessful){
            val body = response.body()
            if(body != null){
                return body
            }
        }
        return null
    }
}