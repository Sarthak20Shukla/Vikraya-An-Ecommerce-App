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
import androidx.recyclerview.widget.RecyclerView
import com.example.vikraya.R
import com.example.vikraya.adapters.AddressAdapter
import com.example.vikraya.adapters.BillingProductsAdapter
import com.example.vikraya.data.CartProduct
import com.example.vikraya.databinding.FragmentAddressBinding
import com.example.vikraya.databinding.FragmentBillingBinding
import com.example.vikraya.utils.HorizontalItemDecoration
import com.example.vikraya.utils.Resource
import com.example.vikraya.viewModel.BillingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BillingFragment: Fragment() {
    private  lateinit var binding: FragmentBillingBinding
    private  val addressAdapter by lazy{ AddressAdapter()}
    private val billingProductsAdapter by  lazy { BillingProductsAdapter() }
    private val viewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private  var products= emptyList<CartProduct>()
    private var totalPrice=0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        products=args.products.toList()
        totalPrice=args.totalPrice
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBillingProductsRv()
        setupAddressRv()

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.address.collectLatest {
                when(it){
                    is Resource.Error -> {
                        binding.progressbarAddress.visibility=View.GONE
                Toast.makeText(requireContext(),"Error ${it.message}",Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is Resource.Success ->{
              addressAdapter.differ.submitList(it.data)
                        binding.progressbarAddress.visibility=View.GONE

                    }
else-> Unit
                }
            }
        }
        billingProductsAdapter.differ.submitList(products)
        binding.tvTotalPrice.text="₹ $totalPrice"
    }

    private fun setupBillingProductsRv() {
binding.rvProducts.apply {
    layoutManager=LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
    adapter=billingProductsAdapter
    addItemDecoration(HorizontalItemDecoration())

}

    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter=addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }

    }

}