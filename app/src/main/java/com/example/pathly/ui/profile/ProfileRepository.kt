package com.example.pathly.ui.profile

import android.util.Log
import com.example.pathly.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ProfileRepository"
private const val USERS_COLLECTION = "users"

@Singleton
class ProfileRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val currentUserId: String?
        get() = auth.currentUser?.uid

    suspend fun saveUserProfile(profile: UserProfile): Boolean {
        val uid = currentUserId ?: return false
        
        return try {
            firestore.collection(USERS_COLLECTION)
                .document(uid)
                .set(profile)
                .await()
            
            Log.d(TAG, "Successfully saved profile for user: $uid")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user profile", e)
            false
        }
    }

    suspend fun getUserProfile(): UserProfile? {
        val uid = currentUserId ?: return null
        
        return try {
            val document = firestore.collection(USERS_COLLECTION)
                .document(uid)
                .get()
                .await()

            if (document.exists()) {
                document.toObject(UserProfile::class.java)?.also {
                    Log.d(TAG, "Successfully fetched profile for user: $uid")
                }
            } else {
                Log.d(TAG, "No profile found for user: $uid")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching user profile", e)
            null
        }
    }
} 