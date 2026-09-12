package com.example.product_catalog

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.product_catalog.API.APIRespond
import com.example.product_catalog.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductInfo : AppCompatActivity() {

    private lateinit var productImage: ImageView
    private lateinit var productName: TextView
    private lateinit var productCategory: TextView
    private lateinit var productPrice: TextView
    private lateinit var productRating: TextView
    private lateinit var productDesc: TextView
    private lateinit var stockstatus: TextView
    private lateinit var backBtn: ImageButton

    private var respond = APIRespond()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_info)

        productImage = findViewById(R.id.productImage)
        productName = findViewById(R.id.productName)
        productCategory = findViewById(R.id.productCategory)
        productPrice = findViewById(R.id.productPrice)
        productRating = findViewById(R.id.productRating)
        productDesc = findViewById(R.id.productDesc)
        stockstatus = findViewById(R.id.stockStatus)
        backBtn = findViewById(R.id.back_Btn)

        val productId = intent.getIntExtra("PRODUCT_ID", -1)

        if (productId != -1 ) {
            loadProductDetails(productId)
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()

        }

        backBtn.setOnClickListener { view ->
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }
    }

    private fun loadProductDetails(productId: Int){
        CoroutineScope(Dispatchers.Main).launch {
            val result = respond.getProductsDetail(productId)

            result.onSuccess { product ->
                displayProduct(product)
            }

            result.onFailure { product ->
                Toast.makeText(this@ProductInfo,"Product not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayProduct(product: Product){
        productName.text = product.title
        productCategory.text = product.category
        productPrice.text = "%.2f".format(product.price)
        productRating.text = "%.2f".format(product.rating)
        productDesc.text = product.description ?: "No Description for this product"

        stockstatus.text = if (product.stock > 0) {
            "In Stock (${product.stock} available)"
        }else {
            "Out of Stock"
        }

        productImage.load(product.thumbnail){
            crossfade(true)
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.ic_dialog_alert)
        }
    }
}