package com.example.gaseagua.data

sealed class Category(val category: String) {
    object Agua: Category("Agua")
    object Gas: Category("Gas")
}