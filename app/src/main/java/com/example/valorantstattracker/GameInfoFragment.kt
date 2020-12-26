package com.example.valorantstattracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.valorantstattracker.databinding.FragmentGameInfoBinding
import com.example.valorantstattracker.objects.Agent
import com.example.valorantstattracker.objects.GameResult

class GameInfoFragment : Fragment() {

    private lateinit var binding: FragmentGameInfoBinding
    private val args: GameInfoFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentGameInfoBinding.inflate(inflater)
        val agentImage = Agent.getImageResource(args.game.agentName)
        agentImage?.let {
            binding.infoAgentImage.setImageResource(agentImage)
        }
        binding.infoGameResult.text = GameResult.convertIntToResultString(args.game.result, resources)
        binding.infoCombatScore.text = args.game.combatScore.toString()
        return binding.root
    }
}