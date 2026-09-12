package com.example.product_catalog.model

data class ProductsInAPI(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)