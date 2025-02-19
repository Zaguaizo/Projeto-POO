package com.example.gaseagua.fragments.shopping

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
import com.example.gaseagua.R
import com.example.gaseagua.adapters.AddressAdapter
import com.example.gaseagua.data.Address
import com.example.gaseagua.data.CartProduct
import com.example.gaseagua.data.order.Order
import com.example.gaseagua.data.order.OrderStatus
import com.example.gaseagua.databinding.FragmentBillingBinding
import com.example.gaseagua.util.Resource
import com.example.gaseagua.viewmodel.BillingViewModel
import com.example.gaseagua.viewmodel.OrderViewModel
import com.example.kelineyt.adapters.BillingProductsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class BillingFragment: Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductsAdapter by lazy { BillingProductsAdapter() }
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f
    private var selectedAddress: Address? = null
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        products = args.products.toList()
        totalPrice = args.totalPrice
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBillingProductsRv()
        setupAddressRv()

        if(!args.payment){
            binding.apply {
                botaoFazerPedido.visibility = View.INVISIBLE
                totalBoxContainer.visibility = View.INVISIBLE
                middleLine.visibility = View.INVISIBLE
                bottomLine.visibility = View.INVISIBLE
            }
        }

        binding.imageAdicionarEndereO.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        binding.imageFechar.setOnClickListener{
            findNavController().navigateUp()
        }
        lifecycleScope.launchWhenStarted {
            billingViewModel.address.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressbarEndereO.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        addressAdapter.differ.submitList(it.data)
                        binding.progressbarEndereO.visibility = View.GONE
                    }
                    is Resource.Error ->{
                        binding.progressbarEndereO.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            orderViewModel.order.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        findNavController().navigateUp()
                        Toast.makeText(requireContext(), "Pedido feito com sucesso", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error ->{
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        billingProductsAdapter.differ.submitList(products)
        binding.tvPreOTotal.text = "R$ $totalPrice"
        addressAdapter.onClick = {
            selectedAddress = it
            if (!args.payment) {
                val b = Bundle().apply { putParcelable("address", selectedAddress) }
                findNavController().navigate(R.id.action_billingFragment_to_addressFragment, b)
            }
        }
        binding.botaoFazerPedido.setOnClickListener{
            if(selectedAddress == null){
                Toast.makeText(requireContext(), "Selecione um endereço", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showOrderConfirmation()
        }
    }

    private fun showOrderConfirmation() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Pedido")
            setMessage("Deseja fazer o pedido?")
            setNegativeButton("Continuar comprando"){ dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Prosseguir"){ dialog, _ ->
                val order = Order(
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

    private fun setupAddressRv() {
        binding.rvEndereO.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter = addressAdapter
        }
    }

    private fun setupBillingProductsRv() {
        binding.rvProdutos.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter = billingProductsAdapter
        }
    }
}