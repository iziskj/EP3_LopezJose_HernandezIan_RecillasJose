package com.example.ep3_lopezjose_hernandezian_recillasjose.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    val api: RecetasApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecetasApi::class.java)
    }
}