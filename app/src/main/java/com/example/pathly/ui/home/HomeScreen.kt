package com.example.pathly.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pathly.ui.theme.PathlyTheme

sealed class BottomNavItem(
    val route: String,
    val icon: @Composable () -> Unit,
    val label: String
) {
    object Home : BottomNavItem(
        route = "home",
        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
        label = "Home"
    )
    object Jobs : BottomNavItem(
        route = "jobs",
        icon = { Icon(Icons.Default.Work, contentDescription = "Jobs") },
        label = "Jobs"
    )
    object Progress : BottomNavItem(
        route = "progress",
        icon = { Icon(Icons.Default.Assessment, contentDescription = "Progress") },
        label = "Progress"
    )
    object Profile : BottomNavItem(
        route = "profile",
        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
        label = "Profile"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(BottomNavItem.Home.route) }
    val isProfileComplete = remember { false } // Placeholder for profile completion status

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Jobs,
                    BottomNavItem.Progress,
                    BottomNavItem.Profile
                ).forEach { item ->
                    NavigationBarItem(
                        selected = selectedTab == item.route,
                        onClick = { selectedTab = item.route },
                        icon = item.icon,
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                BottomNavItem.Home.route -> HomeContent(isProfileComplete)
                // Other screens will be handled by navigation
                else -> {}
            }
        }
    }
}

@Composable
private fun HomeContent(isProfileComplete: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Welcome Message Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isProfileComplete) Icons.Default.WorkOutline else Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = if (isProfileComplete) 
                        "Your personalized job feed will appear here soon." 
                    else 
                        "Complete your profile to get personalized job matches!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (!isProfileComplete) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { /* Navigate to profile */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Complete Profile")
                    }
                }
            }
        }
    }
} 

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    PathlyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen()
        }
    }
} 