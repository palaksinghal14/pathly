package com.example.pathly.model

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val education: Education = Education(),
    val skills: List<String> = emptyList(),
    val projects: List<Project> = emptyList(),
    val accomplishments: String = "",
    val experience: List<Experience> = emptyList(),
    val isComplete: Boolean = false
) {
    fun isProfileComplete(): Boolean {
        return name.isNotBlank() &&
                email.isNotBlank() &&
                phone.isNotBlank() &&
                education.graduationDegree.isNotBlank() &&
                education.graduationInstitute.isNotBlank() &&
                education.graduationYear.isNotBlank() &&
                skills.isNotEmpty() &&
                projects.isNotEmpty() &&
                accomplishments.isNotBlank()
    }

    fun getCompletionPercentage(): Float {
        var completedFields = 0
        var totalFields = 8 // Required fields count

        if (name.isNotBlank()) completedFields++
        if (email.isNotBlank()) completedFields++
        if (phone.isNotBlank()) completedFields++
        if (education.graduationDegree.isNotBlank() &&
            education.graduationInstitute.isNotBlank() &&
            education.graduationYear.isNotBlank()) completedFields++
        if (skills.isNotEmpty()) completedFields++
        if (projects.isNotEmpty()) completedFields++
        if (accomplishments.isNotBlank()) completedFields++
        if (experience.isNotEmpty()) completedFields++ // Optional but counts if present

        return (completedFields.toFloat() / totalFields) * 100
    }
} 