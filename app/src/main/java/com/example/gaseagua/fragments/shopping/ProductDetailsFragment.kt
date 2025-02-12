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
import com.example.gaseagua.R
import com.example.gaseagua.adapters.ViewPagerToImages
import com.example.gaseagua.data.CartProduct
import com.example.gaseagua.databinding.FragmentProductDetailsBinding
import com.example.gaseagua.util.Resource
import com.example.gaseagua.util.hideBottomNavigationView
import com.example.gaseagua.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment: Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPagerToImages() }
    private val viewModel by viewModels<DetailsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        hideBottomNavigationView()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupViewpager()

        binding.imgFechar.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.buttonAdicionarAoCarrinho.setOnClickListener{
            viewModel.addUpdateProductInCart(CartProduct(product,1))
        }

        lifecycleScope.launchWhenStarted{
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        binding.buttonAdicionarAoCarrinho.setBackgroundColor(resources.getColor(R.color.teal_200))
                        Toast.makeText(requireContext(), "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error ->{
                        binding.buttonAdicionarAoCarrinho.setBackgroundColor(resources.getColor(R.color.light_red))
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.apply {
            tvNomeDoProduto.text = product.name
            tvPreODoProduto.text = "R$ ${product.price}"
            tvDescriAoDoProduto.text = product.description
        }

        viewPagerAdapter.differ.submitList(product.images)
    }

    private fun setupViewpager() {
        binding.apply {
            viewPagerImagensProdutos.adapter = viewPagerAdapter
        }
    }
}