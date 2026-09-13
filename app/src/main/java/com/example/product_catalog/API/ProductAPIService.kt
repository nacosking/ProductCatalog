package com.example.product_catalog.API

import com.example.product_catalog.model.Product
import com.example.product_catalog.model.ProductsInAPI
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductAPIService {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): ProductsInAPI

    @GET("products/{id}")
    suspend fun getProductsDetails(
        @Path("id") productId: Int
    ): Product

    @GET("products/search")
    suspend fun  searchProduct(
        @Query("q") query: String
    ): ProductsInAPI
}