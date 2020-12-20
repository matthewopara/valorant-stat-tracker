package com.example.valorantstattracker.games

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantstattracker.Agent
import com.example.valorantstattracker.GameResult
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.FragmentGamesBinding

class GamesFragment : Fragment() {

    private lateinit var binding: FragmentGamesBinding
    private lateinit var gamesViewModel: GamesViewModel
    private lateinit var gamesViewModelFactory: GamesViewModelFactory
    private lateinit var gamesAdapter: GamesRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentGamesBinding.inflate(inflater)
        gamesViewModelFactory = createGamesViewModelFactory()
        gamesViewModel = ViewModelProvider(this, gamesViewModelFactory)
            .get(GamesViewModel::class.java)

        // TODO: Remove this for loop after implementing FAB onClick
//        for (i in 1..15) {
//            gamesViewModel.createGame(makeGame(Agent.OMEN))
//        }

        initRecyclerView()
        displayGameHistory()

        binding.newGameButton.setOnClickListener {
            // TODO: Open game entry fragment for game data entry
            val action = GamesFragmentDirections.actionGamesToGameEntry()
            findNavController().navigate(action)
            Log.d("GamesFragment", "Enter Game Data")
        }

        return binding.root
    }

    // TODO: Remove this testing method after implementing FAB onClick
    private fun makeGame(agentName: String): Game {
        return Game(entryTimeMilli = 10, result = GameResult.WIN,
            agentName = agentName, combatScore = 100, kills = 20, deaths = 20,
            assists = 20, econRating = 30, firstBloods = 2, plants = 2, defuses = 1)
    }

    private fun createGamesViewModelFactory(): GamesViewModelFactory {
        val application = requireNotNull(activity).application
        val dataSource = GameDatabase.getInstance(application).getGameDao()
        return GamesViewModelFactory(dataSource, application)
    }

    private fun initRecyclerView() {
        gamesAdapter = GamesRecyclerAdapter(resources)
        val decoration = GameItemDecoration(R.dimen.game_item_spacing)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@GamesFragment.context)
            addItemDecoration(decoration)
            adapter = gamesAdapter
        }
    }

    private fun displayGameHistory() {
        gamesViewModel.gameHistory.observe(viewLifecycleOwner, { gameHistory ->
            gamesAdapter.submitList(gameHistory)
            binding.recyclerView.adapter = gamesAdapter
        })
    }
}