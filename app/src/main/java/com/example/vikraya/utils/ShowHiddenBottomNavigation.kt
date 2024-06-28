package com.example.vikraya.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.example.vikraya.R
import com.example.vikraya.activities.ShopingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigationView(){
    val bottomNavigationView= (activity as ShopingActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation)
    bottomNavigationView.visibility= View.GONE
}
fun Fragment.showBottomNavigationView(){
    val bottomNavigationView= (activity as ShopingActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation)
    bottomNavigationView.visibility= View.VISIBLE
}