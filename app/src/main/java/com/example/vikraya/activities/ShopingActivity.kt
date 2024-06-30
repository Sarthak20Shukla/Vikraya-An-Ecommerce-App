package com.example.vikraya.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.load.engine.Resource
import com.example.vikraya.R
import com.example.vikraya.databinding.ActivityShopingBinding
import com.example.vikraya.viewModel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShopingActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityShopingBinding.inflate(layoutInflater)
    }
    val viewModel by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navController= findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is com.example.vikraya.utils.Resource.Success->{
                        val count=it.data?.size?:0
                        val bottomNavigation=findViewById<BottomNavigationView>(R.id.bottom_navigation)
                        bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                            number=count
                            backgroundColor=resources.getColor(R.color.g_blue)
                        }
                    }

                    else -> Unit
                }
            }
        }
    }
}