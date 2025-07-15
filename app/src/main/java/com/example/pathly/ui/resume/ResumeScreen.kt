package com.example.pathly.ui.resume

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pathly.ui.theme.PathlyTheme
import com.example.pathly.viewmodel.ResumeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeScreen(
    viewModel: ResumeViewModel = hiltViewModel()
) {
    val resumeText = viewModel.resumeText
    val isLoading = viewModel.isLoading
    val error by viewModel.error.collectAsState(initial = null)

    LaunchedEffect(error) {
        error?.let {
            // Show snackbar or handle error
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Resume Generator",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Generate Resume Button
        Button(
            onClick = { 
                // TODO: Get actual UserProfile from ProfileViewModel or pass as parameter
                // viewModel.generateResume(userProfile)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generating...")
            } else {
                Icon(Icons.Default.Description, contentDescription = "Generate")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate Resume")
            }
        }

        // Resume Content
        if (!isLoading && resumeText != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Generated Resume",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = resumeText,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        } else if (!isLoading) {
            // Empty State
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Click 'Generate Resume' to create your\nprofessional resume",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Error Handling
        error?.let { errorMessage ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { viewModel.clearError() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResumeScreenPreview() {
    PathlyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ResumeScreen()
        }
    }
} 