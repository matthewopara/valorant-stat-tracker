package com.example.valorantstattracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.valorantstattracker.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)
    }

    fun showAppBarLayout() {
        binding.appBarLayout.setExpanded(true)
    }

    fun hideAppBarLayout() {
        binding.appBarLayout.setExpanded(false)
    }

    fun getFloatingActionButton(): FloatingActionButton = binding.floatingActionButton
}