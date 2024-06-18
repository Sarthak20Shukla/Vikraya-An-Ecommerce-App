package com.example.vikraya.viewModel

import android.app.DownloadManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vikraya.data.Product
import com.example.vikraya.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainCategoryViewModel @Inject constructor(

    private val firestore: FirebaseFirestore
):ViewModel() {
    private val _specialProducts=MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts:StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestDealsProducts=MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestDealsProducts:StateFlow<Resource<List<Product>>> = _bestDealsProducts

    private val _bestProducts=MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts:StateFlow<Resource<List<Product>>> = _bestProducts
    private val paginginfo = PagingInfo()
    init{
        fetchSpecialProducts()
        fetchBestDeals()
        fetchBestProducts()
    }
    fun fetchSpecialProducts() {
        viewModelScope.launch {
        _specialProducts.emit(Resource.Loading())
    }
        firestore.collection("Products").whereEqualTo("category","Special Products").get().addOnSuccessListener{result ->
            val specialProductsList = result.toObjects(Product::class.java)
            viewModelScope.launch {
                _specialProducts.emit(Resource.Success(specialProductsList))
            }
        }.addOnFailureListener{
            viewModelScope.launch {
                _specialProducts.emit(Resource.Error(it.message.toString()))
            }
        }
    }
    fun fetchBestDeals() {
        viewModelScope.launch {
            _bestDealsProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category","Best Deals").get().addOnSuccessListener{result ->
            val bestDealsProducts = result.toObjects(Product::class.java)
            viewModelScope.launch {
                _bestDealsProducts.emit(Resource.Success(bestDealsProducts))
            }
        }.addOnFailureListener{
            viewModelScope.launch {
                _bestDealsProducts.emit(Resource.Error(it.message.toString()))
            }
        }
    }
    fun fetchBestProducts() {
        if (!paginginfo.isPagingEnd)
            viewModelScope.launch {
                _bestProducts.emit(Resource.Loading())
            }
        firestore.collection("Products")./*whereEqualTo("category","Chair").orderBy("id",
            com.google.firebase.firestore.Query.Direction.ASCENDING).*/limit(paginginfo.page * 10).get()
            .addOnSuccessListener { result ->
                val bestProducts = result.toObjects(Product::class.java)
                paginginfo.isPagingEnd = bestProducts == paginginfo.oldBestProducts
                paginginfo.oldBestProducts = bestProducts
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(bestProducts))
                }
                paginginfo.page++
            }.addOnFailureListener {
            viewModelScope.launch {
                _bestProducts.emit(Resource.Error(it.message.toString()))
            }
        }
    }
    }
    internal data class PagingInfo(
        var page:Long=1,
        var oldBestProducts:List<Product> = emptyList(),
        var isPagingEnd : Boolean=false
    )