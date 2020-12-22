package com.example.valorantstattracker.gameentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.valorantstattracker.objects.Agent
import com.example.valorantstattracker.ExposedDropDownMenu
import com.example.valorantstattracker.objects.GameResult
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.FragmentGameEntryBinding
import com.example.valorantstattracker.games.GamesViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

private const val AGENT_KEY = "AGENT"
private const val RESULT_KEY = "RESULT"

class GameEntryFragment : Fragment() {

    private lateinit var gameEntryViewModel: GameEntryViewModel
    private lateinit var binding: FragmentGameEntryBinding
    private lateinit var dropdownMenus: List<ExposedDropDownMenu>
    private lateinit var textInputs: List<TextInputEditText>
    private lateinit var textInputSetters: Map<TextInputEditText, (String) -> Unit>
    private lateinit var textInputValidators: Map<TextInputEditText, LiveData<Boolean>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentGameEntryBinding.inflate(inflater)

        //fillInputsWithSavedBundle(savedInstanceState)

        val gameEntryViewModelFactory = createGameEntryViewModelFactory()
        gameEntryViewModel = ViewModelProvider(this, gameEntryViewModelFactory)
            .get(GameEntryViewModel::class.java)



        textInputSetters = mapOf(binding.combatScoreInput to fun (inputText: String) { gameEntryViewModel.setCombatScore(inputText) },
             binding.killsInput to fun (inputText: String) { gameEntryViewModel.setKills(inputText) },
             binding.deathsInput to fun (inputText: String) { gameEntryViewModel.setDeaths(inputText) },
             binding.assistsInput to fun (inputText: String) { gameEntryViewModel.setAssists(inputText) },
             binding.econRatingInput to fun (inputText: String) { gameEntryViewModel.setEconRating(inputText) },
             binding.firstBloodsInput to fun (inputText: String) { gameEntryViewModel.setFirstBloods(inputText) },
             binding.plantsInput to fun (inputText: String) { gameEntryViewModel.setPlants(inputText) },
             binding.defusesInput to fun (inputText: String) { gameEntryViewModel.setDefuses(inputText) }
        )

        textInputValidators = mapOf(binding.combatScoreInput to gameEntryViewModel.combatScoreIsValid,
            binding.killsInput to gameEntryViewModel.killsIsValid ,
            binding.deathsInput to gameEntryViewModel.deathsIsValid,
            binding.assistsInput to gameEntryViewModel.assistsIsValid,
            binding.econRatingInput to gameEntryViewModel.econRatingIsValid,
            binding.firstBloodsInput to gameEntryViewModel.firstBloodsIsValid,
            binding.plantsInput to gameEntryViewModel.plantsIsValid,
            binding.defusesInput to gameEntryViewModel.defusesIsValid)

        // TODO: Later -> Get data from view model when screen orientation changes

        prepareForDataRetrieval()
        setUpConfirmButton()

        return binding.root
    }

    private fun createGameEntryViewModelFactory(): GameEntryViewModelFactory {
        val application = requireNotNull(activity).application
        val dataSource = GameDatabase.getInstance(application).getGameDao()
        return GameEntryViewModelFactory(dataSource, application)
    }

    private fun fillInputsWithSavedBundle(savedInstanceState: Bundle?) {
        savedInstanceState?.let { binding.agentMenu.setText(it.getString(AGENT_KEY)) }
        savedInstanceState?.let { binding.gameResultMenu.setText(it.getString(RESULT_KEY)) }
    }

    private fun prepareForDataRetrieval() {
        dropdownMenus = listOf(binding.agentMenu, binding.gameResultMenu)
        textInputs = listOf(binding.combatScoreInput,
            binding.killsInput,
            binding.deathsInput,
            binding.assistsInput,
            binding.econRatingInput,
            binding.firstBloodsInput,
            binding.plantsInput,
            binding.defusesInput)
        setUpAgentMenu()
        setUpResultMenu()
        setUpTextInput(binding.combatScoreInput)
        setUpTextInput(binding.killsInput)
        setUpTextInput(binding.deathsInput)
        setUpTextInput(binding.assistsInput)
        setUpTextInput(binding.econRatingInput)
        setUpTextInput(binding.firstBloodsInput)
        setUpTextInput(binding.plantsInput)
        setUpTextInput(binding.defusesInput)
//        for (editText in textInputs) {
//            setUpTextInput(editText)
//        }
    }

    private fun setUpAgentMenu() {
        binding.agentMenu.setText(gameEntryViewModel.getAgentName())
        val agentAdapter =
            ArrayAdapter(requireContext(), R.layout.text_list_item, Agent.getAgentList())
        binding.agentMenu.setAdapter(agentAdapter)
        binding.agentMenu.doOnTextChanged { inputText, _, _, _ ->
            gameEntryViewModel.setAgentName(inputText.toString())
        }
        gameEntryViewModel.agentNameIsValid.observe(viewLifecycleOwner, { isValid ->
            if (isValid) {
                binding.agentMenu.error = null
            } else {
                binding.agentMenu.error = resources.getString(R.string.error_input_required)
            }
        })
    }

    private fun setUpResultMenu() {
        binding.gameResultMenu.setText(gameEntryViewModel.getGameResult())
        val resultList = listOf(
            resources.getString(R.string.win),
            resources.getString(R.string.lose),
            resources.getString(R.string.draw)
        )
        val resultAdapter = ArrayAdapter(requireContext(), R.layout.text_list_item, resultList)
        binding.gameResultMenu.setAdapter(resultAdapter)
        binding.gameResultMenu.doOnTextChanged { inputText, _, _, _ ->
            gameEntryViewModel.setGameResult(inputText.toString())
        }
        gameEntryViewModel.gameResultIsValid.observe(viewLifecycleOwner, { isValid ->
            if (isValid) {
                binding.gameResultMenu.error = null
            } else {
                binding.gameResultMenu.error = resources.getString(R.string.error_input_required)
            }
        })
    }

    private fun setUpTextInput(editText: TextInputEditText) {
        editText.doOnTextChanged { inputText, _, _ ,_ ->
            textInputSetters[editText]?.let { setTextInViewModel ->
                setTextInViewModel(inputText.toString())
            }
        }
        textInputValidators[editText]?.observe(viewLifecycleOwner, { isValid ->
            if (isValid) {
                editText.error = null
            } else {
                editText.error = resources.getString((R.string.error_number_required))
            }
        })


//        binding.combatScoreInput.doAfterTextChanged {
//            gameEntryViewModel.setCombatScore(it.toString())
//        }
//        gameEntryViewModel.combatScoreIsValid.observe(viewLifecycleOwner, { isValid ->
//            if (isValid) {
//                binding.combatScoreInput.error = null
//            } else {
//                binding.combatScoreInput.error = resources.getString((R.string.error_number_required))
//            }
//        })

    }



//    private fun setUpTextInput(editText: TextInputEditText) {
//        editText.doOnTextChanged { inputText, _, _, _ ->
//            editText.error = makeErrorMessage(inputText)
//        }
//    }

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
        gameEntryViewModel.showSnackbarError.observe(viewLifecycleOwner, { showSnackbar ->
            if (showSnackbar) {
                Snackbar.make(
                    binding.coordinatorLayout,
                    getString(R.string.invalid_data),
                    Snackbar.LENGTH_SHORT
                ).show()
                gameEntryViewModel.showSnackbarErrorComplete()
            }
        })
        gameEntryViewModel.navigateToGamesFragment.observe(viewLifecycleOwner, { navigate ->
            if (navigate) {
                val action = GameEntryFragmentDirections.actionGameEntryToGames()
                findNavController().navigate(action)
                gameEntryViewModel.navigateToGamesFragmentComplete()
            }
        })
        binding.confirmButton.setOnClickListener {
            gameEntryViewModel.attemptConfirmation()
//            val hasInvalidInputs = displayAnyInputErrors()
//
//            if (hasInvalidInputs) {
//                Snackbar.make(
//                    binding.coordinatorLayout,
//                    getString(R.string.invalid_data),
//                    Snackbar.LENGTH_SHORT
//                ).show()
//            } else {
//                val newGame = makeNewGame() ?: return@setOnClickListener
//                val action = GameEntryFragmentDirections.actionGameEntryToGames(newGame)
//                findNavController().navigate(action)
//            }
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