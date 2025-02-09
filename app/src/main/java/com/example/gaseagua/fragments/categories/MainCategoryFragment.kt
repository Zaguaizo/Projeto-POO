package com.example.gaseagua.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaseagua.R
import com.example.gaseagua.adapters.SpecialProductsAdapter
import com.example.gaseagua.databinding.FragmentMainCategoryBinding
import com.example.gaseagua.util.Resource
import com.example.gaseagua.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
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
    }

    private fun hideLoading() {
        binding.mainProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainProgressBar.visibility = View.VISIBLE
    }

    private fun setupSpecialProducts() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = specialProductsAdapter
        }

    }
}