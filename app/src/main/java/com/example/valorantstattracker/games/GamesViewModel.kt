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
    val searchResults: LiveData<List<Game>>
        get() = _searchResults

    private var _allGames = MutableLiveData<List<GameListItem>>()
    val allGames: LiveData<List<GameListItem>>
        get() = _allGames

    private val _gameItemUpdatedIndex = MutableLiveData(-1)
    val gameItemUpdatedIndex: LiveData<Int>
        get() = _gameItemUpdatedIndex

    private var _numOfSelectedItems = MutableLiveData(0)
    val numOfSelectedItems: LiveData<Int>
        get() = _numOfSelectedItems

    init {
        updateAllGames()
    }

    fun onGameItemPressed(index: Int) {
        val amountSelected = _numOfSelectedItems.value ?: 0
        if (amountSelected > 0) {
            changeItemSelectState(index)
        } else {
            // TODO: open game
            Log.d("HelloWorld", "Opened game at index: $index")
        }
    }

    fun onGameItemLongPressed(index: Int) {
        changeItemSelectState(index)
    }

    fun onActionModeExit() {
        unSelectAllGameItems()
    }

    fun onActionModeDelete() {
        // TODO: Instead of deleting, set a flag on the game. In OnCleared, delete all games that are flagged
        deleteSelectedGames()
    }

    fun onNewGameButtonPressed() {
        unSelectAllGameItems()
    }

    private fun unSelectAllGameItems() {
        _allGames.value?.let { list ->
            for (i in list.indices) {
                if (list[i].isSelected) {
                    unSelectGameItem(list[i])
                    _gameItemUpdatedIndex.value = i
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
        _numOfSelectedItems.value = _numOfSelectedItems.value?.minus(1)
    }

    private fun selectGameItem(gameListItem: GameListItem) {
        gameListItem.isSelected = true
        _numOfSelectedItems.value = _numOfSelectedItems.value?.plus(1)
    }

    private fun updateAllGames() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _allGames.postValue(GameListItem.createGameItemList(gameDao.getAllGames()))
            }
        }
    }


    private fun deleteSelectedGames() {
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