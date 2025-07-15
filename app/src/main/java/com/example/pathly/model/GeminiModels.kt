package com.example.pathly.model

import com.google.gson.annotations.SerializedName

data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

data class GeminiResponse(
    val candidates: List<Candidate>?,
    @SerializedName("promptFeedback")
    val promptFeedback: PromptFeedback?
)

data class Candidate(
    val content: Content?,
    @SerializedName("finishReason")
    val finishReason: String?,
    val index: Int?
)

data class PromptFeedback(
    @SerializedName("safetyRatings")
    val safetyRatings: List<SafetyRating>?
)

data class SafetyRating(
    val category: String?,
    val probability: String?
) 