package com.example.gaseagua.util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.gaseagua.R
import com.example.gaseagua.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigationView() {
    val bottomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        com.example.gaseagua.R.id.bottomNavigation)
    bottomNavigationView.visibility = android.view.View.GONE
}

fun Fragment.showBottomNavigationView() {
    val bottomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        com.example.gaseagua.R.id.bottomNavigation)
    bottomNavigationView.visibility = android.view.View.VISIBLE
}