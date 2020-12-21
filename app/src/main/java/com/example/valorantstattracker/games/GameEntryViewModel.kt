package com.example.valorantstattracker.games

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.valorantstattracker.InputHolder
import com.example.valorantstattracker.database.GameDao
import com.example.valorantstattracker.objects.GameResult
import com.example.valorantstattracker.objects.InputValidator

class GameEntryViewModel(private val gameDao: GameDao,
                         application: Application) : AndroidViewModel(application) {

    val inputHolder = InputHolder()

    val _agentNameIsValid = MutableLiveData(true)
    val agentNameIsValid: LiveData<Boolean>
        get() = _agentNameIsValid

    val _gameResultIsValid = MutableLiveData(true)
    val gameResultIsValid: LiveData<Boolean>
        get() = _gameResultIsValid

    val _combatScoreIsValid = MutableLiveData(true)
    val combatScoreIsValid: LiveData<Boolean>
        get() = _combatScoreIsValid

    val _killsIsValid = MutableLiveData(true)
    val killsIsValid: LiveData<Boolean>
        get() = _killsIsValid

    val _deathsIsValid = MutableLiveData(true)
    val deathsIsValid: LiveData<Boolean>
        get() = _deathsIsValid

    val _assistsIsValid = MutableLiveData(true)
    val assistsIsValid: LiveData<Boolean>
        get() = _assistsIsValid

    val _econRatingIsValid = MutableLiveData(true)
    val econRatingIsValid: LiveData<Boolean>
        get() = _econRatingIsValid

    val _firstBloodsIsValid = MutableLiveData(true)
    val firstBloodsIsValid: LiveData<Boolean>
        get() = _firstBloodsIsValid

    val _plantsIsValid = MutableLiveData(true)
    val plantsIsValid: LiveData<Boolean>
        get() = _plantsIsValid

    val _defusesIsValid = MutableLiveData(true)
    val defusesIsValid: LiveData<Boolean>
        get() = _defusesIsValid

    val _showSnackbarError = MutableLiveData(false)
    val showSnackbarError: LiveData<Boolean>
        get() = _showSnackbarError

    val _navigateToGamesFragment = MutableLiveData(false)
    val navigateToGamesFragment: LiveData<Boolean>
        get() = _navigateToGamesFragment


    fun setAgentName(input: String) {
        inputHolder.agentNameInput = input
        _agentNameIsValid.value = InputValidator.isAgentName(input)
    }

    fun setGameResult(input: String) {
        inputHolder.gameResultInput = input
        val gameResultInt = GameResult.convertResultTextToInt(input, getApplication<Application>().resources)
        _gameResultIsValid.value = InputValidator.isGameResult(gameResultInt)
    }

    fun setCombatScore(input: String) {
        inputHolder.combatScoreInput = input
        _combatScoreIsValid.value = InputValidator.isNumber(input)
    }

    fun setKills(input: String) {
        inputHolder.killsInput = input
        _killsIsValid.value = InputValidator.isNumber(input)
    }

    fun setDeaths(input: String) {
        inputHolder.deathsInput = input
        _deathsIsValid.value = InputValidator.isNumber(input)
    }

    fun setAssists(input: String) {
        inputHolder.assistsInput = input
        _assistsIsValid.value = InputValidator.isNumber(input)
    }

    fun setEconRating(input: String) {
        inputHolder.econRatingInput = input
        _econRatingIsValid.value = InputValidator.isNumber(input)
    }

    fun setFirstBloods(input: String) {
        inputHolder.firstBloodsInput = input
        _firstBloodsIsValid.value = InputValidator.isNumber(input)
    }

    fun setPlants(input: String) {
        inputHolder.plantsInput = input
        _plantsIsValid.value = InputValidator.isNumber(input)
    }

    fun setDefuses(input: String) {
        inputHolder.defusesInput = input
        _defusesIsValid.value = InputValidator.isNumber(input)
    }

    fun attemptConfirmation() {
        if (validateAllInputs()) {
            // TODO: navigate
            _navigateToGamesFragment.value = true
        } else {
            _showSnackbarError.value = true
        }
    }

    private fun validateAllInputs(): Boolean {
        var allInputsAreValid = true

        if (!InputValidator.isAgentName(inputHolder.agentNameInput)) {
            allInputsAreValid = false
            _agentNameIsValid.value = false
        }

        val gameResultInt = GameResult.convertResultTextToInt(
            inputHolder.gameResultInput,
            getApplication<Application>().resources
        )
        if (!InputValidator.isGameResult(gameResultInt)) {
            allInputsAreValid = false
            _gameResultIsValid.value = false
        }

        if (!InputValidator.isNumber(inputHolder.combatScoreInput)) {
            allInputsAreValid = false
            _combatScoreIsValid.value = false
        }

        if (!InputValidator.isNumber(inputHolder.killsInput)) {
            allInputsAreValid = false
            _killsIsValid.value = false
        }

        if (!InputValidator.isNumber(inputHolder.deathsInput)) {
            allInputsAreValid = false
            _deathsIsValid.value = false
        }

        if (!InputValidator.isNumber(inputHolder.assistsInput)) {
            allInputsAreValid = false
            _assistsIsValid.value = false
        }

        if (!InputValidator.isNumber(inputHolder.econRatingInput)) {
            allInputsAreValid = false
            _econRatingIsValid.value = false
        }

        if (!InputValidator.isNumber(inputHolder.firstBloodsInput)) {
            allInputsAreValid = false
            _firstBloodsIsValid.value = false
        }

        if (!InputValidator.isNumber(inputHolder.plantsInput)) {
            allInputsAreValid = false
            _plantsIsValid.value = false
        }

        if (!InputValidator.isNumber(inputHolder.defusesInput)) {
            allInputsAreValid = false
            _defusesIsValid.value = false
        }

        return allInputsAreValid
    }

    fun showSnackbarErrorComplete() {
        _showSnackbarError.value = false
    }

    fun navigateToGamesFragmentComplete() {
        _navigateToGamesFragment.value = false
    }
}