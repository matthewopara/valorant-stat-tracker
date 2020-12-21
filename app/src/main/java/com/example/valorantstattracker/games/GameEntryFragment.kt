package com.example.valorantstattracker.games

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valorantstattracker.Agent
import com.example.valorantstattracker.ExposedDropDownMenu
import com.example.valorantstattracker.GameResult
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.databinding.FragmentGameEntryBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

private const val AGENT_KEY = "AGENT"
private const val RESULT_KEY = "RESULT"

class GameEntryFragment : Fragment() {

    private lateinit var binding: FragmentGameEntryBinding
    private lateinit var dropdownMenus: List<ExposedDropDownMenu>
    private lateinit var textInputs: List<TextInputEditText>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentGameEntryBinding.inflate(inflater)
        prepareForDataRetrieval(savedInstanceState)
        setUpConfirmButton()

        return binding.root
    }

    private fun prepareForDataRetrieval(savedInstanceState: Bundle?) {
        dropdownMenus = listOf(binding.agentMenu, binding.gameResultMenu)
        textInputs = listOf(binding.combatScoreInput,
            binding.killsInput,
            binding.deathsInput,
            binding.assistsInput,
            binding.econRatingInput,
            binding.firstBloodsInput,
            binding.plantsInput,
            binding.defusesInput)
        setUpAgentMenu(savedInstanceState)
        setUpResultMenu(savedInstanceState)
        for (editText in textInputs) {
            setUpTextInput(editText)
        }
    }

    private fun setUpAgentMenu(savedInstanceState: Bundle?) {
        savedInstanceState?.let { binding.agentMenu.setText(it.getString(AGENT_KEY)) }
        val agentAdapter =
            ArrayAdapter(requireContext(), R.layout.text_list_item, Agent.getAgentList())
        binding.agentMenu.setAdapter(agentAdapter)
        binding.agentMenu.doAfterTextChanged {
            binding.agentMenu.error = null
        }
    }

    private fun setUpResultMenu(savedInstanceState: Bundle?) {
        savedInstanceState?.let { binding.gameResultMenu.setText(it.getString(RESULT_KEY)) }
        val resultList = listOf(
            resources.getString(R.string.win),
            resources.getString(R.string.lose),
            resources.getString(R.string.draw)
        )
        val resultAdapter = ArrayAdapter(requireContext(), R.layout.text_list_item, resultList)
        binding.gameResultMenu.setAdapter(resultAdapter)
        binding.gameResultMenu.doAfterTextChanged {
            binding.gameResultMenu.error = null
        }
    }

    private fun setUpTextInput(editText: TextInputEditText) {
        editText.doOnTextChanged { inputText, _, _, _ ->
            editText.error = makeErrorMessage(inputText)
        }
    }

    private fun makeErrorMessage(inputText: CharSequence?): String? {
        val isInvalidInput = inputText?.any { !it.isDigit() }
        val isNoInput = inputText?.isEmpty()

        return when {
            isInvalidInput == true -> {
                getString(R.string.error_number_required)
            }
            isNoInput == true -> {
                getString(R.string.error_input_required)
            }
            else -> {
                null
            }
        }
    }

    private fun setUpConfirmButton() {
        binding.confirmButton.setOnClickListener {
            val hasInvalidInputs = displayAnyInputErrors()

            if (hasInvalidInputs) {
                Snackbar.make(
                    binding.coordinatorLayout,
                    getString(R.string.invalid_data),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val newGame = makeNewGame() ?: return@setOnClickListener
                val action = GameEntryFragmentDirections.actionGameEntryToGames(newGame)
                findNavController().navigate(action)
            }
        }
    }

    private fun displayAnyInputErrors(): Boolean {
        var hasErrors = false
        for (menu in dropdownMenus) {
            if (menu.text.toString().isEmpty()) {
                hasErrors = true
                menu.error = getString(R.string.error_input_required)
            }
        }
        for (editText in textInputs) {
            if (editText.error != null) {
                hasErrors = true
            } else if (editText.text.toString().isEmpty()) {
                hasErrors = true
                editText.error = getString(R.string.error_input_required)
            }
        }
        return hasErrors
    }

    private fun makeNewGame(): Game? {
        val gameResult =
            GameResult.convertResultTextToInt(binding.gameResultMenu.text.toString(), resources)
                ?: return null

        return Game(result = gameResult,
            agentName = binding.agentMenu.text.toString(),
            combatScore = binding.combatScoreInput.text.toString().toInt(),
            kills = binding.killsInput.text.toString().toInt(),
            deaths = binding.deathsInput.text.toString().toInt(),
            assists = binding.assistsInput.text.toString().toInt(),
            econRating = binding.econRatingInput.text.toString().toInt(),
            firstBloods = binding.firstBloodsInput.text.toString().toInt(),
            plants = binding.plantsInput.text.toString().toInt(),
            defuses = binding.defusesInput.text.toString().toInt())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveMenuTextsToBundle(outState)
    }

    private fun saveMenuTextsToBundle(outState: Bundle) {
        outState.putString(AGENT_KEY, binding.agentMenu.text.toString())
        outState.putString(RESULT_KEY, binding.gameResultMenu.text.toString())
    }
}