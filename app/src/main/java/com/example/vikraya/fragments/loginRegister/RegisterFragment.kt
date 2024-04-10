package com.example.vikraya.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.vikraya.R
import com.example.vikraya.data.User
import com.example.vikraya.databinding.FragmentRegisterBinding
import com.example.vikraya.utils.Resource
import com.example.vikraya.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

private val TAG="RegisterFragment"
@AndroidEntryPoint
class RegisterFragment: Fragment() {
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var binding:FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            buttonRegisterRegister.setOnClickListener {
                val user= User(
                    edFirstname.text.toString().trim(),
                    edLastname.text.toString().trim(),
                    edEmailRegister.text.toString().trim(),
                )
                val password=edPasswordRegister.text.toString()
                viewModel.createAccountwithEmailandPassword(user,password)

            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.buttonRegisterRegister.startAnimation()

                    }
                    is Resource.Success -> {
                        Log.d("test",it.data.toString())
                        binding.buttonRegisterRegister.revertAnimation()

                    }
                    is Resource.Error -> {
                        Log.e(TAG,it.message.toString())
                        binding.buttonRegisterRegister.revertAnimation()
                    }



                    is Resource.Unspecified -> Unit
                }
            }
        }
    }
}