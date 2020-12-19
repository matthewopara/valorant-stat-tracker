package com.example.valorantstattracker.games

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantstattracker.GameResult
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.Game

class GamesRecyclerAdapter(private val resources: Resources) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Game> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GameViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.game_list_item, parent, false), resources)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GameViewHolder -> holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(gamesList: List<Game>) {
        items = gamesList
    }

    private class GameViewHolder(itemView: View, val resources: Resources): RecyclerView.ViewHolder(itemView) {
        val gameResult: TextView = itemView.findViewById(R.id.game_result_text)
        val kda: TextView = itemView.findViewById(R.id.kda_text)
        val agentImage: ImageView = itemView.findViewById(R.id.agent_image)

        fun bind(game: Game) {
            gameResult.text = convertGameResult(game.result)
            gameResult.setTextColor(getGameResultColor(game.result))
            kda.text = resources.getString(R.string.kda, game.kills, game.deaths, game.assists)
            // TODO: set image to correct agent image
            agentImage.setImageResource(R.drawable.ic_launcher_background)
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