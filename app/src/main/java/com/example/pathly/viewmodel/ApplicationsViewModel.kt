package com.example.pathly.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathly.model.AppliedJob
import com.example.pathly.repository.ApplicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ApplicationsViewModel"

@HiltViewModel
class ApplicationsViewModel @Inject constructor(
    private val repository: ApplicationRepository
) : ViewModel() {

    private val _applications = MutableStateFlow<List<AppliedJob>>(emptyList())
    val applications: StateFlow<List<AppliedJob>> = _applications.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedApplication = MutableStateFlow<AppliedJob?>(null)
    val selectedApplication: StateFlow<AppliedJob?> = _selectedApplication.asStateFlow()

    init {
        loadApplications()
    }

    fun loadApplications() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                repository.getUserApplications()
                    .onSuccess { applications ->
                        _applications.value = applications
                        Log.d(TAG, "Successfully loaded ${applications.size} applications")
                    }
                    .onFailure { error ->
                        _error.value = "Failed to load applications: ${error.message}"
                        Log.e(TAG, "Error loading applications", error)
                    }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectApplication(application: AppliedJob) {
        _selectedApplication.value = application
    }

    fun clearSelectedApplication() {
        _selectedApplication.value = null
    }

    fun clearError() {
        _error.value = null
    }
} 