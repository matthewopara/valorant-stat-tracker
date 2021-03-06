package com.example.valorantstattracker.objects

import android.app.Application
import android.content.res.Resources
import androidx.test.core.app.ApplicationProvider
import com.example.valorantstattracker.R
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class GameResultTest {

    private lateinit var resources: Resources

    @Before
    fun setUp() {
        resources = ApplicationProvider.getApplicationContext<Application>().resources
    }

    @Test
    fun convertValidResultTextToInt() {
        val resultInt = GameResult.convertResultStringToInt("Win", resources)
        assertEquals(GameResult.WIN, resultInt)
    }

    @Test
    fun convertinValidResultTextToInt() {
        val resultInt = GameResult.convertResultStringToInt("Hello", resources)
        assertNull(resultInt)
    }

    @Test
    fun convertValidIntToResultText() {
        val resultString = GameResult.convertIntToResultString(GameResult.WIN, resources)
        assertEquals(resources.getString(R.string.win), resultString)
    }

    @Test
    fun convertinvalidIntToResultText() {
        val resultString = GameResult.convertIntToResultString(42, resources)
        assertNull(resultString)
    }
}