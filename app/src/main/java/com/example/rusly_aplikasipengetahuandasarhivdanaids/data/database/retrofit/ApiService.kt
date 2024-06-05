package com.example.rusly_aplikasipengetahuandasarhivdanaids.data.database.retrofit

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private const val BASE_URL = "https://fcm.googleapis.com/"

    fun getRetrofit(): ApiConfig {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(ApiConfig::class.java)
    }

    const val BASE_URL_MYSQL = "https://aplikasi-tugas.my.id/"
    fun getRetrofitMySql(): ApiConfig {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL_MYSQL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(ApiConfig::class.java)
    }

}