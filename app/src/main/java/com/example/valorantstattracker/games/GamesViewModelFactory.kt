package com.example.valorantstattracker.games

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.valorantstattracker.database.GameDao

class GamesViewModelFactory(private val gameDao: GameDao,
                            private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GamesViewModel::class.java)) {
            return GamesViewModel(gameDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}