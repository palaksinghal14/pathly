package com.example.pathly.model

import java.util.Date

data class AppliedJob(
    val userId: String = "",
    val jobId: String = "",
    val title: String = "",
    val company: String = "",
    val timestamp: Date = Date(),
    val resumeText: String = ""
)

enum class ApplicationStatus {
    APPLIED, SHORTLISTED, REJECTED, INTERVIEW_SCHEDULED;

    fun getColor(): Long {
        return when (this) {
            APPLIED -> 0xFF2196F3 // Blue
            SHORTLISTED -> 0xFF4CAF50 // Green
            REJECTED -> 0xFFF44336 // Red
            INTERVIEW_SCHEDULED -> 0xFF9C27B0 // Purple
        }
    }
} 