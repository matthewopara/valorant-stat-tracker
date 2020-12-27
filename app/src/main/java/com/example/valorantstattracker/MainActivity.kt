package com.example.valorantstattracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.ActivityMainBinding
import com.example.valorantstattracker.games.GamesFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val topLevelDestinations = setOf(R.id.gamesFragment, R.id.insightsFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)
        deleteFlaggedGames()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val navController = Navigation.findNavController(binding.navHostFragment)
                when (tab?.text) {
                    resources.getString(R.string.games) -> {
                        val action = InsightsFragmentDirections.actionInsightsToGames()
                        navController.navigate(action)
                    }
                    resources.getString(R.string.insights) -> {
                        val action = GamesFragmentDirections.actionGamesToInsights()
                        navController.navigate(action)
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // TODO: Implement
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val navController = Navigation.findNavController(binding.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun deleteFlaggedGames() {
        val gameDao = GameDatabase.getInstance(this).getGameDao()
        CoroutineScope(Dispatchers.IO).launch {
            gameDao.deleteFlaggedGames()
        }
    }

    fun getFloatingActionButton(): FloatingActionButton = binding.floatingActionButton

    fun getTabLayout(): TabLayout = binding.tabLayout
}