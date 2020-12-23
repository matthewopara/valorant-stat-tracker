package com.example.valorantstattracker.gamesrecyclerview

import android.content.res.Resources
import android.view.View
import com.example.valorantstattracker.databinding.GameListItemBinding

class ClickableGameViewHolder(binding: GameListItemBinding, resources: Resources) :
    GameViewHolderImpl(binding, resources), View.OnClickListener, View.OnLongClickListener {

    private var clickCallback: (view: View, position: Int) -> Unit = { _, _ -> }
    private var longClickCallback: (view: View, position: Int) -> Unit = { _, _ -> }

    init {
        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
    }

    fun setOnClickCallback(callback: (view: View, position: Int) -> Unit) {
        clickCallback = callback
    }

    fun setOnLongClickCallback(callback: (view: View, position: Int) -> Unit) {
        longClickCallback = callback
    }

    override fun onClick(view: View) {
        clickCallback(view, layoutPosition)
    }

    override fun onLongClick(view: View): Boolean {
        longClickCallback(view, layoutPosition)
        return true
    }
}