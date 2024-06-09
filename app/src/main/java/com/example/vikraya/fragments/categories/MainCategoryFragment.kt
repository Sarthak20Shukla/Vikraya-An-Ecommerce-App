package com.example.vikraya.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.Resource
import com.example.vikraya.R
import com.example.vikraya.adapters.SpecialProductsAdapter
import com.example.vikraya.databinding.FragmentMainCategoryBinding
import com.example.vikraya.dialog.setupBottomSheetDialog
import com.example.vikraya.viewModel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG="MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment :Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var  specialProductsAdapter: SpecialProductsAdapter
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

}