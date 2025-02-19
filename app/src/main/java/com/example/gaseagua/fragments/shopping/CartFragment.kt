package com.example.gaseagua.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gaseagua.R
import com.example.gaseagua.adapters.CartProductAdapter
import com.example.gaseagua.databinding.CartProductItemBinding
import com.example.gaseagua.databinding.FragmentCartBinding
import com.example.gaseagua.firebase.FirebaseCommon
import com.example.gaseagua.util.Resource
import com.example.gaseagua.util.VerticalItemDecoration
import com.example.gaseagua.viewmodel.CartViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

class CartFragment: Fragment(R.layout.fragment_cart) {

    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartProductAdapter() }
    private val viewModel by activityViewModels<CartViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartRv()


        var totalPrice = 0f
        lifecycleScope.launchWhenStarted {
            viewModel.productsPrice.collectLatest { price ->
                price?.let{
                    totalPrice = it
                    binding.tvPreOTotal.text = "R$ $price"
                }
            }
        }


        cartAdapter.onProductClick = {
            val b = Bundle().apply { putParcelable("product",it.product) }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment,b)
        }

        cartAdapter.onPlusClick = {
            viewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.INCREASE)
        }

        cartAdapter.onMinusClick = {
            viewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.DECREASE)
        }

        binding.botaoCheckout.setOnClickListener{
            val action = CartFragmentDirections.actionCartFragmentToBillingFragment(totalPrice,cartAdapter.differ.currentList.toTypedArray(),true)
            findNavController().navigate(action)
        }

        binding.imageFecharCarrinho.setOnClickListener{
            findNavController().navigateUp()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.deleteDialog.collectLatest {
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Apagar item do carrinho")
                    setMessage("Tem certeza que deseja apagar este item do carrinho?")
                    setNegativeButton("Cancelar"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Apagar"){ dialog, _ ->
                        viewModel.deleteCartProduct(it)
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest{
                when(it){
                    is Resource.Success ->{
                        binding.progressbarCarrinho.visibility = View.INVISIBLE
                        if(it.data!!.isEmpty()){
                            showEmptyCart()
                            hideOtherViews()
                        }else{
                            hideEmptyCart()
                            showOtherViews()
                            cartAdapter.differ.submitList(it.data)
                        }
                    }
                    is Resource.Loading ->{
                        binding.progressbarCarrinho.visibility = View.VISIBLE
                    }
                    is Resource.Error ->{
                        binding.progressbarCarrinho.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showOtherViews() {
        binding.apply {
            rvCarrinho.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            botaoCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideOtherViews() {
        binding.apply {
            rvCarrinho.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            botaoCheckout.visibility = View.GONE
        }
    }

    private fun hideEmptyCart() {
       binding.apply {
           clCarrinhoVazio.visibility = View.GONE
       }
    }

    private fun showEmptyCart() {
        binding.apply {
            clCarrinhoVazio.visibility = View.VISIBLE
        }
    }

    private fun setupCartRv(){
        binding.rvCarrinho.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }
}