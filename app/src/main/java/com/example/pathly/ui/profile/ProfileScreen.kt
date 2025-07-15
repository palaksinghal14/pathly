package com.example.pathly.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pathly.model.Education
import com.example.pathly.model.Experience
import com.example.pathly.model.Project
import com.example.pathly.model.UserProfile
import com.example.pathly.ui.theme.PathlyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profile by viewModel.userProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val saveResult by viewModel.saveResult.collectAsState()
    val error by viewModel.error.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Form state
    var fullName by remember { mutableStateOf(profile?.name ?: "") }
    var email by remember { mutableStateOf(profile?.email ?: "") }
    var phone by remember { mutableStateOf(profile?.phone ?: "") }
    var skills by remember { mutableStateOf(profile?.skills?.joinToString(",") ?: "") }
    var projectTitle by remember { mutableStateOf("") }
    var projects by remember { mutableStateOf(profile?.projects?.map { it.title } ?: emptyList()) }
    
    // Education state
    var graduationDegree by remember { mutableStateOf(profile?.education?.graduationDegree ?: "") }
    var graduationInstitute by remember { mutableStateOf(profile?.education?.graduationInstitute ?: "") }
    var graduationYear by remember { mutableStateOf(profile?.education?.graduationYear ?: "") }
    var twelfthBoard by remember { mutableStateOf(profile?.education?.twelfthBoard ?: "") }
    var twelfthYear by remember { mutableStateOf(profile?.education?.twelfthYear ?: "") }
    var tenthBoard by remember { mutableStateOf(profile?.education?.tenthBoard ?: "") }
    var tenthYear by remember { mutableStateOf(profile?.education?.tenthYear ?: "") }
    
    // Experience state
    var currentCompany by remember { mutableStateOf(profile?.experience?.firstOrNull()?.companyName ?: "") }
    var currentRole by remember { mutableStateOf(profile?.experience?.firstOrNull()?.role ?: "") }

    // Load profile if null
    LaunchedEffect(Unit) {
        if (profile == null) {
            viewModel.loadUserProfile()
        }
    }

    // Update form when profile changes
    LaunchedEffect(profile) {
        profile?.let {
            fullName = it.name
            email = it.email
            phone = it.phone
            skills = it.skills.joinToString(",")
            projects = it.projects.map { project -> project.title }
            graduationDegree = it.education.graduationDegree
            graduationInstitute = it.education.graduationInstitute
            graduationYear = it.education.graduationYear
            twelfthBoard = it.education.twelfthBoard
            twelfthYear = it.education.twelfthYear
            tenthBoard = it.education.tenthBoard
            tenthYear = it.education.tenthYear
            currentCompany = it.experience.firstOrNull()?.companyName ?: ""
            currentRole = it.experience.firstOrNull()?.role ?: ""
        }
    }

    // Handle save result
    LaunchedEffect(saveResult) {
        when (saveResult) {
            is ProfileViewModel.SaveResult.Success -> {
                snackbarHostState.showSnackbar(
                    message = "Profile saved successfully",
                    duration = SnackbarDuration.Short
                )
                viewModel.clearSaveResult()
            }
            is ProfileViewModel.SaveResult.Error -> {
                snackbarHostState.showSnackbar(
                    message = (saveResult as ProfileViewModel.SaveResult.Error).message,
                    duration = SnackbarDuration.Long,
                    withDismissAction = true
                )
                viewModel.clearSaveResult()
            }
            null -> { /* Initial state */ }
        }
    }

    // Handle error
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header
                    Text(
                        text = "Profile",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Basic Information Card
                    ProfileSection(title = "Basic Information") {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.Person, "Name") }
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.Email, "Email") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            )
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.Phone, "Phone") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Phone
                            )
                        )
                    }

                    // Skills Card
                    ProfileSection(title = "Skills") {
                        OutlinedTextField(
                            value = skills,
                            onValueChange = { skills = it },
                            label = { Text("Skills (comma separated)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.Code, "Skills") },
                            placeholder = { Text("e.g., Android, Kotlin, Java") }
                        )
                    }

                    // Projects Card
                    ProfileSection(title = "Projects") {
                        OutlinedTextField(
                            value = projectTitle,
                            onValueChange = { projectTitle = it },
                            label = { Text("Project Title") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.Assignment, "Project") }
                        )
                        
                        Button(
                            onClick = { 
                                if (projectTitle.isNotBlank()) {
                                    projects = projects + projectTitle
                                    projectTitle = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, "Add Project")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Project")
                        }

                        // Show added projects
                        projects.forEach { project ->
                            OutlinedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(project)
                                    IconButton(
                                        onClick = { projects = projects - project }
                                    ) {
                                        Icon(Icons.Default.Delete, "Remove project")
                                    }
                                }
                            }
                        }
                    }

                    // Education Card
                    ProfileSection(title = "Education") {
                        // Graduation
                        Text(
                            text = "Graduation",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = graduationDegree,
                            onValueChange = { graduationDegree = it },
                            label = { Text("Degree") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = graduationInstitute,
                            onValueChange = { graduationInstitute = it },
                            label = { Text("Institute") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = graduationYear,
                            onValueChange = { graduationYear = it },
                            label = { Text("Year") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 12th
                        Text(
                            text = "12th Standard",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = twelfthBoard,
                            onValueChange = { twelfthBoard = it },
                            label = { Text("School") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = twelfthYear,
                            onValueChange = { twelfthYear = it },
                            label = { Text("Year") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 10th
                        Text(
                            text = "10th Standard",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = tenthBoard,
                            onValueChange = { tenthBoard = it },
                            label = { Text("School") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = tenthYear,
                            onValueChange = { tenthYear = it },
                            label = { Text("Year") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }

                    // Experience Card (Optional)
                    ProfileSection(title = "Experience (Optional)") {
                        OutlinedTextField(
                            value = currentCompany,
                            onValueChange = { currentCompany = it },
                            label = { Text("Current/Last Company") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.Business, "Company") }
                        )
                        OutlinedTextField(
                            value = currentRole,
                            onValueChange = { currentRole = it },
                            label = { Text("Role") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.Work, "Role") }
                        )
                    }

                    // Save Button
                    Button(
                        onClick = {
                            val updatedProfile = UserProfile(
                                name = fullName,
                                email = email,
                                phone = phone,
                                skills = skills.split(",").map { it.trim() }.filter { it.isNotBlank() },
                                projects = projects.map { Project(title = it) },
                                education = Education(
                                    graduationDegree = graduationDegree,
                                    graduationInstitute = graduationInstitute,
                                    graduationYear = graduationYear,
                                    twelfthBoard = twelfthBoard,
                                    twelfthYear = twelfthYear,
                                    tenthBoard = tenthBoard,
                                    tenthYear = tenthYear
                                ),
                                experience = if (currentCompany.isNotBlank() && currentRole.isNotBlank()) {
                                    listOf(Experience(
                                        companyName = currentCompany,
                                        role = currentRole
                                    ))
                                } else {
                                    emptyList()
                                }
                            )
                            viewModel.saveProfile(updatedProfile)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Save, "Save")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Profile")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    PathlyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProfileScreen()
        }
    }
} 