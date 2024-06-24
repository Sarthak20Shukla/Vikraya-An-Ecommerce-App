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
import androidx.recyclerview.widget.RecyclerView
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
    protected  val  offerAdapter: BestProductAdapter by lazy{ BestProductAdapter() }
    protected  val  bestProductsAdapter: BestProductAdapter by lazy{ BestProductAdapter()}
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
        binding.rvOffer.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView,dx:Int,dy:Int){
                super.onScrolled(recyclerView,dx,dy)
                if(!recyclerView.canScrollVertically(1) && dx!=0){
                    onOfferPagingRequest()
                }
            }
        })
        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{
                v,_,scrollY,_,_ ->
            if(v.getChildAt(0).bottom <= v.height+scrollY){
onBestProductsPagingRequest()            }
        })
    }
    fun showOfferLoading(){
binding.baseCategoryProgressbar.visibility=View.VISIBLE
    }
    fun hideOfferLoading(){
        binding.baseCategoryProgressbar.visibility=View.GONE

    }
    fun showBestProductsLoading(){
        binding.baseCategoryProgressbar2.visibility=View.VISIBLE
    }
    fun hideBestProductsLoading(){
        binding.baseCategoryProgressbar2.visibility=View.GONE

    }
    open fun onOfferPagingRequest(){

    }
    open fun onBestProductsPagingRequest(){

    }
    private fun setupBestProductsRv() {
        binding.rvBestProducts.apply {
            layoutManager= GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter= bestProductsAdapter
        }
    }

    private fun setupOfferRv() {
        binding.rvOffer.apply {
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter= offerAdapter
        }
    }

   }