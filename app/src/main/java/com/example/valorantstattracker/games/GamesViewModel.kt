package com.example.valorantstattracker.games

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.valorantstattracker.GameListItem
import com.example.valorantstattracker.objects.GameResult
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.database.GameDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GamesViewModel(
    private val gameDao: GameDao,
    application: Application) : AndroidViewModel(application) {

    private val _searchResults = MutableLiveData(emptyList<Game>())

    private var _allGames = mutableListOf<GameListItem>()
    val allGames: List<GameListItem>
        get() = _allGames

    private val _allGamesUpdated = MutableLiveData(false)
    val allGamesUpdated: LiveData<Boolean>
        get() = _allGamesUpdated

    private val _gameItemUpdatedIndex = MutableLiveData(-1)
    val gameItemUpdatedIndex: LiveData<Int>
        get() = _gameItemUpdatedIndex

    private var numOfSelectedItems = 0

    init {
        updateAllGames()
    }

    fun gameItemClicked(index: Int) {
        if (numOfSelectedItems > 0) {
            changeItemSelectState(index)
        } else {
            // TODO: open game
            Log.d("HelloWorld", "Opened game at index: $index")
        }
    }

    fun gameItemLongClicked(index: Int) {
        changeItemSelectState(index)
    }

    private fun changeItemSelectState(index: Int) {
        val clickedGame = _allGames[index]
        if (clickedGame.isSelected) {
            clickedGame.isSelected = false
            _gameItemUpdatedIndex.value = index
            numOfSelectedItems--
        } else {
            clickedGame.isSelected = true
            _gameItemUpdatedIndex.value = index
            numOfSelectedItems++
        }
    }

    fun allGamesUpdatedComplete() {
        _allGamesUpdated.value = false
    }

    private fun updateAllGames() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _allGames = GameListItem.createGameItemList(gameDao.getEveryGame()).toMutableList()
                _allGamesUpdated.postValue(true)
            }
        }
    }




    val searchResults: LiveData<List<Game>>
        get() = _searchResults

    private var lastEnteredTime: Long? = null

    fun createGame(game: Game)
    {
        if (game.entryTimeMilli != lastEnteredTime) {
            lastEnteredTime = game.entryTimeMilli
            CoroutineScope(Dispatchers.IO).launch {
                gameDao.insert(game)
            }
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