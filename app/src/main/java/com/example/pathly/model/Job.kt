package com.example.pathly.model

data class Job(
    val jobId: String = "",
    val title: String = "",
    val company: String = "",
    val description: String = "",
    val skillsRequired: List<String> = emptyList(),
    val location: String = "",
    val applyDeadline: String = ""
) 