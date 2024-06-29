package com.example.vikraya.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vikraya.R
import com.example.vikraya.activities.ShopingActivity
import com.example.vikraya.adapters.ColorsAdapter
import com.example.vikraya.adapters.SizeAdapter
import com.example.vikraya.adapters.ViewPager2Images
import com.example.vikraya.data.CartProduct
import com.example.vikraya.databinding.FragmentProductDetailsBinding
import com.example.vikraya.utils.Resource
import com.example.vikraya.utils.hideBottomNavigationView
import com.example.vikraya.viewModel.DetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment: Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding:FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy{ViewPager2Images()}
    private val sizesAdapter by lazy { SizeAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private var selectedColor: Int?= null
    private var selectedSize:String?=null
    private val viewModel by viewModels<DetailsViewModel>()

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

        sizesAdapter.onItemClick ={
            selectedSize=it
        }
        colorsAdapter.onItemClick={
            selectedColor=it
        }
        binding.buttonAddToCart.setOnClickListener{
            viewModel.addUpdateProductInCart(CartProduct(products,1,selectedColor,selectedSize))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Loading->{
                        binding.buttonAddToCart.startAnimation()

                    }
                    is Resource.Success->{
                        binding.buttonAddToCart.revertAnimation()
                    binding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                        Toast.makeText(requireContext(),"Product was Added",Toast.LENGTH_SHORT).show()

                    }
                    is Resource.Error->{
                        binding.buttonAddToCart.stopAnimation()
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()

                    }
                    else ->Unit
                }
            }
        }
binding.apply {
    tvProductName.text=products.name
    tvProductPrice.text="₹ ${products.price}"
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