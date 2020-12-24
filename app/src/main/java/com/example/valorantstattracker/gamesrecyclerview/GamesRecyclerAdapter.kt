package com.example.valorantstattracker.gamesrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantstattracker.GameListItem
import com.example.valorantstattracker.databinding.GameListItemBinding

class GamesRecyclerAdapter(private val factory: GameViewHolderFactory) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var gameListItems: List<GameListItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = GameListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return factory.create(GameViewHolder::class.java, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GameViewHolder -> holder.bindViewHolder(gameListItems[position])
        }
    }

    override fun getItemCount(): Int {
        return gameListItems.size
    }

    fun submitList(gamesList: List<GameListItem>) {
        gameListItems = gamesList
    }
}