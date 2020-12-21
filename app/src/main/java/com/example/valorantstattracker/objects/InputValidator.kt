package com.example.valorantstattracker.objects

object InputValidator {
    fun isNumber(input: String?): Boolean {
        val isInvalid = input?.any { !it.isDigit() }
        return !(isInvalid == true || isInvalid == null)
    }
}