package com.example.valorantstattracker.objects

object InputValidator {
    fun isNumber(input: String?): Boolean {
        val isInvalid = input?.any { !it.isDigit() }
        return !(isInvalid == true || isInvalid == null)
    }

    fun isAgentName(input: String?): Boolean {
        Agent.getAgentList().forEach { agentName ->
            if (agentName == input) {
                return true
            }
        }
        return false
    }

    fun isGameResult(input: Int?): Boolean {
        return input == GameResult.WIN || input == GameResult.LOSE || input == GameResult.DRAW
    }
}