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

class GameEntryFragment : Fragment() {

    private lateinit var binding: FragmentGameEntryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentGameEntryBinding.inflate(inflater)
        prepareForDataRetrieval()
        return binding.root
    }

    private fun prepareForDataRetrieval() {
        setUpAgentMenu()
        setUpResultMenu()
        setUpCombatScoreInput()
        setUpKillsInput()
    }

    private fun setUpAgentMenu() {
        val agentAdapter =
            ArrayAdapter(requireContext(), R.layout.text_list_item, Agent.getAgentList())
        binding.agentMenu.setAdapter(agentAdapter)
    }

    private fun setUpResultMenu() {
        val resultList = listOf(
            resources.getString(R.string.win),
            resources.getString(R.string.lose),
            resources.getString(R.string.draw)
        )
        val resultAdapter = ArrayAdapter(requireContext(), R.layout.text_list_item, resultList)
        binding.gameResultMenu.setAdapter(resultAdapter)
    }

    private fun setUpCombatScoreInput() {
        binding.combatScoreInput.doOnTextChanged { inputText, _, _, _ ->
            binding.combatScoreInput.error = makeErrorMessage(inputText)
        }
    }

    private fun setUpKillsInput() {
        binding.killsInput.doOnTextChanged { inputText, _, _, _ ->
            binding.killsInput.error = makeErrorMessage(inputText)
        }
    }

    private fun makeErrorMessage(inputText: CharSequence?): String? {
        val isInvalidInput = inputText?.any { !it.isDigit() }
        if (isInvalidInput == true) {
            return getString(R.string.error_int)
        } else {
            return null
        }
    }
}