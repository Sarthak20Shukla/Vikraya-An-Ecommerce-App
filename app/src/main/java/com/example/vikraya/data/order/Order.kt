package com.example.vikraya.data.order

import com.example.vikraya.data.Address
import com.example.vikraya.data.CartProduct

data class Order(
    val orderStatus : String,
    val totalPrice:Float,
    val products:List<CartProduct>,
    val address:Address
)
