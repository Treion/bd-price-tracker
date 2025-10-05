package com.bd.pricemonitor.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET

interface PriceApiService {
    @GET("api/snapshot")
    suspend fun getSnapshot(): SnapshotResponse
}

object PriceApiFactory {
    fun create(baseUrl: String, debug: Boolean = true): PriceApiService {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = false }
        val logging = HttpLoggingInterceptor().apply {
            level = if (debug) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
        return retrofit.create(PriceApiService::class.java)
    }
}
