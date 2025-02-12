package com.example.gaseagua.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.gaseagua.data.Product
import com.example.gaseagua.databinding.BestDealsRvItemBinding
import com.example.gaseagua.databinding.ProductRvItemBinding
import com.example.gaseagua.helper.getProductPrice

class BestProductAdapter: RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>() {
    inner class BestProductViewHolder(private val binding: ProductRvItemBinding): ViewHolder(binding.root){
        fun bind(product: Product) {
            binding.apply {

                val priceAfterOffer = product.offer.getProductPrice(product.price)
                tvNovoPreO.text = "R$ ${String.format("%.2f",priceAfterOffer)}"
                tvPreO.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

                if(product.offer == null)
                   tvNovoPreO.visibility = View.INVISIBLE

                Glide.with(itemView).load(product.images[0]).into(imagemProduto)
                tvPreO.text = "R$ ${product.price}"
                tvNome2.text = product.name
            }
        }
    }



    private val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        return BestProductViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick:((Product) -> Unit) ? = null

}