package com.example.product_catalog

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.product_catalog.Adapter.ProductListAdapter

class MainPage : AppCompatActivity() {

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var searchBox: EditText
    private lateinit var adapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        productRecyclerView = findViewById(R.id.productRecyclerView)

        RecyclerView()
        loadMockData()
    }

    private fun RecyclerView(){
        adapter = ProductListAdapter { product ->

        }

        productRecyclerView.apply {
            layoutManager = GridLayoutManager (this@MainPage, 1)
            adapter = this@MainPage.adapter
        }
    }

    private fun loadMockData() {
        val mockProducts = listOf(
            Product(1, "Wireless Headphones", 59.99, "https://via.placeholder.com/200", 4.5, 100),
            Product(2, "Phone Case", 15.99, "https://via.placeholder.com/200", 4.2, 200),
            Product(3, "USB Cable", 9.99, "https://via.placeholder.com/200", 4.7, 500),
            Product(4, "Screen Protector", 12.99, "https://via.placeholder.com/200", 4.3, 300),
            Product(5, "Phone Stand", 19.99, "https://via.placeholder.com/200", 4.6, 150),
            Product(6, "Charger", 29.99, "https://via.placeholder.com/200", 4.4, 250)
        )

        adapter.submitList(mockProducts)
    }
}