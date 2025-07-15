package com.example.pathly.di

import com.example.pathly.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Provides
    @Singleton
    @Named("gemini_api_key")
    fun provideGeminiApiKey(): String {
        // In a real app, this should come from BuildConfig or a secure source
        // For now, we'll use a placeholder that should be replaced in production
        return BuildConfig.GEMINI_API_KEY
    }
} 