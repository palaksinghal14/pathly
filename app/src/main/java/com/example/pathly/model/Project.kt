package com.example.pathly.model

data class Project(
    val title: String = "",
    val description: String = "",
    val techStack: List<String> = emptyList()
) 