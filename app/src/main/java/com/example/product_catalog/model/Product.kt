package com.example.product_catalog.model

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val thumbnail: String,
    val rating: Double,
    val stock: Int,
    val category: String,
    val description: String,
    val availabilityStatus: String ?= null
)