package com.example.valorantstattracker.gamesrecyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantstattracker.GameListItem
import com.example.valorantstattracker.database.Game

abstract class GameViewHolder(itemView: View):
    RecyclerView.ViewHolder(itemView) {

    abstract fun bindViewHolder(gameListItem: GameListItem)
}