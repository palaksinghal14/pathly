package com.example.pathly.network

import com.example.pathly.model.GeminiRequest
import com.example.pathly.model.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiService {
    @POST("v1beta/models/gemini-pro:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
} 