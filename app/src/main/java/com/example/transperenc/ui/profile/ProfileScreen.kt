package com.example.transperenc.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.transperenc.R
import com.example.transperenc.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    
    var editUsername by remember(currentUser) { mutableStateOf(currentUser?.username ?: "") }
    var editRole by remember(currentUser) { mutableStateOf(currentUser?.role ?: "") }
    var editOrg by remember(currentUser) { mutableStateOf(currentUser?.organization ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isEditing) {
                        IconButton(onClick = { 
                            userViewModel.updateProfile(editUsername, editRole, editOrg)
                            isEditing = false 
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Save")
                        }
                    } else {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isEditing) {
                TextField(
                    value = editUsername,
                    onValueChange = { editUsername = it },
                    label = { Text(stringResource(R.string.username)) },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = currentUser?.username ?: "Guest",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = currentUser?.email ?: "No email",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.account_info), style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (isEditing) {
                        TextField(
                            value = editRole,
                            onValueChange = { editRole = it },
                            label = { Text("Role") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = editOrg,
                            onValueChange = { editOrg = it },
                            label = { Text("Organization") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text("Role: ${currentUser?.role ?: "N/A"}")
                        Text("Organization: ${currentUser?.organization ?: "N/A"}")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (!isEditing) {
                Button(
                    onClick = {
                        userViewModel.logout()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.logout))
                }
            }
        }
    }
}
