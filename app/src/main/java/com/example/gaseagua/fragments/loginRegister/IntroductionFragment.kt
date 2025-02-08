package com.example.gaseagua.fragments.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gaseagua.R
import com.example.gaseagua.databinding.FragmentIntroductionBinding


class IntroductionFragment: Fragment(R.layout.fragment_introduction) {
    private lateinit var binding: FragmentIntroductionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener{
            findNavController().navigate(R.id.action_introductionFragment_to_loginFragment)
        }
        binding.buttonRegister.setOnClickListener{
            findNavController().navigate(R.id.action_introductionFragment_to_registerFragment)
        }
    }
}