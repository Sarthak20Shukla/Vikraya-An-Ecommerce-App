package com.example.vikraya.fragments.shopping

import android.app.AlertDialog
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
import com.example.vikraya.data.Address
import com.example.vikraya.data.CartProduct
import com.example.vikraya.data.order.Order
import com.example.vikraya.data.order.OrderStatus
import com.example.vikraya.databinding.FragmentAddressBinding
import com.example.vikraya.databinding.FragmentBillingBinding
import com.example.vikraya.utils.HorizontalItemDecoration
import com.example.vikraya.utils.Resource
import com.example.vikraya.viewModel.BillingViewModel
import com.example.vikraya.viewModel.OrderViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BillingFragment: Fragment() {
    private  lateinit var binding: FragmentBillingBinding
    private  val addressAdapter by lazy{ AddressAdapter()}
    private val billingProductsAdapter by  lazy { BillingProductsAdapter() }
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private  var products= emptyList<CartProduct>()
    private var totalPrice=0f
    private var selectedAddress: Address?=null
    private  val orderViewModel by viewModels<OrderViewModel>()
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
        if (!args.payment){
            binding.apply {
                buttonPlaceOrder.visibility = View.INVISIBLE
                totalBoxContainer.visibility = View.INVISIBLE
                middleLine.visibility = View.INVISIBLE
                bottomLine.visibility = View.INVISIBLE

            }
        }
        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }
        addressAdapter.onClick= {
            selectedAddress = it
            if (!args.payment) {


                val b: Bundle = Bundle().apply { putParcelable("address", selectedAddress) }
                findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
            }
        }
        lifecycleScope.launchWhenStarted {
            billingViewModel.address.collectLatest {
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

        lifecycleScope.launchWhenStarted {
            orderViewModel.order.collectLatest {
                when(it){
                    is Resource.Error -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        Toast.makeText(requireContext(),"Error ${it.message}",Toast.LENGTH_SHORT).show()

                    }
                    is Resource.Loading -> {
                        binding.buttonPlaceOrder.startAnimation()


                    }
                    is Resource.Success ->{
                        binding.buttonPlaceOrder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(requireView(),"Your order was placed",Snackbar.LENGTH_LONG).show()


                    }
                    else-> Unit
                }
            }
        }
        billingProductsAdapter.differ.submitList(products)
        binding.tvTotalPrice.text="₹ $totalPrice"
        addressAdapter.onClick={
            selectedAddress=it
        }
        binding.buttonPlaceOrder.setOnClickListener{
            if(selectedAddress == null){
                Toast.makeText(requireContext(),"Please select your address",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
showOrderConfirmationDialog()
        }
    }

    private fun showOrderConfirmationDialog() {
        val alertDialog= AlertDialog.Builder(requireContext()).apply {
            setTitle("Order items")
            setMessage("Do you want to order cart item???")
            setNegativeButton("Cancel"){
                    dialog,_->
                dialog.dismiss()
            }
            setPositiveButton("Yes"){

                    dialog,_->
                val order= Order(
                    OrderStatus.Ordered.status,
                    totalPrice,
                    products,
                    selectedAddress!!
                )
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
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