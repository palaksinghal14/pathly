package com.example.pathly.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathly.model.Job
import com.example.pathly.model.UserProfile
import com.example.pathly.repository.ApplicationRepository
import com.example.pathly.repository.JobRepository
import com.example.pathly.utils.ResumeGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "JobViewModel"

@HiltViewModel
class JobViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val applicationRepository: ApplicationRepository,
    private val resumeGenerator: ResumeGenerator
) : ViewModel() {

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _generatedResume = MutableStateFlow<String?>(null)
    val generatedResume: StateFlow<String?> = _generatedResume.asStateFlow()

    private val _applicationStatus = MutableStateFlow<ApplicationStatus?>(null)
    val applicationStatus: StateFlow<ApplicationStatus?> = _applicationStatus.asStateFlow()

    sealed class ApplicationStatus {
        data class Success(val message: String) : ApplicationStatus()
        data class Error(val message: String) : ApplicationStatus()
    }

    fun loadJobs(userProfile: UserProfile? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val result = if (userProfile != null) {
                    jobRepository.getFilteredJobs(userProfile)
                } else {
                    jobRepository.getAllJobs()
                }

                result
                    .onSuccess { jobList ->
                        _jobs.value = jobList
                        Log.d(TAG, "Successfully loaded ${jobList.size} jobs")
                    }
                    .onFailure { error ->
                        _error.value = "Failed to load jobs: ${error.message}"
                        Log.e(TAG, "Error loading jobs", error)
                    }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun generateResumeForJob(job: Job, userProfile: UserProfile) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _generatedResume.value = null

                val resume = resumeGenerator.generateResumeForJob(userProfile, job.description)
                _generatedResume.value = resume
                Log.d(TAG, "Successfully generated resume for job: ${job.title}")
            } catch (e: Exception) {
                _error.value = "Failed to generate resume: ${e.message}"
                Log.e(TAG, "Error generating resume", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun autoApply(job: Job, userProfile: UserProfile) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _applicationStatus.value = null

                val resume = resumeGenerator.generateResumeForJob(userProfile, job.description)
                
                applicationRepository.saveApplication(job, resume)
                    .onSuccess {
                        _applicationStatus.value = ApplicationStatus.Success(
                            "Successfully applied to ${job.title} at ${job.company} with AI-optimized resume"
                        )
                        Log.d(TAG, "Successfully applied to job: ${job.title}")
                    }
                    .onFailure { error ->
                        _applicationStatus.value = ApplicationStatus.Error(
                            "Failed to apply: ${error.message}"
                        )
                        Log.e(TAG, "Error applying to job", error)
                    }
            } catch (e: Exception) {
                _applicationStatus.value = ApplicationStatus.Error(
                    "Failed to apply: ${e.message}"
                )
                Log.e(TAG, "Error auto-applying", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearGeneratedResume() {
        _generatedResume.value = null
    }

    fun clearApplicationStatus() {
        _applicationStatus.value = null
    }
} 