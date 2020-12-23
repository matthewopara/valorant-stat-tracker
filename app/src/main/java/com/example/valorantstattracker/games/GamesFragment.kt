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
import com.example.valorantstattracker.gamesrecyclerview.ClickableGameViewHolderFactory
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.FragmentGamesBinding
import com.example.valorantstattracker.gamesrecyclerview.GamesRecyclerAdapter

class GamesFragment : Fragment() {

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
        val viewHolderFactory = ClickableGameViewHolderFactory(resources)
        // TODO: set on click listeners
        //  (How will you set the visibility of the selection bar when its time to turn it on?)
        viewHolderFactory.setOnClickCallback { binding, position -> Log.d("HelloWorld", "Hello There :)") }
        viewHolderFactory.setOnLongClickCallback { binding, position -> Log.d("HelloWorld", "Greetings :P") }
        gamesAdapter = GamesRecyclerAdapter(viewHolderFactory)
        val decoration = GameItemDecoration(resources.getDimension(R.dimen.game_item_spacing).toInt())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@GamesFragment.context)
            addItemDecoration(decoration)
            adapter = gamesAdapter
        }
    }

    private fun displayGameHistory() {
        gamesViewModel.gameHistory.observe(viewLifecycleOwner, { gameHistory ->
            this.gameHistory = gameHistory
            gamesAdapter.submitList(gameHistory)
            binding.recyclerView.adapter = gamesAdapter
        })
    }
}