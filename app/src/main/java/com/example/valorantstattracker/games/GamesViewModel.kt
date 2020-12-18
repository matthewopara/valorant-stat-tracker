package com.example.valorantstattracker.games

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.valorantstattracker.GameResult
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.database.GameDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GamesViewModel(
        val gameDao: GameDao,
        application: Application) : AndroidViewModel(application) {

    private val _searchResults = MutableLiveData(emptyList<Game>())

    val gameHistory = gameDao.getAllGames()
    val searchResults: LiveData<List<Game>>
        get() = _searchResults

    fun createGame(game: Game)
    {
        CoroutineScope(Dispatchers.IO).launch {
            gameDao.insert(game)
        }
    }

    fun deleteGame(game: Game) {
        CoroutineScope(Dispatchers.IO).launch {
            gameDao.delete(game)
        }
    }

    fun clearGameHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            gameDao.clear()
        }
    }

    fun searchGamesByAgent(agentName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _searchResults.postValue(gameDao.getGamesWithAgent(agentName))
        }
    }

    fun searchGamesByResult(gameResult: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _searchResults.postValue(when (gameResult) {
                GameResult.WIN -> gameDao.getWonGames()
                GameResult.LOSE -> gameDao.getLostGames()
                GameResult.DRAW -> gameDao.getDrawGames()
                else -> throw IllegalArgumentException("gameResult must be either GameResult.WIN, " +
                                                        "GameResult.LOSE, or GameResult.DRAW")
            })
        }
    }
}