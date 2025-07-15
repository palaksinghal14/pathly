package com.example.pathly.utils

object GeminiConstants {
    const val BASE_URL = "https://generativelanguage.googleapis.com/"
    
    // Prompt templates
    val BASIC_RESUME_PROMPT = """
        Generate a professional, ATS-friendly resume with the following information:
        
        Name: %s
        Email: %s
        Phone: %s
        
        Skills: %s
        
        Education:
        - Graduation: %s from %s (%s)
        - 12th: %s (%s)
        - 10th: %s (%s)
        
        Projects: %s
        
        Experience: %s
        
        Please format the resume with clear sections (Education, Skills, Projects, Experience) 
        using bullet points where appropriate. Make it concise but comprehensive.
    """.trimIndent()
    
    val TARGETED_RESUME_PROMPT = """
        Generate a targeted, ATS-friendly resume tailored for the following job description:
        
        JOB DESCRIPTION:
        %s
        
        CANDIDATE INFORMATION:
        Name: %s
        Email: %s
        Phone: %s
        
        Skills: %s
        
        Education:
        - Graduation: %s from %s (%s)
        - 12th: %s (%s)
        - 10th: %s (%s)
        
        Projects: %s
        
        Experience: %s
        
        Please create a resume that:
        1. Highlights skills and experiences most relevant to the job description
        2. Uses industry-specific keywords from the job posting
        3. Formats with clear sections and bullet points
        4. Is optimized for ATS systems
    """.trimIndent()
} 