package com.example.pathly.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathly.model.UserProfile
import com.example.pathly.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    sealed class SaveResult {
        object Success : SaveResult()
        data class Error(val message: String) : SaveResult()
    }

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _saveResult = MutableStateFlow<SaveResult?>(null)
    val saveResult: StateFlow<SaveResult?> = _saveResult.asStateFlow()

    init {
        if (repository.isUserLoggedIn()) {
            loadUserProfile()
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                repository.getUserProfile()
                    .fold(
                        onSuccess = { profile ->
                            _userProfile.value = profile
                            Log.d(TAG, "Successfully loaded user profile")
                        },
                        onFailure = { error ->
                            _error.value = "Failed to load profile: ${error.message}"
                            Log.e(TAG, "Error loading profile", error)
                        }
                    )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _saveResult.value = null

                repository.saveUserProfile(profile)
                    .fold(
                        onSuccess = {
                            _userProfile.value = profile
                            _saveResult.value = SaveResult.Success
                            Log.d(TAG, "Successfully saved user profile")
                        },
                        onFailure = { error ->
                            val errorMessage = "Failed to save profile: ${error.message}"
                            _error.value = errorMessage
                            _saveResult.value = SaveResult.Error(errorMessage)
                            Log.e(TAG, "Error saving profile", error)
                        }
                    )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearSaveResult() {
        _saveResult.value = null
    }

    fun isProfileComplete(): Boolean = _userProfile.value?.isProfileComplete() ?: false

    fun getProfileCompletionPercentage(): Float = _userProfile.value?.getCompletionPercentage() ?: 0f
} 