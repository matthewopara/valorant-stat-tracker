package com.example.valorantstattracker.games

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.valorantstattracker.GameListItem
import com.example.valorantstattracker.GameListManager
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.GameDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GamesViewModel(
    private val gameDao: GameDao,
    application: Application) : AndroidViewModel(application) {

    private val gameListManager = GameListManager(application.resources)

    val gameItems: LiveData<List<GameListItem>>
        get() = gameListManager.gameItems

    private val _updateEntireList = MutableLiveData(false)
    val updateEntireList: LiveData<Boolean>
        get() = _updateEntireList

    private val _gameItemUpdatedIndex = MutableLiveData(-1)
    val gameItemUpdatedIndex: LiveData<Int>
        get() = _gameItemUpdatedIndex

    val numOfItemsSelectedTitle: LiveData<String>
        get() = Transformations.map(gameListManager.numOfItemsSelected) { itemCount ->
            getApplication<Application>().resources.getString(R.string.amount_selected, itemCount)
        }

    private val _showActionMode = MutableLiveData(false)
    val showActionMode: LiveData<Boolean>
        get() = _showActionMode

    private val _navigateToGameEntry = MutableLiveData(false)
    val navigateToGameEntry: LiveData<Boolean>
        get() = _navigateToGameEntry

    init {
        resetGameListManager()
        //updateAllGames()
    }

    // TODO: Eventually will have to filter list to only contain games that are not flagged for deletion
    private fun resetGameListManager() {
        CoroutineScope(Dispatchers.IO).launch {
            val games = gameDao.getAllGames()
            withContext(Dispatchers.Main) {
                gameListManager.setGameItemList(games)
            }
        }
    }

    fun gameItemPressed(itemIndex: Int) {
        if (isSelecting()) {
            changeItemSelectState(itemIndex)
        } else {
            openGameInfo()
        }
    }

    fun gameItemLongPressed(itemIndex: Int) {
        if (_showActionMode.value == false) {
            _showActionMode.value = true
        }
        changeItemSelectState(itemIndex)
    }

    private fun isSelecting(): Boolean {
        val amountSelected = gameListManager.numOfItemsSelected.value ?: 0
        return amountSelected > 0
    }

    private fun changeItemSelectState(itemIndex: Int) {
        gameListManager.changeSelectState(itemIndex)
        _gameItemUpdatedIndex.value = itemIndex
        if (gameListManager.numOfItemsSelected.value == 0) {
            _showActionMode.value = false
        }
    }

    private fun openGameInfo() {
        // TODO: Need to Implement
        Log.d("GamesViewModel", "Open game info screen")
    }

    fun actionModeExit() {
        gameListManager.unselectAllGameItems()
        _showActionMode.value = false
        _updateEntireList.value = true
    }

    fun updateEntireListComplete() {
        _updateEntireList.value = false
    }

    fun actionModeDeletePressed() {
        // TODO: Need to Implement
        //  For now delete selected games and reset game list manager. Don't unselect all games
        //  Later implement flagging to allow user to undo a deletion (alo reset game list manager)
        Log.d("GamesViewModel", "Delete")
    }

    fun newGameButtonPressed() {
        _navigateToGameEntry.value = true
    }

    fun navigateToGameEntryComplete() {
        _navigateToGameEntry.value = false
    }

    override fun onCleared() {
        super.onCleared()
        deleteFlaggedGames()
    }

    private fun deleteFlaggedGames() {
        // TODO: Need to implement
    }

    private fun clearGameHistory() {
        // TODO: Need to implement
        //  Flag all games for deletion and reset game list manager
    }
}