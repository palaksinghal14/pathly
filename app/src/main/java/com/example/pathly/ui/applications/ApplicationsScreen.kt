package com.example.pathly.ui.applications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pathly.model.AppliedJob
import com.example.pathly.viewmodel.ApplicationsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ApplicationsScreen(
    viewModel: ApplicationsViewModel = hiltViewModel()
) {
    val applications by viewModel.applications.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedApplication by viewModel.selectedApplication.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle error messages
    LaunchedEffect(error) {
        if (error != null) {
            snackbarHostState.showSnackbar(
                message = error!!,
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
            viewModel.clearError()
        }
    }

    // Show resume preview dialog
    if (selectedApplication != null) {
        ResumePreviewDialog(
            application = selectedApplication!!,
            onDismiss = { viewModel.clearSelectedApplication() }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                applications.isEmpty() -> {
                    EmptyState()
                }
                else -> {
                    ApplicationsList(
                        applications = applications,
                        onViewResume = { viewModel.selectApplication(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicationsList(
    applications: List<AppliedJob>,
    onViewResume: (AppliedJob) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(applications) { application ->
            ApplicationCard(
                application = application,
                onViewResume = { onViewResume(application) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ApplicationCard(
    application: AppliedJob,
    onViewResume: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Job Title and Company
            Column {
                Text(
                    text = application.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = application.company,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Application Date
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = "Applied Date",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Applied on ${dateFormat.format(application.timestamp)}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // View Resume Button
            OutlinedButton(
                onClick = onViewResume,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    Icons.Default.Description,
                    contentDescription = "View Resume",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("View Resume")
            }
        }
    }
}

@Composable
private fun ResumePreviewDialog(
    application: AppliedJob,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Resume for ${application.title}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = application.resumeText,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.WorkOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No job applications yet",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
} 