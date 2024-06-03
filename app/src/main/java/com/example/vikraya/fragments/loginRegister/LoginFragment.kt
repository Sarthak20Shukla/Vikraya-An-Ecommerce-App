package com.example.vikraya.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vikraya.R
import com.example.vikraya.activities.ShopingActivity
import com.example.vikraya.databinding.FragmentLoginBinding
import com.example.vikraya.dialog.setupBottomSheetDialog
import com.example.vikraya.utils.Resource
import com.example.vikraya.viewModel.LoginViewModel
import com.example.vikraya.viewModel.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.donthaveanaccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.apply {
            buttonLoginLogin.setOnClickListener {
                val email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.toString()
                viewModel.login(email, password)

            }
        }
            binding.tvForgotPasswordLogin.setOnClickListener {
               // findNavController().navigate(R.id.action_loginFragment_to_reset_password_dialog)

                setupBottomSheetDialog { email ->
                    viewModel.resetPassword(email)


                }
            }
            lifecycleScope.launchWhenStarted {
                viewModel.resetPassword.collect {
                    when (it) {
                        is Resource.Error -> {
                            Snackbar.make(
                                requireView(),
                                "Error: ${it.message}",
                                Snackbar.LENGTH_LONG
                            ).show()

                        }

                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            Snackbar.make(
                                requireView(),
                                "Reset link was sent to your email",
                                Snackbar.LENGTH_LONG
                            ).show()


                        }

                        else -> Unit
                    }
                }
            }

            lifecycleScope.launchWhenStarted {
                viewModel.login.collect {
                    when (it) {

                        is Resource.Loading -> {
                            binding.buttonLoginLogin.startAnimation()
                        }

                        is Resource.Success -> {
                            binding.buttonLoginLogin.revertAnimation()
                            Intent(requireActivity(), ShopingActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            binding.buttonLoginLogin.revertAnimation()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

