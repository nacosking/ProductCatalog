package com.example.product_catalog

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainPage : AppCompatActivity() {

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var searchBox: EditText
    private lateinit var adapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        productRecyclerView = findViewById(R.id.productRecyclerView)


    }

    private fun RecyclerView(){

    }
}