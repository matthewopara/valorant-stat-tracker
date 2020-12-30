package com.example.valorantstattracker.insights

import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.objects.GameResult

class GameResultTracker(val agentName: String, var winCount: Int = 0, var loseCount: Int = 0, var drawCount: Int = 0) {
    val numOfGames: Int
        get() = winCount + loseCount + drawCount

    fun getWinPercentage(): Float = winCount.toFloat() / numOfGames.toFloat()

    fun getLosePercentage(): Float = loseCount.toFloat() / numOfGames.toFloat()

    fun getDrawPercentage(): Float = drawCount.toFloat() / numOfGames.toFloat()

    fun addGameResult(game: Game) {
        when (game.result) {
            GameResult.WIN -> winCount++
            GameResult.LOSE -> loseCount++
            GameResult.DRAW -> drawCount++
            else -> throw Exception("Received a non-valid game result int")
        }
    }
}