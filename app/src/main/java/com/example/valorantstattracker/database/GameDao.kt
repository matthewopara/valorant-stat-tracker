package com.example.valorantstattracker.database

import androidx.room.*
import com.example.valorantstattracker.objects.GameResult

@Dao
interface GameDao {

    @Insert
    suspend fun insert(game: Game)

    @Delete
    suspend fun delete(game: Game)

    @Update
    suspend fun update(game: Game)

    @Query("DELETE FROM game_table WHERE delete_flag = 1")
    suspend fun deleteFlaggedGames()

    @Query("DELETE FROM game_table")
    suspend fun clear()

    @Query("SELECT * FROM game_table WHERE game_id = :id")
    suspend fun getGame(id: Long): Game?

    @Query("SELECT * FROM game_table WHERE delete_flag = 0 ORDER BY game_id DESC")
    fun getAllGames(): List<Game>

    @Query("SELECT * FROM game_table WHERE delete_flag = 0 ORDER BY game_id DESC LIMIT :x")
    suspend fun getLastXGames(x: Int): List<Game>

    @Query("SELECT * FROM game_table WHERE delete_flag = 0 AND agent_name = :agentName ORDER BY game_id DESC")
    suspend fun getGamesWithAgent(agentName: String): List<Game>

    @Query("SELECT * FROM game_table WHERE delete_flag = 0 AND result = ${GameResult.WIN} ORDER BY game_id DESC")
    suspend fun getWonGames(): List<Game>

    @Query("SELECT * FROM game_table WHERE delete_flag = 0 AND result = ${GameResult.LOSE} ORDER BY game_id DESC")
    suspend fun getLostGames(): List<Game>

    @Query("SELECT * FROM game_table WHERE delete_flag = 0 AND result = ${GameResult.DRAW} ORDER BY game_id DESC")
    suspend fun getDrawGames(): List<Game>
}