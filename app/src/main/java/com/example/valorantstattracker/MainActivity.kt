package com.example.valorantstattracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.valorantstattracker.database.GameDatabase
import com.example.valorantstattracker.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        val gameDao = GameDatabase.getInstance(this).getGameDao()
        CoroutineScope(Dispatchers.IO).launch {
            gameDao.deleteFlaggedGames()
        }
    }

    fun getFloatingActionButton(): FloatingActionButton = binding.floatingActionButton
}