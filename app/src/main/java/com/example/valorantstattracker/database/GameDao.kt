package com.example.valorantstattracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.valorantstattracker.Constants

@Dao
interface GameDao {

    @Insert
    suspend fun insert(game: Game)

    @Delete
    suspend fun delete(game: Game)

    @Query("DELETE FROM game_table")
    suspend fun clear()

    @Query("SELECT * FROM game_table WHERE game_id = :id")
    suspend fun getGame(id: Long): Game?

    @Query("SELECT * FROM game_table")
    suspend fun getAllGames(): LiveData<List<Game>>

    @Query("SELECT * FROM game_table ORDER BY game_id DESC LIMIT :x")
    suspend fun getLastXGames(x: Int): List<Game>

    @Query("SELECT * FROM game_table WHERE agent_name = :agentName ORDER BY game_id DESC")
    suspend fun getGamesWithAgent(agentName: String): List<Game>

    @Query("SELECT * FROM game_table WHERE result = ${Constants.WIN} ORDER BY game_id DESC")
    suspend fun getWonGames(): List<Game>

    @Query("SELECT * FROM game_table WHERE result = ${Constants.LOSE} ORDER BY game_id DESC")
    suspend fun getLostGames(): List<Game>

    @Query("SELECT * FROM game_table WHERE result = ${Constants.DRAW} ORDER BY game_id DESC")
    suspend fun getDrawGames(): List<Game>
}