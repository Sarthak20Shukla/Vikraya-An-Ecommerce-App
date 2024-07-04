package com.example.vikraya.data.order

import android.os.Parcelable
import com.example.vikraya.data.Address
import com.example.vikraya.data.CartProduct
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong

@Parcelize
data class Order(
    val orderStatus : String="",
    val totalPrice:Float=0f,
    val products:List<CartProduct> = emptyList(),
    val address:Address= Address(),
    val date:String = SimpleDateFormat("ss-MM-yyyy", Locale.ENGLISH).format(Date()),
    val orderId: Long=nextLong(0,100_000_000_000)+totalPrice.toLong()

) : Parcelable

