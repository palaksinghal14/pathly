package com.example.pathly.repository

import android.util.Log
import com.example.pathly.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "UserRepository"
private const val USERS_COLLECTION = "users"
private const val PROFILE_COLLECTION = "profile"
private const val DETAILS_DOCUMENT = "details"

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val currentUserId: String?
        get() = auth.currentUser?.uid

    suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        val uid = currentUserId ?: return Result.failure(IllegalStateException("User not authenticated"))
        
        return try {
            firestore.collection(USERS_COLLECTION)
                .document(uid)
                .collection(PROFILE_COLLECTION)
                .document(DETAILS_DOCUMENT)
                .set(profile)
                .await()
            
            Log.d(TAG, "Successfully saved profile for user: $uid")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user profile", e)
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): Result<UserProfile?> {
        val uid = currentUserId ?: return Result.failure(IllegalStateException("User not authenticated"))
        
        return try {
            val document = firestore.collection(USERS_COLLECTION)
                .document(uid)
                .collection(PROFILE_COLLECTION)
                .document(DETAILS_DOCUMENT)
                .get()
                .await()

            if (document.exists()) {
                val profile = document.toObject(UserProfile::class.java)
                Log.d(TAG, "Successfully fetched profile for user: $uid")
                Result.success(profile)
            } else {
                Log.d(TAG, "No profile found for user: $uid")
                Result.success(null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching user profile", e)
            Result.failure(e)
        }
    }

    fun isUserLoggedIn(): Boolean = currentUserId != null
} 