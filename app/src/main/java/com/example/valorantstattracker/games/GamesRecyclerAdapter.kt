package com.example.valorantstattracker.games

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantstattracker.objects.Agent
import com.example.valorantstattracker.objects.GameResult
import com.example.valorantstattracker.R
import com.example.valorantstattracker.ItemClickListener
import com.example.valorantstattracker.ItemLongClickListener
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.databinding.GameListItemBinding

class GamesRecyclerAdapter(private val resources: Resources) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    constructor(resources: Resources, itemClickListener: ItemClickListener): this(resources) {
        this.itemClickListener = itemClickListener
    }

    constructor(resources: Resources, itemClickListener: ItemClickListener,
                itemLongClickListener: ItemLongClickListener): this(resources) {
        this.itemClickListener = itemClickListener
        this.itemLongClickListener = itemLongClickListener
    }

    private var games: List<Game> = ArrayList()
    private var itemClickListener: ItemClickListener? = null
    private var itemLongClickListener: ItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = GameListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding, resources, itemClickListener, itemLongClickListener)
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

    private class GameViewHolder(
        binding: GameListItemBinding,
        val resources: Resources,
        val clickListener: ItemClickListener?,
        val longClickListener: ItemLongClickListener?): RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

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
                agentImage.contentDescription = game.agentName
            }
        }

        override fun onClick(view: View) {
            clickListener?.onItemClick(view, layoutPosition)
        }

        override fun onLongClick(view: View): Boolean {
            longClickListener?.apply {
                onItemLongClick(view, layoutPosition)
                return true
            }
            return false
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