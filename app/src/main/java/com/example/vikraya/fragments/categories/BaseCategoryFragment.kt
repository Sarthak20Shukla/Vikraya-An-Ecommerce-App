package com.example.vikraya.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vikraya.R
import com.example.vikraya.adapters.BestDealsAdapter
import com.example.vikraya.adapters.BestProductAdapter
import com.example.vikraya.adapters.SpecialProductsAdapter
import com.example.vikraya.databinding.FragmentBaseCategoryBinding
import com.example.vikraya.databinding.FragmentMainCategoryBinding
import com.example.vikraya.viewModel.MainCategoryViewModel
import kotlinx.coroutines.flow.collectLatest

open class BaseCategoryFragment: Fragment(R.layout.fragment_base_category) {
    private lateinit var binding: FragmentBaseCategoryBinding
    private lateinit var  offerAdapter: BestProductAdapter
    private lateinit var  bestProductsAdapter: BestProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root;
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOfferRv()
        setupBestProductsRv()

    }

    private fun setupBestProductsRv() {
        bestProductsAdapter= BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager= GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter= bestProductsAdapter
        }
    }

    private fun setupOfferRv() {
        offerAdapter= BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter= offerAdapter
        }
    }

   }