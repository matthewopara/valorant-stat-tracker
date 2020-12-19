package com.example.valorantstattracker.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.FragmentGamesBinding

class GamesFragment : Fragment() {

    private lateinit var binding: FragmentGamesBinding
    private lateinit var gamesViewModel: GamesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentGamesBinding.inflate(inflater)
        gamesViewModel = createGamesViewModel()

        return binding.root
    }

    private fun createGamesViewModel(): GamesViewModel {
        val application = requireNotNull(activity).application
        val dataSource = GameDatabase.getInstance(application).getGameDao()
        return GamesViewModel(dataSource, application)
    }
}