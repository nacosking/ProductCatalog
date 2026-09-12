package com.example.product_catalog.API

import com.example.product_catalog.model.ProductsInAPI
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductAPIService {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): ProductsInAPI
}