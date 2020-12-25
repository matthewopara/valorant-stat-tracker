package com.example.valorantstattracker.games

import android.os.Bundle
import android.view.*
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantstattracker.MainActivity
import com.example.valorantstattracker.gamesrecyclerview.ClickableGameViewHolderFactory
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.FragmentGamesBinding
import com.example.valorantstattracker.gamesrecyclerview.GameViewHolderFactory
import com.example.valorantstattracker.gamesrecyclerview.GamesRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GamesFragment : Fragment() {

    private lateinit var binding: FragmentGamesBinding
    private lateinit var gamesViewModel: GamesViewModel
    private lateinit var gamesViewModelFactory: GamesViewModelFactory
    private lateinit var gamesAdapter: GamesRecyclerAdapter
    private lateinit var newGameButton: FloatingActionButton

    private var actionMode: ActionMode? = null
    private lateinit var  actionModeCallback: ActionMode.Callback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentGamesBinding.inflate(inflater)
        gamesViewModelFactory = createGamesViewModelFactory()
        gamesViewModel = ViewModelProvider(this, gamesViewModelFactory)
            .get(GamesViewModel::class.java)

        makeActionModeCallback()

        initRecyclerView()
        displayGameHistory()
        showActionBar()
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
        viewHolderFactory.setOnClickCallback { _, position ->
            gamesViewModel.onGameItemPressed(position)
        }
        viewHolderFactory.setOnLongClickCallback { _, position ->
            showActionMode()
            gamesViewModel.onGameItemLongPressed(position)
        }

        gamesViewModel.numOfSelectedItems.observe(viewLifecycleOwner, { numSelected ->
            if (numSelected > 0) {
                actionMode?.title = getString(R.string.amount_selected, numSelected)
            } else {
                actionMode?.finish()
            }

        })

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

    private fun showActionBar() {
        requireActivity().let { hostActivity ->
            if (hostActivity is MainActivity) {
                hostActivity.supportActionBar?.show()
            }
        }
    }

    private fun setUpNewGameButton() {
        newGameButton = requireActivity().findViewById(R.id.floating_action_button)
        newGameButton.show()

        newGameButton.setOnClickListener {
            gamesViewModel.onNewGameButtonPressed()
            val action = GamesFragmentDirections.actionGamesToGameEntry()
            findNavController().navigate(action)
        }
    }

    private fun showActionMode() {
        requireActivity().let { hostActivity ->
            if (hostActivity is MainActivity && actionMode == null) {
                actionMode = hostActivity.startSupportActionMode(actionModeCallback)
            }
        }
    }



    private fun makeActionModeCallback() {
        actionModeCallback = object : ActionMode.Callback {
            // Called when the action mode is created; startActionMode() was called
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                // Inflate a menu resource providing context menu items
                mode.menuInflater.inflate(R.menu.select_game_menu, menu)
                return true
            }

            // Called each time the action mode is shown. Always called after onCreateActionMode, but
            // may be called multiple times if the mode is invalidated.
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false // Return false if nothing is done
            }

            // Called when the user selects a contextual menu item
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.delete_button -> {
                        gamesViewModel.onActionModeDelete()
                        mode.finish() // Action picked, so close the CAB
                        true
                    }
                    else -> false
                }
            }

            // Called when the user exits the action mode
            override fun onDestroyActionMode(mode: ActionMode) {
                gamesViewModel.onActionModeExit()
                actionMode = null
            }
        }
    }

}