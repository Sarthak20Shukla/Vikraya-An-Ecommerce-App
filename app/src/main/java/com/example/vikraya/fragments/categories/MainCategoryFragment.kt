package com.example.vikraya.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.Resource
import com.example.vikraya.R
import com.example.vikraya.adapters.BestDealsAdapter
import com.example.vikraya.adapters.BestProductAdapter
import com.example.vikraya.adapters.SpecialProductsAdapter
import com.example.vikraya.databinding.FragmentMainCategoryBinding
import com.example.vikraya.dialog.setupBottomSheetDialog
import com.example.vikraya.utils.showBottomNavigationView
import com.example.vikraya.viewModel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG="MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment :Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var  specialProductsAdapter: SpecialProductsAdapter
    private lateinit var  bestDealsAdapter: BestDealsAdapter
    private lateinit var  bestProductsAdapter: BestProductAdapter

    private val viewModel by viewModels<MainCategoryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMainCategoryBinding.inflate(inflater)
        return binding.root;
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpecialProductsRv()
        setupBestDealsRv()
        setupBestProductsRv()
        specialProductsAdapter.onClick = {
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
        bestDealsAdapter.onClick = {
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
        bestProductsAdapter.onClick = {
            val b=Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.specialProducts.collectLatest {
                when(it){

                    is com.example.vikraya.utils.Resource.Error ->  {
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is com.example.vikraya.utils.Resource.Loading -> {
                        showLoading()
                    }
                    is com.example.vikraya.utils.Resource.Success -> {
                        specialProductsAdapter.differ.submitList(it.data)
                         hideLoading()
                    }
                    else ->Unit

                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.bestDealsProducts.collectLatest {
                when(it){

                    is com.example.vikraya.utils.Resource.Error ->  {
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is com.example.vikraya.utils.Resource.Loading -> {
                        showLoading()
                    }
                    is com.example.vikraya.utils.Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    else ->Unit

                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when(it){

                    is com.example.vikraya.utils.Resource.Error ->  {
                        binding.bestProductsProgressbar.visibility = View.GONE

                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is com.example.vikraya.utils.Resource.Loading -> {
                        binding.bestProductsProgressbar.visibility = View.VISIBLE
                    }
                    is com.example.vikraya.utils.Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        binding.bestProductsProgressbar.visibility = View.GONE

                    }
                    else ->Unit

                }
            }
        }
        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{
            v,_,scrollY,_,_ ->
            if(v.getChildAt(0).bottom <= v.height+scrollY){
                viewModel.fetchBestProducts()
            }
        })
    }

    private fun setupBestProductsRv() {
        bestProductsAdapter= BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager= GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter= bestProductsAdapter
        }
    }

    private fun setupBestDealsRv() {
        bestDealsAdapter= BestDealsAdapter()
        binding.rvBestDealProducts.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter= bestDealsAdapter
       }
    }

    private fun showLoading() {
        binding.mainCategoryProgressbar.visibility=View.VISIBLE

    }
    private fun hideLoading() {
        binding.mainCategoryProgressbar.visibility=View.GONE
    }
    private fun setupSpecialProductsRv() {
        specialProductsAdapter= SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter= specialProductsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

}