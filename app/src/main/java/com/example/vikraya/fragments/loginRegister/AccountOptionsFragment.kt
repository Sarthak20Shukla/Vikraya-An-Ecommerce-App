package com.example.vikraya.fragments.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vikraya.R
import com.example.vikraya.databinding.FragmentAccountOptionsBinding
import com.example.vikraya.databinding.FragmentIntroductionBinding

class AccountOptionsFragment: Fragment(R.layout.fragment_account_options) {
    private lateinit var binding: FragmentAccountOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* lifecycleScope.launchWhenStarted {
             viewModel.navigate.collect {
                 when (it) {
                      SHOPPING_ACTIVITY -> {
                         Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                             startActivity(intent)
                         }
                     }

                     ACCOUNT_OPTIONS_FRAGMENT -> {
                         findNavController().navigate(it)
                     }

                     else -> Unit
                 }
             }
         }*/

        binding.registerbutton.setOnClickListener {
            findNavController().navigate(R.id.action_accountOptionsFragment_to_registerFragment)
        }
        binding.loginbutton.setOnClickListener {
            findNavController().navigate(R.id.action_accountOptionsFragment_to_loginFragment2   )
        }
    }
}