package com.example.product_catalog.API

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object APIClient {
    private const val BASE_URL = "https://dummyJSON.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val productAPIService: ProductAPIService = retrofit.create(ProductAPIService::class.java)
}