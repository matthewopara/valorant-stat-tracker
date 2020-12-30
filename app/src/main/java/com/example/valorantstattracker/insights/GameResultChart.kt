package com.example.valorantstattracker.insights

import android.content.res.Resources
import android.graphics.Color
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.objects.GameResult
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

class GameResultChart(val chart: PieChart, private val resources: Resources) {

    private var allTrackers = setOf<GameResultTracker>()
    private var numOfWins = 0f
    private var numOfLoses = 0f
    private var numOfDraws = 0f

    init {
        setChartAttributes()
    }

    private fun setChartAttributes() {
        chart.centerText = resources.getString(R.string.game_results)
        chart.setCenterTextSize(20f)
        chart.setEntryLabelColor(Color.WHITE)
        chart.setUsePercentValues(true)
        chart.legend.setCustom(emptyArray())
        chart.description.isEnabled = false
    }

    fun setGames(games: List<Game>) {
        allTrackers = createTrackers(games)
        updateChart()
    }

    private fun createTrackers(games: List<Game>): Set<GameResultTracker> {
        val trackerSet = mutableSetOf<GameResultTracker>()
        for (game in games) {
            when (game.result) {
                GameResult.WIN -> numOfWins++
                GameResult.LOSE -> numOfLoses++
                GameResult.DRAW -> numOfDraws++
            }
            val tracker = allTrackers.find { it.agentName == game.agentName }
            if (tracker == null) {
                val newTracker = GameResultTracker(game.agentName)
                newTracker.addGameResult(game)
                trackerSet.add(newTracker)
            } else {
                tracker.addGameResult(game)
                trackerSet.add(tracker)
            }
        }
        return trackerSet
    }

    private fun updateChart() {
        val entries = arrayListOf<PieEntry>()
        val winEntry = PieEntry(numOfWins, resources.getString(R.string.win))
        val loseEntry = PieEntry(numOfLoses, resources.getString(R.string.lose))
        val drawEntry = PieEntry(numOfDraws, resources.getString(R.string.draw))
        entries.addAll(listOf(winEntry, loseEntry, drawEntry))

        val dataSet = PieDataSet(entries, "")
        dataSet.apply {
            sliceSpace = 3f
            selectionShift = 5f
            colors = listOf(
                GameResult.getGameResultColor(GameResult.WIN),
                GameResult.getGameResultColor(GameResult.LOSE),
                GameResult.getGameResultColor(GameResult.DRAW)
            )
            valueFormatter = PercentFormatter(chart)
            valueTextSize = 11f
            valueTextColor = Color.WHITE
        }
        chart.data = PieData(dataSet)
        chart.invalidate()
    }
}