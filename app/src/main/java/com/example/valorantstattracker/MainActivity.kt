package com.example.valorantstattracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.ActivityMainBinding
import com.example.valorantstattracker.games.GamesFragmentDirections
import com.example.valorantstattracker.insights.InsightsFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SELECTED_TAB = "selected_tab"
    }

    private lateinit var binding: ActivityMainBinding
    private val topLevelDestinations = setOf(R.id.gamesFragment, R.id.insightsFragment)
    private var rotated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)
        deleteFlaggedGames()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (rotated) {
                    rotated = false
                    return
                }
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

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (rotated) {
                    rotated = false
                    return
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }
        })
    }

    override fun onStart() {
        super.onStart()
        val navController = Navigation.findNavController(binding.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_TAB, binding.tabLayout.selectedTabPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val tabIndex = savedInstanceState.getInt(SELECTED_TAB)
        val selectedTab = binding.tabLayout.getTabAt(tabIndex)
        selectedTab?.let {
            rotated = true
            binding.tabLayout.selectTab(it)
        }
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