package com.example.gaseagua.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gaseagua.R
import com.example.gaseagua.adapters.HomeViewpagerAdapter
import com.example.gaseagua.databinding.FragmentHomeBinding
import com.example.gaseagua.fragments.categories.GasFragment
import com.example.gaseagua.fragments.categories.MainCategoryFragment
import com.example.gaseagua.fragments.categories.AguaFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            AguaFragment(),
            GasFragment(),
        )

        binding.viewpagerHome.isUserInputEnabled = false

        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome){
            tab, position ->
            when(position){
                0 -> tab.text = "Principal"
                1 -> tab.text = "Água"
                2 -> tab.text = "Gás"
            }
        }.attach()
    }
}