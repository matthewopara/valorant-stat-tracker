package com.example.valorantstattracker.gamesrecyclerview

import com.example.valorantstattracker.databinding.GameListItemBinding

interface GameViewHolderFactory {
    fun <T : GameViewHolder> create(modelClass: Class<T>, binding: GameListItemBinding) : T
}