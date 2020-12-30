package com.example.valorantstattracker.insights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.valorantstattracker.MainActivity
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.FragmentInsightsBinding
import com.example.valorantstattracker.objects.BasicUIUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class InsightsFragment : Fragment() {

    private lateinit var binding: FragmentInsightsBinding
    private lateinit var gameResultChart: GameResultChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentInsightsBinding.inflate(inflater)
        BasicUIUtil.hideFloatingActionButton(requireActivity() as MainActivity)

        gameResultChart = GameResultChart(binding.pieChart, resources)
        CoroutineScope(Dispatchers.IO).launch {
            val lastGames = GameDatabase.getInstance(requireContext()).getGameDao().getLastXGames(10)
            gameResultChart.setGames(lastGames)
        }

        return binding.root
    }
}