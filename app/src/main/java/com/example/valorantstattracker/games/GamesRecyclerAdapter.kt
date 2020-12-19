package com.example.valorantstattracker.games

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantstattracker.Agent
import com.example.valorantstattracker.GameResult
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.databinding.GameListItemBinding

class GamesRecyclerAdapter(private val resources: Resources) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var games: List<Game> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = GameListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding, resources)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GameViewHolder -> holder.bind(games[position])
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }

    fun submitList(gamesList: List<Game>) {
        games = gamesList
    }

    private class GameViewHolder(binding: GameListItemBinding, val resources: Resources): RecyclerView.ViewHolder(binding.root) {
        val gameResult: TextView = binding.gameResultText
        val kda: TextView = binding.kdaText
        val agentImage: ImageView = binding.agentImage

        fun bind(game: Game) {
            gameResult.text = convertGameResult(game.result)
            gameResult.setTextColor(getGameResultColor(game.result))
            kda.text = resources.getString(R.string.kda, game.kills, game.deaths, game.assists)
            val imgResource = Agent.getImageResource(game.agentName)
            imgResource?.let {
                agentImage.setImageResource(it)
            }
        }

        private fun convertGameResult(result: Int): String {
            return when (result) {
                GameResult.WIN -> resources.getString(R.string.win)
                GameResult.LOSE -> resources.getString(R.string.lose)
                GameResult.DRAW -> resources.getString(R.string.draw)
                else -> "?"
            }
        }

        private fun getGameResultColor(result: Int): Int {
            return when (result) {
                GameResult.WIN -> Color.GREEN
                GameResult.LOSE -> Color.RED
                GameResult.DRAW -> Color.GRAY
                else -> Color.BLACK
            }
        }
    }
}