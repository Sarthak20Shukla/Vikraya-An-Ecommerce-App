package com.example.vikraya.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vikraya.data.order.Order
import com.example.vikraya.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private  val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
):ViewModel() {

    private val _order=MutableStateFlow<Resource<Order>>(com.example.vikraya.utils.Resource.Unspecified())
    val order = _order.asStateFlow()

    fun placeOrder(order:Order){
        viewModelScope.launch {
            _order.emit(Resource.Loading())
        }
        firestore.runBatch{
            batch->
            //TODO: ADD THE ORDER
            firestore.collection("user")
                .document(auth.uid!!)
                .collection("orders")
                .document()
                .set(order)

            firestore.collection("ordeers").document().set(order)

            firestore.collection("user").document(auth.uid!!).collection("Cart").get()
                .addOnSuccessListener {
                    it.documents.forEach {
                        it.reference.delete()
                    }
                }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _order.emit(Resource.Success(order))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _order.emit(Resource.Error(it.message.toString()))
            }
        }
    }
}