package com.example.gaseagua.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.gaseagua.R
import com.example.gaseagua.data.User
import com.example.gaseagua.databinding.FragmentRegisterBinding
import com.example.gaseagua.util.RegisterValidation
import com.example.gaseagua.util.Resource
import com.example.gaseagua.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment: Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            btnRegistrar.setOnClickListener {
                val user = User(
                    editTextNome.text.toString().trim(),
                    editTextSobrenome.text.toString().trim(),
                    editTextEmail.text.toString().trim()
                )
                val password = editTextSenha.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        Log.d("test",it.data.toString())
                    }
                    is Resource.Error ->{
                        Log.e(TAG,it.message.toString())
                    }
                    else -> Unit
                }
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect{ validation ->
                if(validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.editTextEmail.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.editTextSenha.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
                if(validation.nome is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.editTextNome.apply {
                            requestFocus()
                            error = validation.nome.message
                        }
                    }
                }
                if(validation.sobrenome is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.editTextSobrenome.apply {
                            requestFocus()
                            error = validation.sobrenome.message
                        }
                    }
                }
            }
        }
    }


}