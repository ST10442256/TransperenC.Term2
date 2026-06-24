package com.example.transperenc.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.transperenc.R
import com.example.transperenc.viewmodel.MainViewModel
import com.example.transperenc.viewmodel.Report

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val reports by mainViewModel.reports.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddReportDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, desc, budget, goal, loc ->
                mainViewModel.addReport(title, desc, budget, goal, loc)
                showAddDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.reports), style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Create Report") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reports, key = { it.id }) { report ->
                ReportCard(report = report, onDelete = { mainViewModel.deleteReport(report.id) })
            }
        }
    }
}

@Composable
fun ReportCard(report: Report, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(report.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
            Text("Date: ${report.dateSubmitted}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(report.description, style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Progress Section (Budget Usage)
            val budgetProgress = if (report.targetBudget > 0) (report.currentSpending / report.targetBudget).toFloat().coerceIn(0f, 1f) else 0f
            Text("Budget Usage (R ${report.currentSpending.toInt()} / R ${report.targetBudget.toInt()})", style = MaterialTheme.typography.labelMedium)
            LinearProgressIndicator(
                progress = budgetProgress,
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = if (budgetProgress > 0.9f) Color.Red else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Donation Progress Section
            val donationProgress = if (report.donationGoal > 0) (report.amountDonated / report.donationGoal).toFloat().coerceIn(0f, 1f) else 0f
            val goalReached = report.amountDonated >= report.donationGoal && report.donationGoal > 0

            Text("Donation Progress (R ${report.amountDonated.toInt()} / R ${report.donationGoal.toInt()})", style = MaterialTheme.typography.labelMedium)
            LinearProgressIndicator(
                progress = donationProgress,
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = if (goalReached) Color(0xFF4CAF50) else MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            
            if (goalReached) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Celebration, contentDescription = null, tint = Color(0xFF4CAF50))
                    Spacer(Modifier.width(8.dp))
                    Text("Goal reached! Hooray!", color = Color(0xFF4CAF50), style = MaterialTheme.typography.bodyMedium)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Status", style = MaterialTheme.typography.labelSmall)
                    Text(report.status, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                }
                Column {
                    Text("Beneficiaries", style = MaterialTheme.typography.labelSmall)
                    Text(report.beneficiariesReached, style = MaterialTheme.typography.bodyMedium)
                }
                Column {
                    Text("Location", style = MaterialTheme.typography.labelSmall)
                    Text(report.projectLocation, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun AddReportDialog(onDismiss: () -> Unit, onConfirm: (String, String, Double, Double, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var loc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Transparency Project") },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Project Title") }, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(value = budget, onValueChange = { budget = it }, label = { Text("Target Budget (ZAR)") }, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(value = goal, onValueChange = { goal = it }, label = { Text("Donation Goal (ZAR)") }, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(value = loc, onValueChange = { loc = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth()) }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    onConfirm(
                        title, 
                        desc, 
                        budget.toDoubleOrNull() ?: 0.0, 
                        goal.toDoubleOrNull() ?: 0.0, 
                        loc
                    ) 
                }, 
                enabled = title.isNotBlank() && budget.toDoubleOrNull() != null
            ) {
                Text("Create Project")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
