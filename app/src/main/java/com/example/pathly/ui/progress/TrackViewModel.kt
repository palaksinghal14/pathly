package com.example.pathly.ui.progress

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathly.model.AppliedJob
import com.example.pathly.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TrackViewModel"

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val repository: JobRepository
) : ViewModel() {

    private val _appliedJobs = MutableStateFlow<List<AppliedJob>>(emptyList())
    val appliedJobs: StateFlow<List<AppliedJob>> = _appliedJobs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAppliedJobs()
    }

    fun loadAppliedJobs() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                repository.getAppliedJobs()
                    .onSuccess { jobs ->
                        _appliedJobs.value = jobs.sortedByDescending { it.timestamp }
                        Log.d(TAG, "Successfully loaded ${jobs.size} applied jobs")
                    }
                    .onFailure { error ->
                        _error.value = "Failed to load applications: ${error.message}"
                        Log.e(TAG, "Error loading applied jobs", error)
                    }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
} 