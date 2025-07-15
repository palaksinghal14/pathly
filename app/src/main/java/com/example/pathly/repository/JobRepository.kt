package com.example.pathly.repository

import android.util.Log
import com.example.pathly.model.AppliedJob
import com.example.pathly.model.Job
import com.example.pathly.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "JobRepository"
private const val JOBS_COLLECTION = "jobs"
private const val USERS_COLLECTION = "users"
private const val APPLICATIONS_COLLECTION = "applications"

@Singleton
class JobRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val currentUserId: String?
        get() = auth.currentUser?.uid

    suspend fun getAllJobs(): Result<List<Job>> {
        return try {
            val snapshot = firestore.collection(JOBS_COLLECTION)
                .get()
                .await()

            val jobs = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Job::class.java)?.copy(jobId = doc.id)
            }
            
            Result.success(jobs)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching jobs", e)
            Result.failure(e)
        }
    }

    suspend fun getFilteredJobs(userProfile: UserProfile): Result<List<Job>> {
        return try {
            val allJobs = getAllJobs().getOrThrow()
            val filteredJobs = allJobs.filter { job ->
                job.skillsRequired.any { skill ->
                    userProfile.skills.contains(skill)
                }
            }
            Result.success(filteredJobs)
        } catch (e: Exception) {
            Log.e(TAG, "Error filtering jobs", e)
            Result.failure(e)
        }
    }

    suspend fun saveJobApplication(jobId: String, job: Job, resumeText: String): Result<Unit> {
        val uid = currentUserId ?: return Result.failure(IllegalStateException("User not authenticated"))
        
        return try {
            val appliedJob = AppliedJob(
                jobId = jobId,
                title = job.title,
                company = job.company,
                timestamp = Date(),
                resumeText = resumeText
            )

            firestore.collection(USERS_COLLECTION)
                .document(uid)
                .collection(APPLICATIONS_COLLECTION)
                .document(jobId)
                .set(appliedJob)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving job application", e)
            Result.failure(e)
        }
    }

    suspend fun getAppliedJobs(): Result<List<AppliedJob>> {
        val uid = currentUserId ?: return Result.failure(IllegalStateException("User not authenticated"))
        
        return try {
            val snapshot = firestore.collection(USERS_COLLECTION)
                .document(uid)
                .collection(APPLICATIONS_COLLECTION)
                .get()
                .await()

            val appliedJobs = snapshot.documents.mapNotNull { doc ->
                doc.toObject(AppliedJob::class.java)
            }
            
            Result.success(appliedJobs)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching applied jobs", e)
            Result.failure(e)
        }
    }
} 