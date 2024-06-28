package com.example.vikraya.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vikraya.R
import com.example.vikraya.activities.ShopingActivity
import com.example.vikraya.adapters.ColorsAdapter
import com.example.vikraya.adapters.SizeAdapter
import com.example.vikraya.adapters.ViewPager2Images
import com.example.vikraya.databinding.FragmentProductDetailsBinding
import com.example.vikraya.utils.hideBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductDetailsFragment: Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding:FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy{ViewPager2Images()}
    private val sizesAdapter by lazy { SizeAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        binding=FragmentProductDetailsBinding.inflate(inflater)
return  binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val products=args.product

        setupSizesRv()
        setupColorsRv()
        setupViewpager()
        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }
binding.apply {
    tvProductName.text=products.name
    tvProductPrice.text="$ ${products.price}"
    tvProductDescription.text=products.description
    if(products.colors.isNullOrEmpty())
        tvProductColors.visibility=View.INVISIBLE
    if(products.sizes.isNullOrEmpty())
        tvProductSize.visibility=View.INVISIBLE

}
        viewPagerAdapter.differ.submitList(products.images)
        products.colors?.let { colorsAdapter.differ.submitList(it) }
        products.sizes?.let { sizesAdapter.differ.submitList(it) }
    }

    private fun setupViewpager() {
binding.apply {
    viewPagerProductImages.adapter=viewPagerAdapter
}
    }

    private fun setupColorsRv() {
        binding.rvColors.apply {
            adapter=colorsAdapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupSizesRv() {
        binding.rvSize.apply {
            adapter=sizesAdapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }
}