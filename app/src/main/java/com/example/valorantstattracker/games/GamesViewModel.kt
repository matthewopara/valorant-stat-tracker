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

    private var _allGames = MutableLiveData<List<GameListItem>>()
    val allGames: LiveData<List<GameListItem>>
        get() = _allGames

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

    fun unSelectAllGameItems() {
        _allGames.value?.let { list ->
            for (gameItem in list) {
                if (gameItem.isSelected) {
                    unSelectGameItem(gameItem)
                }
            }
        }
    }

    private fun changeItemSelectState(index: Int) {
        _allGames.value?.get(index)?.let { clickedGameItem ->
            if (clickedGameItem.isSelected) {
                unSelectGameItem(clickedGameItem)
            } else {
                selectGameItem(clickedGameItem)
            }
            _gameItemUpdatedIndex.value = index
        }
    }

    private fun unSelectGameItem(gameListItem: GameListItem) {
        gameListItem.isSelected = false
        numOfSelectedItems--
    }

    private fun selectGameItem(gameListItem: GameListItem) {
        gameListItem.isSelected = true
        numOfSelectedItems++
    }

    private fun updateAllGames() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _allGames.postValue(GameListItem.createGameItemList(gameDao.getAllGames()))
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

    fun deleteSelectedGames() {
        CoroutineScope(Dispatchers.IO).launch {
            _allGames.value?.forEach {
                if (it.isSelected) {
                    gameDao.delete(it.game)
                }
            }
            updateAllGames()
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