package com.example.gaseagua.fragments.shopping

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
import com.example.gaseagua.data.Address
import com.example.gaseagua.databinding.FragmentAddressBinding
import com.example.gaseagua.util.Resource
import com.example.gaseagua.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class AddressFragment: Fragment() {
    private lateinit var binding: FragmentAddressBinding
    val viewModel by viewModels<AddressViewModel>()
    val args by navArgs<AddressFragmentArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest{
                when(it){
                    is Resource.Loading ->{
                        binding.progressbarEndereO.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.progressbarEndereO.visibility = View.INVISIBLE
                        findNavController().navigateUp()
                    }
                    is Resource.Error ->{
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest{
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val address = args.address
        if(address == null){
            binding.botaoDeletar.visibility = View.GONE
        }else{
            binding.apply {
                editTituloEndereO.setText(address.addressTitle)
                editNomeCompleto.setText(address.fullName)
                editEndereOCompleto.setText(address.fullAddress)
                editCelular.setText(address.phone)
                editCidade.setText(address.city)
                editEstado.setText(address.state)
            }
        }

        binding.apply {
            botaoSalvar.setOnClickListener{
                val addressTitle = editTituloEndereO.text.toString()
                val fullName = editNomeCompleto.text.toString()
                val fullAddress = editEndereOCompleto.text.toString()
                val phone = editCelular.text.toString()
                val city = editCidade.text.toString()
                val state = editEstado.text.toString()
                val address = Address(addressTitle, fullName, fullAddress, phone, city, state)

                viewModel.addAddress(address)
            }
        }
    }

}