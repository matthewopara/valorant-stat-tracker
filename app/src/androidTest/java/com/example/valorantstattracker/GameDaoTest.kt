package com.example.valorantstattracker

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.database.GameDao
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.objects.Agent
import com.example.valorantstattracker.objects.GameResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.After

import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Test
import java.io.IOException

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class GameDaoTest {
    private lateinit var gameDao: GameDao
    private lateinit var db: GameDatabase
    private lateinit var ioScope: CoroutineScope

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GameDatabase::class.java).build()
        gameDao = db.getGameDao()

        ioScope = CoroutineScope(Dispatchers.IO)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetGame() {
        val id = 1L
        val game  = createGameWithId(id)

        ioScope.launch {
            gameDao.insert(game)
            val returnedGame = gameDao.getGame(id)
            assertEquals(game.gameId, returnedGame?.gameId)
        }
    }

    @Test
    fun insertAndDeleteGame() {
        ioScope.launch {
            val game = createGameWithId(1)
            gameDao.insert(game)
            gameDao.delete(game)
            val searchedGame = gameDao.getGame(1)
            assertNull(searchedGame)
        }
    }

    @Test
    fun clearGames() {
        ioScope.launch {
            for (i in 1..5) {
                gameDao.insert(createGameWithId(i.toLong()))
            }

            gameDao.clear()
            val games = gameDao.getLastXGames(1)
            val sizeOfGames = games.size
            assertEquals(0, sizeOfGames)
        }
    }

    @Test
    fun getLastXGamesReturnsXGames() {
        ioScope.launch {
            for (i in 1..7) {
                gameDao.insert(createGameWithId(i.toLong()))
            }

            val lastGames = gameDao.getLastXGames(4)
            val sizeOfLastGames = lastGames.size
            assertEquals(4, sizeOfLastGames)
        }
    }

    @Test
    fun getLastXGamesReturnsLastGames() {
        ioScope.launch {
            for (i in 1..2) {
                gameDao.insert(createGameWithId(i.toLong()))
            }

            val lastGame = gameDao.getLastXGames(1)
            val lastGameId = lastGame[0].gameId
            assertEquals(2, lastGameId)
        }
    }

    @Test
    fun getGamesWithAgentOnlyReturnsThatAgent() {
        ioScope.launch {
            for (i in 1..3) {
                gameDao.insert(createGameWithAgent(Agent.BREACH))
                gameDao.insert(createGameWithAgent(Agent.OMEN))
            }

            val omenGames = gameDao.getGamesWithAgent(Agent.OMEN)
            val numOfNotOmenGames = omenGames.count { it.agentName != Agent.OMEN }

            assertEquals(0, numOfNotOmenGames)
        }
    }

    @Test
    fun getWonGamesOnlyReturnsWonGames() {
        ioScope.launch {
            for (i in 1..3) {
                gameDao.insert(createGameWithResult(GameResult.WIN))
                gameDao.insert(createGameWithResult(GameResult.LOSE))
                gameDao.insert(createGameWithResult(GameResult.DRAW))
            }

            val wonGames = gameDao.getWonGames()
            val numOfNotWonGames = wonGames.count { it.result != GameResult.WIN }
            assertEquals(0, numOfNotWonGames)
        }
    }

    @Test
    fun getLostGamesOnlyReturnsLostGames() {
        ioScope.launch {
            for (i in 1..3) {
                gameDao.insert(createGameWithResult(GameResult.WIN))
                gameDao.insert(createGameWithResult(GameResult.LOSE))
                gameDao.insert(createGameWithResult(GameResult.DRAW))
            }

            val lostGames = gameDao.getLostGames()
            val numOfNotLostGames = lostGames.count { it.result != GameResult.LOSE }
            assertEquals(0, numOfNotLostGames)
        }
    }

    @Test
    fun getDrawGamesOnlyReturnsDrawGames() {
        ioScope.launch {
            for (i in 1..3) {
                gameDao.insert(createGameWithResult(GameResult.WIN))
                gameDao.insert(createGameWithResult(GameResult.LOSE))
                gameDao.insert(createGameWithResult(GameResult.DRAW))
            }

            val drawGames = gameDao.getDrawGames()
            val numOfNotDrawGames = drawGames.count { it.result != GameResult.DRAW }
            assertEquals(0, numOfNotDrawGames)
        }
    }


    private fun createGameWithId(id: Long): Game {
        return Game(id, 10, GameResult.WIN,
            Agent.BREACH, 100, 20, 20,
            20, 30, 2, 2, 1)
    }

    private fun createGameWithAgent(agentName: String): Game {
        return Game(entryTimeMilli = 10, result = GameResult.WIN,
            agentName = agentName, combatScore = 100, kills = 20, deaths = 20,
            assists = 20, econRating = 30, firstBloods = 2, plants = 2, defuses = 1)
    }

    private fun createGameWithResult(result: Int): Game {
        return Game(entryTimeMilli = 10, result = result,
            agentName = Agent.BREACH, combatScore = 100, kills = 20, deaths = 20,
            assists = 20, econRating = 30, firstBloods = 2, plants = 2, defuses = 1)
    }
}

