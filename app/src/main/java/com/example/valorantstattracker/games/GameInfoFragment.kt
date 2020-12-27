package com.example.valorantstattracker.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.valorantstattracker.GameInfoFragmentArgs
import com.example.valorantstattracker.MainActivity
import com.example.valorantstattracker.R
import com.example.valorantstattracker.database.Game
import com.example.valorantstattracker.databinding.FragmentGameInfoBinding
import com.example.valorantstattracker.objects.Agent
import com.example.valorantstattracker.objects.GameResult
import com.example.valorantstattracker.objects.TimeCalculator

class GameInfoFragment : Fragment() {

    private lateinit var binding: FragmentGameInfoBinding
    private val args: GameInfoFragmentArgs by navArgs()
    private lateinit var game: Game

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentGameInfoBinding.inflate(inflater)
        game = args.game
        setActionbarTitle()
        hideFloatingActionButton()
        displayGameInfo()
        return binding.root
    }

    private fun setActionbarTitle() {
        (activity as AppCompatActivity).supportActionBar?.title = game.agentName
    }

    private fun hideFloatingActionButton() {
        val hostActivity = requireActivity()
        if (hostActivity is MainActivity) {
            hostActivity.getFloatingActionButton().hide()
        }
    }

    private fun displayGameInfo() {
        val agentImage = Agent.getImageResource(game.agentName)
        agentImage?.let {
            binding.infoAgentImage.setImageResource(agentImage)
            binding.infoAgentImage.contentDescription = game.agentName
        }
        binding.infoGameResult.text = GameResult.convertIntToResultString(game.result, resources)
        binding.infoGameResult.setTextColor(GameResult.getGameResultColor(game.result))
        binding.infoCombatScore.text = resources.getString(R.string.combat_score_display, game.combatScore)
        binding.infoEconRating.text = resources.getString(R.string.econ_rating_display, game.econRating)
        binding.infoKills.text = resources.getString(R.string.kills_display, game.kills)
        binding.infoDeaths.text = resources.getString(R.string.deaths_display, game.deaths)
        binding.infoAssists.text = resources.getString(R.string.assists_display, game.assists)
        binding.infoFirstBloods.text = resources.getString(R.string.first_bloods_display, game.firstBloods)
        binding.infoPlants.text = resources.getString(R.string.plants_display, game.plants)
        binding.infoDefuses.text = resources.getString(R.string.defuses_display, game.defuses)
        binding.infoDate.text = TimeCalculator.getDate(game.entryTimeMilli)
        binding.infoTime.text = TimeCalculator.getTime(game.entryTimeMilli)
    }


}