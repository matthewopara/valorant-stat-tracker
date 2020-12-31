package com.example.valorantstattracker.insights

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InsightsViewModel : ViewModel() {
    private val _trackers = MutableLiveData<List<GameResultTracker>>()
    val trackers: LiveData<List<GameResultTracker>>
        get() = _trackers

    fun setTrackers(trackerList: List<GameResultTracker>) {
        _trackers.value = trackerList
    }
}