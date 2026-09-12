package com.example.product_catalog

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
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
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var searchBox: EditText
    private lateinit var adapter: ProductListAdapter
    private var respond = APIRespond()

    private var currentSkip = 0
    private val pageLimit = 20
    private var isLoading = false
    private var hasMoreProducts = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        productRecyclerView = findViewById(R.id.productRecyclerView)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)

        RecyclerView()
        loadProductsFromAPI()
    }

    private fun RecyclerView(){
        adapter = ProductListAdapter { product ->

        }

        productRecyclerView.apply {
            layoutManager = GridLayoutManager (this@MainPage, 2)
            adapter = this@MainPage.adapter

            addOnScrollListener(PaginationScroll())
        }
    }

    private fun loadProductsFromAPI() {
        if (isLoading || !hasMoreProducts) return

        isLoading = true
        loadingProgressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.Main).launch {
            val result = respond.getProducts(limit = pageLimit, skip = currentSkip)

            result.onSuccess { response ->
                if (currentSkip == 0 ) {
                    adapter.submitList(response.products)
                } else {
                    adapter.addMoreProducts(response.products)
                }

                currentSkip += pageLimit
                hasMoreProducts = response.products.isNotEmpty()
                isLoading = false
                loadingProgressBar.visibility = View.GONE
            }

            result.onFailure { error ->
                isLoading = false
                loadingProgressBar.visibility = View.GONE
                println("Error: ${error.message}")
            }

        }
    }

    private inner class PaginationScroll : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int){
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val visibileItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPositions = layoutManager.findFirstVisibleItemPosition()

            if (visibileItemCount + firstVisibleItemPositions >= totalItemCount
                && firstVisibleItemPositions >= 0
                && !isLoading) {

                loadProductsFromAPI()

            }
        }
    }
}