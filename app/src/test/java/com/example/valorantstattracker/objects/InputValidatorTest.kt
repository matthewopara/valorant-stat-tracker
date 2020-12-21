package com.example.valorantstattracker.objects

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
}