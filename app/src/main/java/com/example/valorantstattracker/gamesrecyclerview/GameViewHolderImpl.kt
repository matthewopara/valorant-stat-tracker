package com.example.valorantstattracker.gamesrecyclerview

import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.databinding.GameListItemBinding
import com.example.valorantstattracker.objects.Agent
import com.example.valorantstattracker.objects.GameResult

open class GameViewHolderImpl(binding: GameListItemBinding, val resources: Resources) :
    GameViewHolder(binding.root) {

    private val gameResultTextView: TextView = binding.gameResultText
    private val kdaTextView: TextView = binding.kdaText
    private val agentImageView: ImageView = binding.agentImage

    override fun bindViewHolder(game: Game) {
        gameResultTextView.text = GameResult.convertIntToResultString(game.result, resources)
        gameResultTextView.setTextColor(GameResult.getGameResultColor(game.result))
        kdaTextView.text = resources.getString(R.string.kda, game.kills, game.deaths, game.assists)
        val agentImgResource = Agent.getImageResource(game.agentName)
        agentImgResource?.let {
            agentImageView.setImageResource(it)
            agentImageView.contentDescription = game.agentName
        }
    }
}