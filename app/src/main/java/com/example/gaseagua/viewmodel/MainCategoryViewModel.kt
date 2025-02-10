package com.example.gaseagua.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gaseagua.data.Product
import com.example.gaseagua.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Initial())
    val specialProducts: StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestDealsProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Initial())
    val bestDealsProducts: StateFlow<Resource<List<Product>>> = _bestDealsProducts

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Initial())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts


    private val pagingInfo = PagingInfo()

    init {
        fetchSpecialProducts()
        fetchBestDeals()
        fetchBestProducts()
    }

    fun fetchSpecialProducts(){
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())

            firestore.
            collection("Produtos")
            .whereEqualTo("category","Special Products").get().addOnSuccessListener { result ->
                val specialProductsList = result.toObjects(Product:: class.java)
                Log.d("Firestore", "Produtos encontrados: ${specialProductsList.size}")
                specialProductsList.forEach {
                    Log.d("Firestore", "Produto: ${it.name}, Categoria: ${it.category}")
                }
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                }
            }
        }
    }

    fun fetchBestDeals(){
        viewModelScope.launch {
            _bestDealsProducts.emit(Resource.Loading())
            firestore.collection("Produtos")
            .whereEqualTo("category", "Best Deals").get()
            .addOnSuccessListener {
                    result ->
                val bestDealsProductsList = result.toObjects(Product:: class.java)
                Log.d("Firestore", "Produtos encontrados: ${bestDealsProductsList.size}")
                bestDealsProductsList.forEach {
                    Log.d("Firestore", "Produto: ${it.name}, Categoria: ${it.category}")
                }
                viewModelScope.launch {
                    _bestDealsProducts.emit(Resource.Success(bestDealsProductsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestDealsProducts.emit(Resource.Error(it.message.toString()))
                }
            }
        }
    }

    fun fetchBestProducts(){
        if(!pagingInfo.isPagingEnd){
            viewModelScope.launch {
                _bestProducts.emit(Resource.Loading())
                firestore.collection("Produtos").limit(pagingInfo.bestProductsPage * 10).get()
                .addOnSuccessListener {
                    result ->
                    val bestProductsList = result.toObjects(Product:: class.java)
                    pagingInfo.isPagingEnd = bestProductsList == pagingInfo.oldBestProduct
                    pagingInfo.oldBestProduct = bestProductsList
                    Log.d("Firestore", "Produtos encontrados: ${bestProductsList.size}")
                    bestProductsList.forEach {
                    Log.d("Firestore", "Produto: ${it.name}, Categoria: ${it.category}")
                    }
                    pagingInfo.bestProductsPage++
                    viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(bestProductsList))
                    }
                }.addOnFailureListener {
                        viewModelScope.launch {
                        _bestProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
            }
        }
    }
}
internal data class PagingInfo(
    var bestProductsPage: Long = 1,
    var oldBestProduct: List<Product> = emptyList(),
    var isPagingEnd: Boolean = false
)