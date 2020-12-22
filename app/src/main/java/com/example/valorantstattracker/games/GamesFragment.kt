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
import com.example.valorantstattracker.ItemClickListener
import com.example.valorantstattracker.ItemLongClickListener
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.FragmentGamesBinding

class GamesFragment : Fragment(), ItemClickListener, ItemLongClickListener {

    private lateinit var binding: FragmentGamesBinding
    private lateinit var gamesViewModel: GamesViewModel
    private lateinit var gamesViewModelFactory: GamesViewModelFactory
    private lateinit var gamesAdapter: GamesRecyclerAdapter
    private var gameHistory: List<Game>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentGamesBinding.inflate(inflater)
        gamesViewModelFactory = createGamesViewModelFactory()
        gamesViewModel = ViewModelProvider(this, gamesViewModelFactory)
            .get(GamesViewModel::class.java)

        initRecyclerView()
        displayGameHistory()

        binding.newGameButton.setOnClickListener {
            val action = GamesFragmentDirections.actionGamesToGameEntry()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun createGamesViewModelFactory(): GamesViewModelFactory {
        val application = requireNotNull(activity).application
        val dataSource = GameDatabase.getInstance(application).getGameDao()
        return GamesViewModelFactory(dataSource, application)
    }

    private fun initRecyclerView() {
        gamesAdapter = GamesRecyclerAdapter(resources, this, this)
        val decoration = GameItemDecoration(resources.getDimension(R.dimen.game_item_spacing).toInt())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@GamesFragment.context)
            addItemDecoration(decoration)
            adapter = gamesAdapter
        }
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d("GamesFragment", "Agent Name Clicked: ${gameHistory?.get(position)?.agentName}")
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d("GamesFragment", "Game Kills Long Clicked: ${gameHistory?.get(position)?.kills}")
    }

    private fun displayGameHistory() {
        gamesViewModel.gameHistory.observe(viewLifecycleOwner, { gameHistory ->
            this.gameHistory = gameHistory
            gamesAdapter.submitList(gameHistory)
            binding.recyclerView.adapter = gamesAdapter
        })
    }
}