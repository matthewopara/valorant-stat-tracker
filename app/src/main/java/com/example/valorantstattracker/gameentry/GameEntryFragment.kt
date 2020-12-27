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
import com.example.valorantstattracker.MainActivity
import com.example.valorantstattracker.objects.Agent
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.FragmentGameEntryBinding
import com.example.valorantstattracker.objects.BasicUIUtil
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class GameEntryFragment : Fragment() {

    private lateinit var gameEntryViewModel: GameEntryViewModel
    private lateinit var binding: FragmentGameEntryBinding
    private lateinit var textInputSetters: Map<TextInputEditText, (String) -> Unit>
    private lateinit var textInputValidators: Map<TextInputEditText, LiveData<Boolean>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentGameEntryBinding.inflate(inflater)

        val gameEntryViewModelFactory = createGameEntryViewModelFactory()
        gameEntryViewModel = ViewModelProvider(this, gameEntryViewModelFactory)
            .get(GameEntryViewModel::class.java)

        BasicUIUtil.hideFloatingActionButton(requireActivity() as MainActivity)
        BasicUIUtil.makeTabLayoutGone(requireActivity() as MainActivity)
        prepareForDataRetrieval()
        setUpConfirmButton()

        return binding.root
    }

    private fun createGameEntryViewModelFactory(): GameEntryViewModelFactory {
        val application = requireNotNull(activity).application
        val dataSource = GameDatabase.getInstance(application).getGameDao()
        return GameEntryViewModelFactory(dataSource, application)
    }

    private fun prepareForDataRetrieval() {
        assignTextInputSetters()
        assignTextInputValidators()
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
    }

    private fun assignTextInputSetters() {
        textInputSetters = mapOf(binding.combatScoreInput to fun (inputText: String) { gameEntryViewModel.setCombatScore(inputText) },
            binding.killsInput to fun (inputText: String) { gameEntryViewModel.setKills(inputText) },
            binding.deathsInput to fun (inputText: String) { gameEntryViewModel.setDeaths(inputText) },
            binding.assistsInput to fun (inputText: String) { gameEntryViewModel.setAssists(inputText) },
            binding.econRatingInput to fun (inputText: String) { gameEntryViewModel.setEconRating(inputText) },
            binding.firstBloodsInput to fun (inputText: String) { gameEntryViewModel.setFirstBloods(inputText) },
            binding.plantsInput to fun (inputText: String) { gameEntryViewModel.setPlants(inputText) },
            binding.defusesInput to fun (inputText: String) { gameEntryViewModel.setDefuses(inputText) }
        )
    }

    private fun assignTextInputValidators() {
        textInputValidators = mapOf(binding.combatScoreInput to gameEntryViewModel.combatScoreIsValid,
            binding.killsInput to gameEntryViewModel.killsIsValid ,
            binding.deathsInput to gameEntryViewModel.deathsIsValid,
            binding.assistsInput to gameEntryViewModel.assistsIsValid,
            binding.econRatingInput to gameEntryViewModel.econRatingIsValid,
            binding.firstBloodsInput to gameEntryViewModel.firstBloodsIsValid,
            binding.plantsInput to gameEntryViewModel.plantsIsValid,
            binding.defusesInput to gameEntryViewModel.defusesIsValid)
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
    }

    private fun setUpConfirmButton() {
        observeSnackbarError()
        observeNavigateToGamesFragment()
        binding.confirmButton.setOnClickListener {
            gameEntryViewModel.attemptConfirmation()
        }
    }

    private fun observeSnackbarError() {
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
    }

    private fun observeNavigateToGamesFragment() {
        gameEntryViewModel.navigateToGamesFragment.observe(viewLifecycleOwner, { navigate ->
            if (navigate) {
                val action = GameEntryFragmentDirections.actionGameEntryToGames()
                findNavController().navigate(action)
                gameEntryViewModel.navigateToGamesFragmentComplete()
            }
        })
    }
}