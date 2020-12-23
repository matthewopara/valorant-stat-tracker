package com.example.valorantstattracker.gamesrecyclerview

import android.content.res.Resources
import android.view.View
import com.example.valorantstattracker.databinding.GameListItemBinding

class ClickableGameViewHolderFactory(private val resources: Resources) : GameViewHolderFactory {

    private var clickCalllback: (binding: GameListItemBinding, position: Int) -> Unit = { _, _ -> }
    private var longClickCallback: (binding: GameListItemBinding, position: Int) -> Unit = { _, _ -> }

    @Suppress("UNCHECKED_CAST")
    override fun <T : GameViewHolder> create(modelClass: Class<T>, binding: GameListItemBinding): T {
        if (modelClass.isAssignableFrom(ClickableGameViewHolder::class.java)) {
            val clickableGameViewHolder = ClickableGameViewHolder(binding, resources)
            clickableGameViewHolder.setOnClickCallback(clickCalllback)
            clickableGameViewHolder.setOnLongClickCallback(longClickCallback)
            return clickableGameViewHolder as T
        }
        throw IllegalArgumentException("Unknown GameViewHolder class")
    }

    fun setOnClickCallback(callback: (binding: GameListItemBinding, position: Int) -> Unit) {
        clickCalllback = callback
    }

    fun setOnLongClickCallback(callback: (binding: GameListItemBinding, position: Int) -> Unit) {
        longClickCallback = callback
    }

}