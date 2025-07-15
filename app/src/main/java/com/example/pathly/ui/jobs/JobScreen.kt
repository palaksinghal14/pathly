package com.example.pathly.ui.jobs

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pathly.model.Job
import com.example.pathly.model.UserProfile
import com.example.pathly.ui.theme.PathlyTheme
import com.example.pathly.utils.SampleJobsData
import com.example.pathly.viewmodel.JobViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun JobScreen(
    viewModel: JobViewModel = hiltViewModel(),
    userProfile: UserProfile? = null // TODO: Inject from parent
) {
    val jobs by viewModel.jobs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val generatedResume by viewModel.generatedResume.collectAsState()
    val applicationStatus by viewModel.applicationStatus.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Load jobs when userProfile changes
    LaunchedEffect(userProfile) {
        viewModel.loadJobs(userProfile)
    }

    // Handle application status changes
    LaunchedEffect(applicationStatus) {
        when (val status = applicationStatus) {
            is JobViewModel.ApplicationStatus.Success -> {
                snackbarHostState.showSnackbar(
                    message = status.message,
                    duration = SnackbarDuration.Short
                )
                viewModel.clearApplicationStatus()
            }
            is JobViewModel.ApplicationStatus.Error -> {
                snackbarHostState.showSnackbar(
                    message = status.message,
                    duration = SnackbarDuration.Long,
                    withDismissAction = true
                )
                viewModel.clearApplicationStatus()
            }
            null -> { /* Do nothing */ }
        }
    }

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
    if (generatedResume != null) {
        ResumePreviewDialog(
            resume = generatedResume!!,
            onDismiss = { viewModel.clearGeneratedResume() }
        )
    }

    // temporary data
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Add this block temporarily
    LaunchedEffect(Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val uploader = SampleJobsData(firestore)
        uploader.populateJobs()
        Toast
            .makeText(context, "Sample jobs uploaded", Toast.LENGTH_SHORT)
            .show()
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
                jobs.isEmpty() -> {
                    EmptyState()
                }
                else -> {
                    JobList(
                        jobs = jobs,
                        onGenerateResume = { job ->
                            userProfile?.let { profile ->
                                viewModel.generateResumeForJob(job, profile)
                            }
                        },
                        onAutoApply = { job ->
                            userProfile?.let { profile ->
                                viewModel.autoApply(job, profile)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun JobList(
    jobs: List<Job>,
    onGenerateResume: (Job) -> Unit,
    onAutoApply: (Job) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(jobs) { job ->
            JobCard(
                job = job,
                onGenerateResume = { onGenerateResume(job) },
                onAutoApply = { onAutoApply(job) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JobCard(
    job: Job,
    onGenerateResume: () -> Unit,
    onAutoApply: () -> Unit
) {
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
                    text = job.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = job.company,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Location and Deadline
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = job.location,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = "Deadline",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Due: ${job.applyDeadline}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Required Skills
            Text(
                text = "Required Skills",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                job.skillsRequired.take(3).forEach { skill ->
                    SuggestionChip(
                        onClick = { },
                        label = { Text(skill) }
                    )
                }
                if (job.skillsRequired.size > 3) {
                    Text(
                        text = "+${job.skillsRequired.size - 3} more",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            // Description
            Text(
                text = job.description,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onGenerateResume,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = "Generate Resume",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Generate & Preview")
                }
                
                Button(
                    onClick = onAutoApply,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Auto Apply",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Auto Apply")
                }
            }
        }
    }
}

@Composable
private fun ResumePreviewDialog(
    resume: String,
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
                    text = "Generated Resume",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = resume,
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
            Icons.Default.Work,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No jobs available at the moment",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JobScreenPreview() {
    PathlyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            JobScreen()
        }
    }
} 