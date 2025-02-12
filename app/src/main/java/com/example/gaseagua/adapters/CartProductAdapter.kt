package com.example.gaseagua.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gaseagua.data.CartProduct
import com.example.gaseagua.data.Product
import com.example.gaseagua.databinding.CartProductItemBinding
import com.example.gaseagua.databinding.SpecialRvItemBinding
import com.example.gaseagua.helper.getProductPrice

class CartProductAdapter : RecyclerView.Adapter<CartProductAdapter.CartProductsViewHolder>() {

    inner class CartProductsViewHolder(val binding:  CartProductItemBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(cartProduct: CartProduct){
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(imagemProdutoDoCarrinho)
                tvNomeDoProdutoDoCarrinho.text = cartProduct.product.name
                tvQuantidadeProduto.text = cartProduct.quantity.toString()
                val priceAfterOffer = cartProduct.product.offer.getProductPrice(cartProduct.product.price)
                tvPreODoProdutoDoCarrinho.text =  "R$ ${String.format("%.2f",priceAfterOffer)}"
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductsViewHolder {
        return CartProductsViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CartProductsViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)

        holder.itemView.setOnClickListener{
            onProductClick?.invoke(cartProduct)
        }
        holder.binding.plusIcon.setOnClickListener{
            onPlusClick?.invoke(cartProduct)
        }
        holder.binding.minusIcon.setOnClickListener{
            onMinusClick?.invoke(cartProduct)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onProductClick:((CartProduct) -> Unit) ? = null
    var onPlusClick:((CartProduct) -> Unit) ? = null
    var onMinusClick:((CartProduct) -> Unit) ? = null


}