package com.example.product_catalog.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.product_catalog.API.APIRespond
import com.example.product_catalog.Adapter.ProductListAdapter
import com.example.product_catalog.R
import com.example.product_catalog.utils.SearchDebouncer
import com.example.product_catalog.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.product_catalog.model.Product

class MainPage : AppCompatActivity() {

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var loadMore: ProgressBar
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorContainer: LinearLayout
    private lateinit var emptyContainer: LinearLayout
    private lateinit var errorMessage: TextView
    private lateinit var retryButton: Button

    private lateinit var adapter: ProductListAdapter
    private var respond = APIRespond()
    private lateinit var searchDebouncer: SearchDebouncer


    private var currentSkip = 0
    private val pageLimit = 20
    private var isLoading = false
    private var isSearching = false
    private var hasMoreProducts = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        productRecyclerView = findViewById(R.id.productRecyclerView)
        searchView = findViewById(R.id.searchBox)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        loadMore = findViewById(R.id.loadMoreProgressBar)
        errorContainer = findViewById(R.id.errorContainer)
        emptyContainer = findViewById(R.id.emptyContainer)
        errorMessage = findViewById(R.id.errorMessage)
        retryButton = findViewById(R.id.retryButton)

        searchDebouncer = SearchDebouncer(300, CoroutineScope(Dispatchers.Main))

        RecyclerView()
        loadProductsFromAPI()
        searchBox()
    }

    private fun RecyclerView(){
        adapter = ProductListAdapter { product ->
            val intent = Intent(this, ProductInfo::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            Log.d("Product ID", product.id.toString())
            startActivity(intent)
        }

        productRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MainPage, 2)
            adapter = this@MainPage.adapter

            addOnScrollListener(PaginationScroll())
        }
    }

    private fun searchBox(){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(searchedProducts: String?): Boolean {
                if (searchedProducts != null) {
                    searchDebouncer.debounce {
                        if (searchedProducts.isEmpty()){
                            isSearching = false
                            currentSkip = 0
                            loadProductsFromAPI()
                        } else {
                            isSearching = true
                            currentSkip = 0
                            performSearch(searchedProducts)
                        }
                    }
                }
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        isLoading = true
        updateState(UiState.Loading<List<Product>>())

        CoroutineScope(Dispatchers.Main).launch {
            val result = respond.searchProducts(query)

            result.onSuccess { response ->
                if (response.products.isEmpty()) {
                    updateState(UiState.Empty<List<Product>>())
                } else {
                    adapter.submitList(response.products)
                    updateState(UiState.Success<List<Product>>(response.products))
                }
                isLoading = false
            }

            result.onFailure { error ->
                updateState(UiState.Error<List<Product>>(error.message ?: "Search failed"))  // ← Use UiState directly
                isLoading = false
            }
        }
    }

    private fun updateState(state: UiState<*>) {
        when (state) {
            is UiState.Loading -> {
                productRecyclerView.visibility = View.GONE
                loadingProgressBar.visibility = View.VISIBLE
                errorContainer.visibility = View.GONE
                emptyContainer.visibility = View.GONE
            }

            is UiState.Success -> {
                productRecyclerView.visibility = View.VISIBLE
                loadingProgressBar.visibility = View.GONE
                errorContainer.visibility = View.GONE
                emptyContainer.visibility = View.GONE
            }

            is UiState.Error -> {
                productRecyclerView.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
                errorContainer.visibility = View.VISIBLE
                emptyContainer.visibility = View.GONE
                errorMessage.text = state.message
            }

            is UiState.Empty -> {
                productRecyclerView.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
                errorContainer.visibility = View.GONE
                emptyContainer.visibility = View.VISIBLE
            }
        }
    }

    private fun loadProductsFromAPI() {
        if (isLoading || !hasMoreProducts) return

        isLoading = true
        if (currentSkip == 0) {
            updateState(UiState.Loading<List<Product>>())
        }

        CoroutineScope(Dispatchers.Main).launch {
            val result = respond.getProducts(limit = pageLimit, skip = currentSkip)

            result.onSuccess { response ->
                if (currentSkip == 0) {
                    if (response.products.isEmpty()) {
                        updateState(UiState.Empty<List<Product>>())
                    } else {
                        adapter.submitList(response.products)
                        updateState(UiState.Success<List<Product>>(response.products))
                    }
                } else {
                    adapter.addMoreProducts(response.products)
                    loadMore.visibility = View.GONE
                }

                currentSkip += pageLimit
                hasMoreProducts = response.products.isNotEmpty()
                isLoading = false
            }

            result.onFailure { error ->
                isLoading = false
                loadMore.visibility = View.GONE
                if (currentSkip == 0) {
                    updateState(UiState.Error<List<Product>>(error.message ?: "Unknown error"))
                }
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