package com.example.pathly.ui.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pathly.model.AppliedJob
import com.example.pathly.model.ApplicationStatus

@Composable
fun ProgressScreen() {
    // Dummy data
    val appliedJobs = remember {
        listOf(
            AppliedJob(
                "Senior Android Developer",
                "Google",
                "Tech Resume v1",
                92,
                ApplicationStatus.SHORTLISTED
            ),
            AppliedJob(
                "Mobile Developer",
                "Meta",
                "Mobile Dev Resume",
                88,
                ApplicationStatus.INTERVIEW_SCHEDULED
            ),
            AppliedJob(
                "Android Engineer",
                "Netflix",
                "Android Resume v2",
                85,
                ApplicationStatus.APPLIED
            ),
            AppliedJob(
                "Software Engineer",
                "Amazon",
                "SWE Resume",
                78,
                ApplicationStatus.REJECTED
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Application Progress",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Stats Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    count = appliedJobs.size,
                    label = "Applied",
                    icon = Icons.Default.Send
                )
                StatItem(
                    count = appliedJobs.count { it.status == ApplicationStatus.SHORTLISTED },
                    label = "Shortlisted",
                    icon = Icons.Default.ThumbUp
                )
                StatItem(
                    count = appliedJobs.count { it.status == ApplicationStatus.INTERVIEW_SCHEDULED },
                    label = "Interviews",
                    icon = Icons.Default.Event
                )
            }
        }

        // Applications List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(appliedJobs.sortedByDescending { it.atsScore }) { job ->
                ApplicationCard(job)
            }
        }
    }
}

@Composable
fun StatItem(
    count: Int,
    label: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationCard(job: AppliedJob) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = job.jobTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = job.company,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusBadge(status = job.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Resume: ${job.resumeTitle}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "ATS Score: ${job.atsScore}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = when {
                            job.atsScore >= 90 -> MaterialTheme.colorScheme.primary
                            job.atsScore >= 80 -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.tertiary
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: ApplicationStatus) {
    Surface(
        color = Color(status.getColor()).copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.name.replace("_", " "),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = Color(status.getColor()),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressScreenPreview() {
    ProgressScreen()
} 