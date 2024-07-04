package com.example.vikraya.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vikraya.data.order.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllOrdersViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _allOrders = MutableStateFlow<com.example.vikraya.utils.Resource<List<Order>>>(com.example.vikraya.utils.Resource.Unspecified())
    val allOrders = _allOrders.asStateFlow()


    init {
        getAllOrders()
    }
    fun getAllOrders(){
        viewModelScope.launch {
            _allOrders.emit(com.example.vikraya.utils.Resource.Loading())
        }
        firestore.collection("user").document(auth.uid!!).collection("orders").get()
            .addOnSuccessListener {
                val orders = it.toObjects(Order::class.java)
                viewModelScope.launch {
                    _allOrders.emit(com.example.vikraya.utils.Resource.Success(orders))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _allOrders.emit(com.example.vikraya.utils.Resource.Error(it.message.toString()))
                }

            }
    }
}