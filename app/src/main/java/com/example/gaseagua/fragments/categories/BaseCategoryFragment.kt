package com.example.gaseagua.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gaseagua.R
import com.example.gaseagua.adapters.BestProductAdapter
import com.example.gaseagua.databinding.FragmentBaseCategoryBinding
import com.example.gaseagua.util.showBottomNavigationView

open class BaseCategoryFragment: Fragment(R.layout.fragment_base_category) {
    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: BestProductAdapter by lazy { BestProductAdapter() }
    protected val bestProductsAdapter: BestProductAdapter by lazy { BestProductAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOfferRv()
        setupBestProductsRv()

        offerAdapter.onClick={
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
        bestProductsAdapter.onClick={
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        binding.rvOferta.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!recyclerView.canScrollVertically(1) && dx != 0){
                    onOfferPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{
                v,_,scrollY,_,_ ->
            if(v.getChildAt(0).bottom <= v.height + scrollY){
                onBestProductsPagingRequest()
            }
        })
    }

    fun showOfferLoading(){
        binding.progressBarProdutosEmOferta.visibility = View.VISIBLE
    }

    fun hideOfferLoading(){
        binding.progressBarProdutosEmOferta.visibility = View.GONE
    }

    fun showBestProductsLoading(){
        binding.progressbarMelhoresProdutos.visibility = View.VISIBLE
    }

    fun hideBestProductsLoading(){
        binding.progressbarMelhoresProdutos.visibility = View.GONE
    }

    open fun onOfferPagingRequest(){

    }

    open fun onBestProductsPagingRequest() {

    }

    private fun setupBestProductsRv() {
        binding.rvMelhoresProdutos.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL,false)
            adapter = bestProductsAdapter
        }
    }

    private fun setupOfferRv() {
        binding.rvOferta.apply{
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = offerAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }
}