package com.example.valorantstattracker.gamesrecyclerview

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.valorantstattracker.GameListItem
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
    private val selectionBarImageView: ImageView = binding.selectionBar

    override fun bindViewHolder(gameListItem: GameListItem) {
        gameResultTextView.text = GameResult.convertIntToResultString(gameListItem.game.result, resources)
        gameResultTextView.setTextColor(GameResult.getGameResultColor(gameListItem.game.result))
        kdaTextView.text = resources.getString(
            R.string.kda,
            gameListItem.game.kills,
            gameListItem.game.deaths,
            gameListItem.game.assists
        )
        val agentImgResource = Agent.getImageResource(gameListItem.game.agentName)
        agentImgResource?.let {
            agentImageView.setImageResource(it)
            agentImageView.contentDescription = gameListItem.game.agentName
        }
        if (gameListItem.isSelected) {
            selectionBarImageView.visibility = View.VISIBLE
        } else {
            selectionBarImageView.visibility = View.INVISIBLE
        }
    }
}