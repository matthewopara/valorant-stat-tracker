package com.example.valorantstattracker.gamesrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.databinding.GameListItemBinding

class GamesRecyclerAdapter(private val factory: GameViewHolderFactory) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var games: List<Game> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = GameListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return factory.create(GameViewHolder::class.java, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GameViewHolder -> holder.bindViewHolder(games[position])
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }

    fun submitList(gamesList: List<Game>) {
        games = gamesList
    }
}