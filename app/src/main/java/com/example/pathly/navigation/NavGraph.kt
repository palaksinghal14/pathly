package com.example.pathly.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pathly.ui.AuthScreen
import com.example.pathly.ui.WelcomeInfoScreen
import com.example.pathly.ui.home.HomeScreen
import com.example.pathly.ui.jobs.JobScreen
import com.example.pathly.ui.profile.ProfileScreen
import com.example.pathly.ui.progress.ProgressScreen
import com.example.pathly.ui.resume.ResumeScreen
import com.example.pathly.ui.theme.StartScreen

sealed class Screen(val route: String) {
    object Start : Screen("start")
    object Auth : Screen("auth")
    object WelcomeInfo : Screen("welcome_info")
    object Home : Screen("home")
    object Jobs : Screen("jobs")
    object Resume : Screen("resume")
    object Profile : Screen("profile")
    object Progress : Screen("progress")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Start.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Start.route) {
            StartScreen(
                onGetStarted = { navController.navigate(Screen.Auth.route) }
            )
        }
        
        composable(Screen.Auth.route) {
            AuthScreen(
                onSignedIn = { navController.navigate(Screen.WelcomeInfo.route) },
                onSignedUp = { navController.navigate(Screen.WelcomeInfo.route) }
            )
        }
        
        composable(Screen.WelcomeInfo.route) {
            WelcomeInfoScreen(
                onProfileCompleted = { navController.navigate(Screen.Home.route) }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToJobs = { navController.navigate(Screen.Jobs.route) },
                onNavigateToResume = { navController.navigate(Screen.Resume.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToProgress = { navController.navigate(Screen.Progress.route) }
            )
        }
        
        composable(Screen.Jobs.route) {
            JobScreen()
        }
        
        composable(Screen.Resume.route) {
            ResumeScreen()
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        
        composable(Screen.Progress.route) {
            ProgressScreen()
        }
    }
} 