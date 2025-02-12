package com.example.gaseagua.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaseagua.R
import com.example.gaseagua.adapters.BestDealsAdapter
import com.example.gaseagua.adapters.BestProductAdapter
import com.example.gaseagua.adapters.SpecialProductsAdapter
import com.example.gaseagua.databinding.FragmentMainCategoryBinding
import com.example.gaseagua.util.Resource
import com.example.gaseagua.util.showBottomNavigationView
import com.example.gaseagua.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestDealsAdapter : BestDealsAdapter
    private lateinit var bestProductsAdapter: BestProductAdapter

    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProducts()
        setupBestDealsRv()
        setupBestProductsRv()

        specialProductsAdapter.onClick={
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
        bestProductsAdapter.onClick={
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
        bestDealsAdapter.onClick={
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.specialProducts.collectLatest {
                Log.d(TAG, "Estado do Flow: $it")
                when(it){
                    is Resource.Loading ->{
                        showLoading()
                    }
                    is Resource.Success ->{
                        Log.d(TAG, "Produtos carregados: ${it.data?.size}")
                        specialProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error ->{
                        Log.e(TAG, "Erro ao buscar produtos: ${it.message}")
                        hideLoading()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestDealsProducts.collectLatest {
                Log.d(TAG, "Estado do Flow: $it")
                when(it){
                    is Resource.Loading ->{
                        showLoading()
                    }
                    is Resource.Success ->{
                        Log.d(TAG, "Produtos carregados: ${it.data?.size}")
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error ->{
                        Log.e(TAG, "Erro ao buscar produtos: ${it.message}")
                        hideLoading()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                Log.d(TAG, "Estado do Flow: $it")
                when(it){
                    is Resource.Loading ->{
                        binding.progressbarMelhoresProdutos.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        Log.d(TAG, "Produtos carregados: ${it.data?.size}")
                        bestProductsAdapter.differ.submitList(it.data)
                        binding.progressbarMelhoresProdutos.visibility = View.GONE
                    }
                    is Resource.Error ->{
                        Log.e(TAG, "Erro ao buscar produtos: ${it.message}")
                        binding.progressbarMelhoresProdutos.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{
            v,_,scrollY,_,_ ->
            if(v.getChildAt(0).bottom <= v.height + scrollY){
                viewModel.fetchBestProducts()
            }
        })
    }

    private fun setupBestProductsRv() {
        bestProductsAdapter = BestProductAdapter()
        binding.rvMelhoresProdutos2.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL,false)
            adapter = bestProductsAdapter
        }
    }

    private fun setupBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvProdutosMelhoresPreOs.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = bestDealsAdapter
        }
    }

    private fun hideLoading() {
        binding.mainProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainProgressBar.visibility = View.VISIBLE
    }

    private fun setupSpecialProducts() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvProdutosEspeciais.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = specialProductsAdapter
        }

    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }
}