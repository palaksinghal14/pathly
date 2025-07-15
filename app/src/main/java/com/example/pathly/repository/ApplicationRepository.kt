package com.example.pathly.repository

import android.util.Log
import com.example.pathly.model.AppliedJob
import com.example.pathly.model.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ApplicationRepository"
private const val APPLICATIONS_COLLECTION = "applications"

@Singleton
class ApplicationRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val currentUserId: String?
        get() = auth.currentUser?.uid

    suspend fun saveApplication(job: Job, resumeText: String): Result<Unit> {
        val uid = currentUserId ?: return Result.failure(IllegalStateException("User not authenticated"))
        
        return try {
            val appliedJob = AppliedJob(
                userId = uid,
                jobId = job.jobId,
                title = job.title,
                company = job.company,
                timestamp = Date(),
                resumeText = resumeText
            )

            firestore.collection(APPLICATIONS_COLLECTION)
                .document("${uid}_${job.jobId}")
                .set(appliedJob)
                .await()

            Log.d(TAG, "Successfully saved application for job: ${job.title}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving job application", e)
            Result.failure(e)
        }
    }

    suspend fun getUserApplications(): Result<List<AppliedJob>> {
        val uid = currentUserId ?: return Result.failure(IllegalStateException("User not authenticated"))
        
        return try {
            val snapshot = firestore.collection(APPLICATIONS_COLLECTION)
                .whereEqualTo("userId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val applications = snapshot.documents.mapNotNull { doc ->
                doc.toObject(AppliedJob::class.java)
            }
            
            Log.d(TAG, "Successfully fetched ${applications.size} applications")
            Result.success(applications)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching user applications", e)
            Result.failure(e)
        }
    }

    suspend fun hasAppliedToJob(jobId: String): Result<Boolean> {
        val uid = currentUserId ?: return Result.failure(IllegalStateException("User not authenticated"))
        
        return try {
            val document = firestore.collection(APPLICATIONS_COLLECTION)
                .document("${uid}_$jobId")
                .get()
                .await()

            Result.success(document.exists())
        } catch (e: Exception) {
            Log.e(TAG, "Error checking job application status", e)
            Result.failure(e)
        }
    }
} 