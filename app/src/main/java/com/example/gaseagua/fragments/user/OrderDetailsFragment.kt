package com.example.gaseagua.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gaseagua.data.order.OrderStatus
import com.example.gaseagua.data.order.getOrderStatus
import com.example.gaseagua.databinding.FragmentOrderDetailBinding
import com.example.gaseagua.util.VerticalItemDecoration
import com.example.kelineyt.adapters.BillingProductsAdapter

class OrderDetailsFragment: Fragment() {

    private lateinit var binding: FragmentOrderDetailBinding
    private val billingProductsAdapter by lazy { BillingProductsAdapter() }
    private val args by navArgs<OrderDetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val order = args.order
        setupOrderRv()

        binding.apply {
            tvIDDoPedido.text = "Pedido #${order.orderId}"

            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Canceled.status,
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Delivered.status
                )
            )

            val currentOrderState = when(getOrderStatus(order.orderStatus)){
                is OrderStatus.Canceled -> 0
                is OrderStatus.Ordered -> 1
                is OrderStatus.Confirmed -> 2
                is OrderStatus.Delivered -> 3
            }
            stepView.go(currentOrderState,false)
            if(currentOrderState == 3) {
                stepView.done(true)
            }
            tvNomeCompleto.text = order.address.fullName
            tvEndereO2.text = "${order.address.fullAddress} ${order.address.city} ${order.address.state}"
            tvCelular.text = order.address.phone
            tvPreOTotal.text = order.totalPrice.toString()
        }

        billingProductsAdapter.differ.submitList(order.products)
    }

    private fun setupOrderRv() {
        binding.rvProdutos.apply {
            adapter = billingProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
            addItemDecoration(VerticalItemDecoration())
        }
    }
}