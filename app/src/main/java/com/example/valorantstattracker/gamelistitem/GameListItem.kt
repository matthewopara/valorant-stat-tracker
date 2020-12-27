package com.example.valorantstattracker.gamelistitem

import com.example.valorantstattracker.database.Game

data class GameListItem(
    val game: Game,
    var isSelected: Boolean
) {
    companion object {
        fun createGameItemList(gameList: List<Game>): List<GameListItem> {
            val gameListItems = mutableListOf<GameListItem>()
            for (game in gameList) {
                gameListItems.add(GameListItem(game, false))
            }
            return gameListItems
        }
    }
}