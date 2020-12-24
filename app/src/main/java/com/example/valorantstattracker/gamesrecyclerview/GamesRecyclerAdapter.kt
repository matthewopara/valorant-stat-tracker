package com.example.valorantstattracker.gamesrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
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

    fun submitList(newGamesList: List<GameListItem>) {
        val oldGamesList = gameListItems
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            GameItemDiffCallback(
                oldGamesList,
                newGamesList
            )
        )
        gameListItems = newGamesList
        diffResult.dispatchUpdatesTo(this)
    }

    private class GameItemDiffCallback(
        val oldGameList: List<GameListItem>,
        val newGameList: List<GameListItem>
    ): DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldGameList.size

        override fun getNewListSize(): Int = newGameList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldGameList[oldItemPosition].game.gameId == newGameList[newItemPosition].game.gameId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldGameList[oldItemPosition] == newGameList[newItemPosition]
        }
    }
}