package com.example.valorantstattracker.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.FragmentGamesBinding

class GamesFragment : Fragment() {

    private lateinit var binding: FragmentGamesBinding
    private lateinit var gamesViewModel: GamesViewModel
    private lateinit var gamesViewModelFactory: GamesViewModelFactory
    private lateinit var gamesAdapter: GamesRecyclerAdapter
    private val args: GamesFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentGamesBinding.inflate(inflater)
        gamesViewModelFactory = createGamesViewModelFactory()
        gamesViewModel = ViewModelProvider(this, gamesViewModelFactory)
            .get(GamesViewModel::class.java)

        // Inserts new game into database if one is received from GameEntryFragment
        args.newGame?.let { newGame ->
            gamesViewModel.createGame(newGame)
        }

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
        gamesAdapter = GamesRecyclerAdapter(resources)
        val decoration = GameItemDecoration(resources.getDimension(R.dimen.game_item_spacing).toInt())
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