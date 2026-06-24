package com.example.transperenc.ui.donor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.transperenc.R
import com.example.transperenc.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorPortalScreen(
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    val donations by mainViewModel.donations.collectAsState()
    val reports by mainViewModel.reports.collectAsState()
    
    var donorName by remember { mutableStateOf("") }
    var emailId by remember { mutableStateOf("") }
    var donationAmount by remember { mutableStateOf("") }
    var selectedProjectId by remember { mutableStateOf("") }
    var selectedProjectName by remember { mutableStateOf("General Fund") }
    var isAnonymous by remember { mutableStateOf(false) }
    
    var isProcessing by remember { mutableStateOf(false) }
    var paymentSuccess by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    val successGreen = Color(0xFF00A67E)

    if (paymentSuccess) {
        AlertDialog(
            onDismissRequest = { paymentSuccess = false },
            title = { 
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = successGreen)
                    Spacer(Modifier.width(8.dp))
                    Text("Transaction Secured") 
                }
            },
            text = {
                Text("Thank you, ${if (isAnonymous) "Anonymous Donor" else donorName}! Your contribution of R $donationAmount has been registered on the cryptographic ledger and a Section 18A certificate has been generated.")
            },
            confirmButton = {
                Button(onClick = {
                    paymentSuccess = false
                    donorName = ""
                    emailId = ""
                    donationAmount = ""
                    isAnonymous = false
                }) {
                    Text("Return to Portal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = successGreen)
                        Spacer(Modifier.width(8.dp))
                        Text("Donor Portal")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(Color.Red, RoundedCornerShape(4.dp)))
                            Spacer(Modifier.width(8.dp))
                            Text("MAKE UNRESTRICTED CONTRIBUTION", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("Donor Name", style = MaterialTheme.typography.labelMedium)
                        OutlinedTextField(
                            value = donorName,
                            onValueChange = { donorName = it },
                            placeholder = { Text("e.g. Elena Rostova") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text("Deductible Email ID", style = MaterialTheme.typography.labelMedium)
                        OutlinedTextField(
                            value = emailId,
                            onValueChange = { emailId = it },
                            placeholder = { Text("name@domain.com") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text("Amount (R)", style = MaterialTheme.typography.labelMedium)
                        OutlinedTextField(
                            value = donationAmount,
                            onValueChange = { donationAmount = it },
                            placeholder = { Text("Minimum 10") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text("Campaign Program", style = MaterialTheme.typography.labelMedium)
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            OutlinedButton(
                                onClick = { expanded = true }, 
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(selectedProjectName, modifier = Modifier.weight(1f))
                                Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                DropdownMenuItem(
                                    text = { Text("General Fund") },
                                    onClick = {
                                        selectedProjectId = ""
                                        selectedProjectName = "General Fund"
                                        expanded = false
                                    }
                                )
                                reports.forEach { report ->
                                    DropdownMenuItem(
                                        text = { Text(report.title) },
                                        onClick = {
                                            selectedProjectId = report.id
                                            selectedProjectName = report.title
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isAnonymous, onCheckedChange = { isAnonymous = it })
                            Text("I prefer my details anonymized on the public ledger", style = MaterialTheme.typography.bodySmall)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = {
                                isProcessing = true
                                mainViewModel.processDonation(donationAmount, selectedProjectId, selectedProjectName)
                                scope.launch {
                                    delay(1500)
                                    isProcessing = false
                                    paymentSuccess = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = successGreen),
                            enabled = !isProcessing && donationAmount.isNotBlank()
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Text("Secure Contribution Transfer", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            item {
                Text("Live Donation History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            
            items(donations) { donation ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    ListItem(
                        headlineContent = { Text(donation.amount, fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("${donation.project} • ${donation.date}") },
                        trailingContent = {
                            Icon(Icons.Default.Shield, contentDescription = "Verified", tint = successGreen, modifier = Modifier.size(16.dp))
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
            
            item {
                OutlinedButton(onClick = onNavigateToReports, modifier = Modifier.fillMaxWidth()) {
                    Text("Audit Public Ledger")
                }
            }
            
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
