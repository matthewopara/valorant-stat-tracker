package com.example.valorantstattracker.gamelistitem

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.valorantstattracker.database.Game

class GameListManager(val resources: Resources) {
    private val _gameItems = MutableLiveData<List<GameListItem>>(emptyList())
    val gameItems: LiveData<List<GameListItem>>
        get() = _gameItems

    private val _numOfItemsSelected = MutableLiveData(0)
    val numOfItemsSelected: LiveData<Int>
        get() = _numOfItemsSelected

    fun changeSelectState(gameItemIndex: Int) {
        _gameItems.value?.get(gameItemIndex)?.let { gameItem ->
            if (gameItem.isSelected) {
                unselect(gameItem)
            } else {
                select(gameItem)
            }
        }
    }

    private fun select(gameItem: GameListItem) {
        gameItem.isSelected = true
        _numOfItemsSelected.value = _numOfItemsSelected.value?.plus(1)
    }

    private fun unselect(gameItem: GameListItem) {
        gameItem.isSelected = false
        _numOfItemsSelected.value = _numOfItemsSelected.value?.minus(1)
    }

    fun unselectAllGameItems() {
        _gameItems.value?.forEach { gameItem ->
            if (gameItem.isSelected) {
                unselect(gameItem)
            }
        }
    }

    fun setGameItemList(newGames: List<Game>) {
        _numOfItemsSelected.value = 0
        _gameItems.value = GameListItem.createGameItemList(newGames)
    }
}