package com.example.valorantstattracker.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "game_table")
data class Game(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "game_id")
    var gameId: Long = 0L,

    @ColumnInfo(name = "entry_time_milli")
    val entryTimeMilli: Long = System.currentTimeMillis(),

    val result: Int,

    @ColumnInfo(name = "agent_name")
    val agentName: String,

    @ColumnInfo(name = "combat_score")
    val combatScore: Int,

    val kills: Int,
    val deaths: Int,
    val assists: Int,

    @ColumnInfo(name = "econ_rating")
    val econRating: Int,

    @ColumnInfo(name = "first_bloods")
    val firstBloods: Int,

    val plants: Int,
    val defuses: Int,
) : Parcelable
