package com.example.valorantstattracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.valorantstattracker.databinding.FragmentInsightsBinding

class InsightsFragment : Fragment() {

    private lateinit var binding: FragmentInsightsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentInsightsBinding.inflate(inflater)

        return binding.root
    }
}