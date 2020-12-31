package com.example.valorantstattracker.games

import android.app.Application
import androidx.lifecycle.*
import com.example.valorantstattracker.gamelistitem.GameListItem
import com.example.valorantstattracker.gamelistitem.GameListManager
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.database.GameDao
import com.example.valorantstattracker.objects.FLAGGED
import com.example.valorantstattracker.objects.NOT_FLAGGED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GamesViewModel(
    private val gameDao: GameDao,
    application: Application) : AndroidViewModel(application) {

    private val resources = application.resources
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
            resources.getString(R.string.amount_selected, itemCount)
        }

    private val _deletionConfirmMessage = MutableLiveData("")
    val deletionConfirmMessage: LiveData<String>
        get() = _deletionConfirmMessage

    private val _showActionMode = MutableLiveData(false)
    val showActionMode: LiveData<Boolean>
        get() = _showActionMode

    private val _navigateToGameEntry = MutableLiveData(false)
    val navigateToGameEntry: LiveData<Boolean>
        get() = _navigateToGameEntry

    private val _openGame = MutableLiveData<Game>()
    val openGame: LiveData<Game>
        get() = _openGame

    private val recentlyDeletedGames = mutableListOf<Game>()

    init {
        resetGameListManager()
    }

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
            openGameInfo(itemIndex)
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

    private fun openGameInfo(itemIndex: Int) {
        gameListManager.gameItems.value?.get(itemIndex)?.game?.let {
            _openGame.value = it
        }
    }

    fun openGameComplete() {
        _openGame.value = null
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
        recentlyDeletedGames.clear()
        gameListManager.gameItems.value?.forEach { gameItem ->
            if (gameItem.isSelected) {
                gameItem.game.deleteFlag = FLAGGED
                recentlyDeletedGames.add(gameItem.game)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            recentlyDeletedGames.forEach {
                gameDao.update(it)
            }
            resetGameListManager()
            createDeleteConfirmMessage(recentlyDeletedGames.size)
        }
    }

    private fun createDeleteConfirmMessage(amount: Int) {
        if (amount == 1) {
            _deletionConfirmMessage.postValue(resources.getString(R.string.one_game_deleted))
        } else {
            _deletionConfirmMessage.postValue(resources.getString(R.string.games_deleted, amount))
        }
    }

    fun showDeleteConfirmationComplete() {
        _deletionConfirmMessage.value = ""
    }

    fun undoDeletePressed() {
        CoroutineScope(Dispatchers.IO).launch {
            recentlyDeletedGames.forEach { game ->
                game.deleteFlag = NOT_FLAGGED
                gameDao.update(game)
            }
            resetGameListManager()
        }
    }

    fun newGameButtonPressed() {
        _navigateToGameEntry.value = true
    }

    fun navigateToGameEntryComplete() {
        _navigateToGameEntry.value = false
    }
}