package com.example.vikraya.firebase

import com.example.vikraya.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import java.lang.Exception

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth:FirebaseAuth

) {
    private val cartCollection = firestore.collection("user").document(auth.uid!!).collection("Cart")
    fun addProductToCart(cartProduct: CartProduct,onResult: (CartProduct?, Exception?)->Unit){
        cartCollection.document().set(cartProduct).addOnSuccessListener{
            onResult(cartProduct,null)
        }.addOnFailureListener {
onResult(null,it)
        }
    }

    fun increasedQuantity(documentId:String,onResult: (String?, Exception?) -> Unit){

        firestore.runTransaction {
            transition->
            val documentRef=cartCollection.document(documentId)
            val document=transition.get(documentRef)
            val productObject=document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->

                val newQuantity=cartProduct.quantity+1
                val newProductObject=cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef,newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }

    fun decreasedQuantity(documentId:String,onResult: (String?, Exception?) -> Unit){

        firestore.runTransaction {
                transition->
            val documentRef=cartCollection.document(documentId)
            val document=transition.get(documentRef)
            val productObject=document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->

                val newQuantity=cartProduct.quantity-1
                val newProductObject=cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef,newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }
    enum class QuantityChanging{
        INCREASE,DECREASE
    }
}