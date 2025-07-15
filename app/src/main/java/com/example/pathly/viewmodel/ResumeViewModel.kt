package com.example.pathly.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathly.model.UserProfile
import com.example.pathly.utils.ResumeGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private const val TAG = "ResumeViewModel"

@HiltViewModel
class ResumeViewModel @Inject constructor(
    private val resumeGenerator: ResumeGenerator
) : ViewModel() {

    var resumeText by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun generateResume(userProfile: UserProfile) {
        viewModelScope.launch {
            try {
                isLoading = true
                _error.value = null
                resumeText = resumeGenerator.generateBasicResume(userProfile)
            } catch (e: IOException) {
                Log.e(TAG, "Error generating resume", e)
                _error.value = "Failed to generate resume: ${e.message}"
                resumeText = null
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
} 