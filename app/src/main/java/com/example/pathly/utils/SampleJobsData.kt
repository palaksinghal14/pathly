package com.example.pathly.utils

import com.example.pathly.model.Job
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SampleJobsData @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val sampleJobs = listOf(
        Job(
            title = "Android Developer",
            company = "TechCorp",
            location = "San Francisco, CA",
            skillsRequired = listOf("Kotlin", "Android", "Jetpack Compose", "Firebase"),
            description = """
                We are looking for an experienced Android Developer to join our mobile team.
                Key responsibilities:
                - Develop and maintain Android applications using Kotlin
                - Implement UI/UX designs using Jetpack Compose
                - Work with Firebase and other backend services
                - Write clean, maintainable, and testable code
            """.trimIndent(),
            applyDeadline = "2024-05-01"
        ),
        Job(
            title = "Full Stack Developer",
            company = "WebSolutions",
            location = "Remote",
            skillsRequired = listOf("React", "Node.js", "TypeScript", "MongoDB"),
            description = """
                Join our team as a Full Stack Developer!
                Requirements:
                - Strong experience with React and Node.js
                - Knowledge of TypeScript and MongoDB
                - Experience with RESTful APIs
                - Good understanding of web security
            """.trimIndent(),
            applyDeadline = "2024-04-15"
        ),
        Job(
            title = "Mobile App Developer",
            company = "AppStudio",
            location = "New York, NY",
            skillsRequired = listOf("Android", "iOS", "Flutter", "Firebase"),
            description = """
                Looking for a talented Mobile App Developer with cross-platform experience.
                Required skills:
                - Android and iOS development
                - Experience with Flutter
                - Firebase integration
                - Strong problem-solving skills
            """.trimIndent(),
            applyDeadline = "2024-04-30"
        ),
        Job(
            title = "Frontend Developer",
            company = "DesignCo",
            location = "Los Angeles, CA",
            skillsRequired = listOf("React", "JavaScript", "CSS", "UI/UX"),
            description = """
                Join our creative team as a Frontend Developer!
                What we're looking for:
                - Strong React and JavaScript skills
                - Eye for design and UI/UX principles
                - Experience with modern CSS frameworks
                - Passion for creating beautiful user interfaces
            """.trimIndent(),
            applyDeadline = "2024-05-15"
        ),
        Job(
            title = "Backend Engineer",
            company = "CloudTech",
            location = "Seattle, WA",
            skillsRequired = listOf("Java", "Spring Boot", "AWS", "MySQL"),
            description = """
                We're hiring a Backend Engineer to help scale our cloud services.
                Requirements:
                - Java and Spring Boot expertise
                - AWS cloud services experience
                - Database design and optimization
                - High-performance system design
            """.trimIndent(),
            applyDeadline = "2024-04-20"
        )
    )

    suspend fun populateJobs() {
        val batch = firestore.batch()
        val jobsCollection = firestore.collection("jobs")

        sampleJobs.forEach { job ->
            val docRef = jobsCollection.document()
            batch.set(docRef, job)
        }

        batch.commit().await()
    }
} 