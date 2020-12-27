package com.example.valorantstattracker.gamesrecyclerview

import androidx.recyclerview.widget.DiffUtil
import com.example.valorantstattracker.gamelistitem.GameListItem

class GameItemDiffCallback(
    private val oldGameList: List<GameListItem>,
    private val newGameList: List<GameListItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldGameList.size

    override fun getNewListSize(): Int = newGameList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldGameList[oldItemPosition].game.gameId == newGameList[newItemPosition].game.gameId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldGameList[oldItemPosition] == newGameList[newItemPosition]
    }
}