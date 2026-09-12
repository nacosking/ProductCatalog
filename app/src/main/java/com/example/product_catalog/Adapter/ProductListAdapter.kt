package com.example.product_catalog.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.product_catalog.model.Product
import com.example.product_catalog.R

class ProductListAdapter(
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    private val products = mutableListOf<Product>()

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.productName)
        private val price = itemView.findViewById<TextView>(R.id.productPrice)
        private val rating = itemView.findViewById<TextView>(R.id.productRating)

        fun bind(product: Product) {
            title.text = product.title
            price.text = "%.2f".format(product.price)
            rating.text = "%.2f".format(product.rating)

            itemView.setOnClickListener {
                onProductClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item_card, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    fun submitList(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    fun addMoreProducts(newProducts: List<Product>) {
        val startPosition = products.size
        products.addAll(newProducts)
        notifyItemRangeInserted(startPosition, newProducts.size)
    }
}