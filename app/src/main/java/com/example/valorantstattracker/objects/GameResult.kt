package com.example.valorantstattracker.objects

import android.content.res.Resources
import com.example.valorantstattracker.R

object GameResult {
    const val WIN: Int = 0
    const val LOSE: Int = 1
    const val  DRAW: Int = 2

    fun convertResultTextToInt(result: String, resources: Resources): Int? {
        return when (result) {
            resources.getString(R.string.win) -> WIN
            resources.getString(R.string.lose) -> LOSE
            resources.getString(R.string.draw) -> DRAW
            else -> null
        }
    }
}