package com.example.vikraya.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vikraya.R
import com.example.vikraya.adapters.HomeViewPagerAdapter
import com.example.vikraya.databinding.FragmentHomeBinding
import com.example.vikraya.fragments.categories.AccessoryFragment
import com.example.vikraya.fragments.categories.ChairFragment
import com.example.vikraya.fragments.categories.CupboardFragment
import com.example.vikraya.fragments.categories.FurnitureFragment
import com.example.vikraya.fragments.categories.MainCategoryFragment
import com.example.vikraya.fragments.categories.TableFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding:FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment= arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            FurnitureFragment(),
            AccessoryFragment(),
            TableFragment(),
        )

        binding.viewpagerHome.isUserInputEnabled=false

        val viewPager2Adapter= HomeViewPagerAdapter(categoriesFragment,childFragmentManager,lifecycle)
        binding.viewpagerHome.adapter=viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewpagerHome){ tab,position->
            when(position){
                0-> tab.text="Main"
                1-> tab.text="Chair"
                2-> tab.text="Cupboard"
                3-> tab.text="Furniture"
                4-> tab.text="Accessory"
                5-> tab.text="Table"
            }
        }.attach()
    }
}