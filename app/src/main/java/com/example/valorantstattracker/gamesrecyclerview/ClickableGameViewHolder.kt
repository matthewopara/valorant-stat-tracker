package com.example.valorantstattracker.gamesrecyclerview

import android.content.res.Resources
import android.view.View
import com.example.valorantstattracker.databinding.GameListItemBinding

class ClickableGameViewHolder(private val binding: GameListItemBinding, resources: Resources) :
    GameViewHolderImpl(binding, resources), View.OnClickListener, View.OnLongClickListener {

    private var clickCallback: (binding: GameListItemBinding, position: Int) -> Unit = { _, _ -> }
    private var longClickCallback: (binding: GameListItemBinding, position: Int) -> Unit = { _, _ -> }

    init {
        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
    }

    fun setOnClickCallback(callback: (binding: GameListItemBinding, position: Int) -> Unit) {
        clickCallback = callback
    }

    fun setOnLongClickCallback(callback: (binding: GameListItemBinding, position: Int) -> Unit) {
        longClickCallback = callback
    }

    override fun onClick(view: View) {
        clickCallback(binding, layoutPosition)
    }

    override fun onLongClick(view: View): Boolean {
        longClickCallback(binding, layoutPosition)
        return true
    }
}