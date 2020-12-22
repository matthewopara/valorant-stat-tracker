package com.example.valorantstattracker.gameentry

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.valorantstattracker.database.GameDao
import com.example.valorantstattracker.games.GamesViewModel

class GameEntryViewModelFactory(private val gameDao: GameDao,
                                private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameEntryViewModel::class.java)) {
            return GameEntryViewModel(gameDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}