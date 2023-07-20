package com.example.evoting.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {

    private const val BASE_URL: String = "http://10.0.2.2:8000/api/" //TODO: url only for development with emulator
//    private const val BASE_URL: String = "http://192.168.1.5:8000/api/"

    private fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(4000, TimeUnit.MILLISECONDS)
            .readTimeout(4000, TimeUnit.MILLISECONDS)
            .build()
    }

    val endpoint: ApiEndpoint
        get() {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getInterceptor())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiEndpoint::class.java)
        }

}