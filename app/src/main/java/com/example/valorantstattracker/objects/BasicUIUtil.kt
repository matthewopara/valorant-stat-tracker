package com.example.valorantstattracker.objects

import androidx.appcompat.app.AppCompatActivity
import com.example.valorantstattracker.MainActivity

object BasicUIUtil {
    fun setActionbarTitle(activity: AppCompatActivity, title: String) {
        activity.supportActionBar?.title = title
    }

    fun showFloatingActionButton(activity: MainActivity) {
        activity.getFloatingActionButton().show()
    }

    fun hideFloatingActionButton(activity: MainActivity) {
        activity.getFloatingActionButton().hide()
    }

    fun setFloatingActionButtonListener(activity: MainActivity, listener: () -> Unit) {
        activity.getFloatingActionButton().setOnClickListener {
            listener()
        }
    }
}