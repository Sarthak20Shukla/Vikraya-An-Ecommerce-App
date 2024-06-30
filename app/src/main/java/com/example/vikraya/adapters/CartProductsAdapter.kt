package com.example.vikraya.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vikraya.data.CartProduct
import com.example.vikraya.data.Product
import com.example.vikraya.databinding.CartProductItemBinding
import com.example.vikraya.databinding.SpecialRvItemBinding
import com.example.vikraya.helpers.getProductPrice

class CartProductsAdapter: RecyclerView.Adapter<CartProductsAdapter.CartProductsViewHolder>() {
    inner class CartProductsViewHolder( val binding: CartProductItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(imgCartProduct)
                tvCartProductName.text=cartProduct.product.name
                tvQuantity.text=cartProduct.quantity.toString()

                val priceAfterPercentage=cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                tvProductCartPrice.text="₹ ${String.format("%.2f",priceAfterPercentage)}"
                imgColor.setImageDrawable(ColorDrawable(cartProduct.selectedColor?: Color.TRANSPARENT))
                tvCartSize.text=cartProduct.selectedSize?:"".also {
                    imgSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT))

                }
            }
        }
    }
    private val diffCallback=object :DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id==newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductsViewHolder {
        return CartProductsViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartProductsViewHolder, position: Int) {
        val cartProduct =differ.currentList[position]
        holder.bind(cartProduct)
        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cartProduct)
        }
        holder.binding.imgPlus.setOnClickListener {
            onPlusClick?.invoke(cartProduct)
        }
        holder.binding.imgMinus.setOnClickListener {
            onMinusClick?.invoke(cartProduct)
        }
    }
    var onProductClick:((CartProduct)->Unit)? =null
    var onPlusClick:((CartProduct)->Unit)? =null
    var onMinusClick:((CartProduct)->Unit)? =null



}