package com.example.vikraya.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vikraya.R
import com.example.vikraya.adapters.CartProductsAdapter
import com.example.vikraya.databinding.FragmentCartBinding
import com.example.vikraya.firebase.FirebaseCommon
import com.example.vikraya.utils.Resource
import com.example.vikraya.utils.VerticalItemDecoration
import com.example.vikraya.viewModel.CartViewModel
import kotlinx.coroutines.flow.collectLatest

class CartFragment: Fragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartProductsAdapter()}
    private  val viewModel by activityViewModels<CartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCartRv()
        var totalPrice = 0f
        lifecycleScope.launchWhenStarted {
            viewModel.productsPrice.collectLatest { price ->
                price?.let {
                    totalPrice=it
                    binding.tvTotalprice.text="₹ $price"
                }
            }
        }
        binding.imgCloseCart.setOnClickListener {
            findNavController().navigateUp()
        }
        cartAdapter.onProductClick={
            val b=Bundle().apply { putParcelable("product",it.product)}
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment,b)
        }

        cartAdapter.onPlusClick={
            viewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.INCREASE)

        }
        cartAdapter.onMinusClick={
            viewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.DECREASE)
        }

        binding.btnCheckout.setOnClickListener {
            val action=CartFragmentDirections.actionCartFragmentToBillingFragment(totalPrice,cartAdapter.differ.currentList.toTypedArray())
            findNavController().navigate(action)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteDialog.collectLatest {
                val alertDialog=AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item from cart")
                    setMessage("Do you want to delete this item???")
                    setNegativeButton("Cancel"){
                        dialog,_->
                        dialog.dismiss()
                    }
                    setPositiveButton("Yes"){
                        dialog,_->
                    viewModel.deleteCartProduct(it)
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Resource.Error -> {
binding.progressBar.visibility=View.INVISIBLE
Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility=View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility=View.INVISIBLE
                        if(it.data!!.isEmpty()){
                            showEmptyCart()
                            hideOtherView()
                        } else{
                            hideEmptyCart()
                            showOtherViews()
                            cartAdapter.differ.submitList(it.data)
                        }
                    }
                    else ->  Unit
                }
            }
        }
    }

    private fun showOtherViews() {
        binding.apply {
            rvCart.visibility=View.VISIBLE
            btnCheckout.visibility=View.VISIBLE
            linear.visibility=View.VISIBLE


        }
    }

    private fun hideOtherView() {
        binding.apply {
            rvCart.visibility=View.GONE
            btnCheckout.visibility=View.GONE
            linear.visibility=View.GONE

        }    }

    private fun hideEmptyCart() {
binding.apply {
    layoutCardEmpty.visibility=View.GONE
}    }

    private fun showEmptyCart() {
        binding.apply {
            layoutCardEmpty.visibility=View.VISIBLE
        }     }


    private fun setupCartRv() {
        binding.rvCart.apply {
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter= cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }
}