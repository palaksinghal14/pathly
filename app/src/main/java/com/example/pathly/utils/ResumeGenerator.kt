package com.example.pathly.utils

import android.util.Log
import com.example.pathly.model.*
import com.example.pathly.network.GeminiService
import com.example.pathly.utils.GeminiConstants.BASIC_RESUME_PROMPT
import com.example.pathly.utils.GeminiConstants.TARGETED_RESUME_PROMPT
import kotlinx.coroutines.CancellationException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ResumeGenerator"

@Singleton
class ResumeGenerator @Inject constructor(
    private val geminiService: GeminiService,
    private val apiKey: String
) {
    suspend fun generateBasicResume(userProfile: UserProfile): Result<String> {
        return try {
            val prompt = String.format(
                BASIC_RESUME_PROMPT,
                userProfile.name,
                userProfile.email,
                userProfile.phone,
                userProfile.skills.joinToString(", "),
                userProfile.education.graduationDegree,
                userProfile.education.graduationInstitute,
                userProfile.education.graduationYear,
                userProfile.education.twelfthBoard,
                userProfile.education.twelfthYear,
                userProfile.education.tenthBoard,
                userProfile.education.tenthYear,
                userProfile.projects.joinToString("\n") { "- ${it.title}" },
                userProfile.experience.joinToString("\n") { 
                    "- ${it.role} at ${it.companyName} (${it.duration})" 
                }
            )

            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part(text = prompt))
                    )
                )
            )

            val response = geminiService.generateContent(apiKey, request)
            
            if (response.isSuccessful) {
                val resumeText = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: throw IOException("Empty response from Gemini API")
                Result.success(resumeText)
            } else {
                Result.failure(IOException("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error generating basic resume", e)
            Result.failure(IOException("Failed to generate resume: ${e.message}"))
        }
    }

    suspend fun generateResumeForJob(userProfile: UserProfile, jobDescription: String): Result<String> {
        return try {
            val prompt = String.format(
                TARGETED_RESUME_PROMPT,
                jobDescription,
                userProfile.name,
                userProfile.email,
                userProfile.phone,
                userProfile.skills.joinToString(", "),
                userProfile.education.graduationDegree,
                userProfile.education.graduationInstitute,
                userProfile.education.graduationYear,
                userProfile.education.twelfthBoard,
                userProfile.education.twelfthYear,
                userProfile.education.tenthBoard,
                userProfile.education.tenthYear,
                userProfile.projects.joinToString("\n") { "- ${it.title}" },
                userProfile.experience.joinToString("\n") { 
                    "- ${it.role} at ${it.companyName} (${it.duration})" 
                }
            )

            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part(text = prompt))
                    )
                )
            )

            val response = geminiService.generateContent(apiKey, request)
            
            if (response.isSuccessful) {
                val resumeText = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: throw IOException("Empty response from Gemini API")
                Result.success(resumeText)
            } else {
                Result.failure(IOException("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error generating targeted resume", e)
            Result.failure(IOException("Failed to generate targeted resume: ${e.message}"))
        }
    }
} 