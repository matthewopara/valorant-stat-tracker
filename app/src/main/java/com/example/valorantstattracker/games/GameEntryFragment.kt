package com.example.valorantstattracker.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.valorantstattracker.Agent
import com.example.valorantstattracker.R
import com.example.valorantstattracker.databinding.FragmentGameEntryBinding
import com.google.android.material.textfield.TextInputEditText

private const val AGENT_KEY = "AGENT"
private const val RESULT_KEY = "RESULT"

class GameEntryFragment : Fragment() {

    private lateinit var binding: FragmentGameEntryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentGameEntryBinding.inflate(inflater)
        prepareForDataRetrieval(savedInstanceState)

        return binding.root
    }

    private fun prepareForDataRetrieval(savedInstanceState: Bundle?) {
        setUpAgentMenu(savedInstanceState)
        setUpResultMenu(savedInstanceState)
        setUpTextInput(binding.combatScoreInput)
        setUpTextInput(binding.killsInput)
        setUpTextInput(binding.deathsInput)
        setUpTextInput(binding.assistsInput)
        setUpTextInput(binding.econRatingInput)
        setUpTextInput(binding.firstBloodsInput)
        setUpTextInput(binding.defusesInput)
        setUpTextInput(binding.plantsInput)
    }

    private fun setUpAgentMenu(savedInstanceState: Bundle?) {
        savedInstanceState?.let { binding.agentMenu.setText(it.getString(AGENT_KEY)) }
        val agentAdapter =
            ArrayAdapter(requireContext(), R.layout.text_list_item, Agent.getAgentList())
        binding.agentMenu.setAdapter(agentAdapter)
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
    }

    private fun setUpTextInput(editText: TextInputEditText) {
        editText.doOnTextChanged { inputText, _, _, _ ->
            editText.error = makeErrorMessage(inputText)
        }
    }

    private fun makeErrorMessage(inputText: CharSequence?): String? {
        val isInvalidInput = inputText?.any { !it.isDigit() }
        return if (isInvalidInput == true) {
            getString(R.string.error_int)
        } else {
            null
        }
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