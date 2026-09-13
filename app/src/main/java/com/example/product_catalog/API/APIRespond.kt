package com.example.product_catalog.API


import com.example.product_catalog.model.Product
import com.example.product_catalog.model.ProductsInAPI

class APIRespond {
    private val apiService = APIClient.productAPIService

    suspend fun getProducts(limit: Int = 20, skip: Int = 0): Result<ProductsInAPI>{
        return try {
            val response = apiService.getProducts(limit, skip)
            Result.success(response)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun  getProductsDetail(productId: Int): Result<Product>{
        return try {
            val product = apiService.getProductsDetails(productId)
            Result.success(product)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun searchProducts(query: String): Result<ProductsInAPI>{
        return try {
            val response = apiService.searchProduct(query)
            Result.success(response)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}