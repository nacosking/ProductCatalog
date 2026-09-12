package com.example.product_catalog

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.product_catalog.API.APIRespond
import com.example.product_catalog.API.ProductAPIService
import com.example.product_catalog.Adapter.ProductListAdapter
import com.example.product_catalog.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainPage : AppCompatActivity() {

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var searchBox: EditText
    private lateinit var adapter: ProductListAdapter
    private var respond = APIRespond()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        productRecyclerView = findViewById(R.id.productRecyclerView)

        RecyclerView()
        loadProductsFromAPI()
    }

    private fun RecyclerView(){
        adapter = ProductListAdapter { product ->

        }

        productRecyclerView.apply {
            layoutManager = GridLayoutManager (this@MainPage, 1)
            adapter = this@MainPage.adapter
        }
    }

    private fun loadProductsFromAPI() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = respond.getProducts(limit = 20, skip = 0)

            result.onSuccess { response ->
                adapter.submitList(response.products)
            }

            result.onFailure { error ->

            }

        }
    }
}