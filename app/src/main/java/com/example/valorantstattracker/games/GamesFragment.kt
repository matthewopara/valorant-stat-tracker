package com.example.valorantstattracker.games

import android.os.Bundle
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
import com.example.valorantstattracker.gamesrecyclerview.GameViewHolderFactory
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
        setUpNewGameButton()

        return binding.root
    }

    private fun createGamesViewModelFactory(): GamesViewModelFactory {
        val application = requireNotNull(activity).application
        val dataSource = GameDatabase.getInstance(application).getGameDao()
        return GamesViewModelFactory(dataSource, application)
    }

    private fun initRecyclerView() {
        gamesAdapter = GamesRecyclerAdapter(createViewHolderFactory())
        val decoration = GameItemDecoration(resources.getDimension(R.dimen.game_item_spacing).toInt())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@GamesFragment.context)
            addItemDecoration(decoration)
            adapter = gamesAdapter
        }
    }

    private fun createViewHolderFactory(): GameViewHolderFactory {
        val viewHolderFactory = ClickableGameViewHolderFactory(resources)
        viewHolderFactory.setOnClickCallback { _, position -> gamesViewModel.gameItemClicked(position) }
        viewHolderFactory.setOnLongClickCallback { _, position -> gamesViewModel.gameItemLongClicked(position) }
        return viewHolderFactory
    }

    private fun displayGameHistory() {
        // TODO: Do I really need to observe changes
        //  to the entire games list?
        //  Since I passed the same list reference to
        //  the adapter, the list might be updated
        //  automatically when changed from the view model.
        //  Maybe i only need to call gameAdapter.notifyItemRemoved
        //  for every item that was deleted

        displayAllGamesChanges()
        displaySingleItemChanges()
    }

    private fun displayAllGamesChanges() {
        gamesViewModel.allGames.observe(viewLifecycleOwner, { newList ->
            gamesAdapter.submitList((newList))
        })
    }

    private fun displaySingleItemChanges() {
        gamesViewModel.gameItemUpdatedIndex.observe(viewLifecycleOwner, { index ->
            if (index >= 0) {
                gamesAdapter.notifyItemChanged(index)
            }
        })
    }

    private fun setUpNewGameButton() {
        binding.newGameButton.setOnClickListener {
            gamesViewModel.unSelectAllGameItems()
            val action = GamesFragmentDirections.actionGamesToGameEntry()
            findNavController().navigate(action)
        }
    }
}