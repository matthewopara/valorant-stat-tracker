package com.example.valorantstattracker.insights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.valorantstattracker.MainActivity
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.FragmentInsightsBinding
import com.example.valorantstattracker.objects.Agent
import com.example.valorantstattracker.objects.BasicUIUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class InsightsFragment : Fragment() {

    private lateinit var binding: FragmentInsightsBinding
    private lateinit var insightsViewModel: InsightsViewModel
    private lateinit var gameResultChart: GameResultChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentInsightsBinding.inflate(inflater)
        insightsViewModel = ViewModelProvider(this).get(InsightsViewModel::class.java)
        BasicUIUtil.hideFloatingActionButton(requireActivity() as MainActivity)

        gameResultChart = GameResultChart(binding.pieChart, resources)
        CoroutineScope(Dispatchers.IO).launch {
            val lastGames = GameDatabase.getInstance(requireContext()).getGameDao().getLastXGames(10)
            gameResultChart.setGames(lastGames)
        }

        gameResultChart.setOnValuePressedListener { trackers ->
            insightsViewModel.setTrackers(trackers)
        }

        insightsViewModel.trackers.observe(viewLifecycleOwner) { trackers ->
            val agentItems = listOf(binding.firstAgentItem, binding.secondAgentItem, binding.thirdAgentItem, binding.fourthAgentItem)
            var count = trackers.size
            if (count > agentItems.size) {
                count = agentItems.size
            }

            for (item in agentItems) {
                item.root.visibility = View.INVISIBLE
            }

            for (i in 0 until count) {
                agentItems[i].root.visibility = View.VISIBLE
                agentItems[i].winCount.text = resources.getString(R.string.win_display, trackers[i].winCount)
                agentItems[i].loseCount.text = resources.getString(R.string.lose_display, trackers[i].loseCount)
                agentItems[i].drawCount.text = resources.getString(R.string.draw_display, trackers[i].drawCount)
                val imgRes = Agent.getImageResource(trackers[i].agentName)
                imgRes?.let { res ->
                    agentItems[i].agentView.setImageResource(res)
                    agentItems[i].agentView.contentDescription = trackers[i].agentName
                }
            }
        }

        return binding.root
    }
}