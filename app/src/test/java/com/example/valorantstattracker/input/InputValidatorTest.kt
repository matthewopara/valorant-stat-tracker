package com.example.valorantstattracker.input

import com.example.valorantstattracker.objects.Agent
import com.example.valorantstattracker.objects.GameResult
import org.junit.Test

import org.junit.Assert.*

class InputValidatorTest {

    @Test
    fun isNumberTrue() {
        val validation = InputValidator.isNumber("495")
        assertTrue(validation)
    }

    @Test
    fun isNumberFalse() {
        val validation = InputValidator.isNumber("1f3D")
        assertFalse(validation)
    }

    @Test
    fun isAgentNameTrue() {
        val validation = InputValidator.isAgentName(Agent.BREACH)
        assertTrue(validation)
    }

    @Test
    fun isAgentNameFalse() {
        val validation = InputValidator.isAgentName("Scooby-Doo")
        assertFalse(validation)
    }

    @Test
    fun isGameResultTrue() {
        val validation = InputValidator.isGameResult(GameResult.WIN)
        assertTrue(validation)
    }

    @Test
    fun isGameResultFalse() {
        val validation = InputValidator.isGameResult(-495)
        assertFalse(validation)
    }
}