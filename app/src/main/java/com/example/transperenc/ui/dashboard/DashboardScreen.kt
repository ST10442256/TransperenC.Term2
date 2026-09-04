package com.example.transperenc.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.transperenc.R
import com.example.transperenc.viewmodel.MainViewModel
import com.example.transperenc.ui.theme.TransperenCTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    mainViewModel: MainViewModel,
    onNavigateToReports: () -> Unit,
    onNavigateToDocuments: () -> Unit,
    onNavigateToDonorPortal: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAnalytics: () -> Unit
) {
    val reports by mainViewModel.reports.collectAsState()
    val complianceScore by mainViewModel.complianceScore.collectAsState()
    val xp by mainViewModel.xp.collectAsState()
    val level by mainViewModel.level.collectAsState()
    val totalRaised by mainViewModel.totalRaised.collectAsState()
    val alerts by mainViewModel.alerts.collectAsState()

    val navyBlue = Color(0xFF0F2942)
    val successGreen = Color(0xFF00A67E)
    val darkBg = Color(0xFF001F2B)

    Column(modifier = Modifier.fillMaxSize()) {
        // Statutory Deadline Warning Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = darkBg
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "STATUTORY DEADLINE: Form 990 due June 15, 2026!",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = successGreen
                ) {
                    Text(
                        text = "12 DAYS LEFT",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = successGreen, modifier = Modifier.size(28.dp))
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text("NPO-FCTMS", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Text("FINANCIAL LEDGER", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToAnalytics) {
                            Icon(Icons.Default.BarChart, contentDescription = "Analytics")
                        }
                        IconButton(onClick = onNavigateToProfile) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                        }
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    },
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }

                // Hero Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = navyBlue),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Surface(
                                color = successGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    "SYSTEM AUDIT CORE LIVE",
                                    color = successGreen,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "Securing Trust Through Absolute Transparency",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Every cent received is registered on our audit-checked sequential cryptographic ledger.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                // Stats Grid
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StatCard(
                                title = "TOTAL REVENUE",
                                value = "R ${totalRaised.toInt()}",
                                subtitle = "100% Tax Compliant",
                                icon = Icons.Default.AccountBalanceWallet,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = "ALLOCATIONS",
                                value = "R ${(totalRaised * 0.37).toInt()}",
                                subtitle = "Approved funds",
                                icon = Icons.Default.AssignmentTurnedIn,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StatCard(
                                title = "SERVICE RATIO",
                                value = "${(complianceScore * 100).toInt()}%",
                                subtitle = "Goal: > 60%",
                                icon = Icons.Default.TrendingUp,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = "BLOCKCHAIN",
                                value = "SECURED",
                                subtitle = "7 blocks verified",
                                icon = Icons.Default.Lock,
                                valueColor = successGreen,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                if (alerts.isNotEmpty()) {
                    item {
                        Text("Action Required", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    items(alerts) { alert ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (alert.type == "Critical") MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            ListItem(
                                headlineContent = { Text(alert.title, fontWeight = FontWeight.Bold) },
                                supportingContent = { Text(alert.message) },
                                leadingContent = { 
                                    Icon(
                                        if (alert.type == "Critical") Icons.Default.Warning else Icons.Default.Notifications, 
                                        contentDescription = null,
                                        tint = if (alert.type == "Critical") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                    ) 
                                },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                            )
                        }
                    }
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Live Audited Ledger", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        TextButton(onClick = onNavigateToReports) {
                            Text("View All")
                        }
                    }
                }

                items(reports.take(3), key = { it.id }) { report ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(report.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                    Text("Ref: TXN-${report.id.take(8).uppercase()}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                                }
                                Surface(
                                    color = successGreen.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        "CLEARED",
                                        color = successGreen,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            
                            Spacer(Modifier.height(8.dp))
                            
                            val budgetProgress = if (report.targetBudget > 0) (report.currentSpending / report.targetBudget).toFloat().coerceIn(0f, 1f) else 0f
                            
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Audit Clearance Value", style = MaterialTheme.typography.labelSmall)
                                Text("R ${report.currentSpending.toInt()}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                            }
                            LinearProgressIndicator(
                                progress = budgetProgress,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(4.dp),
                                color = successGreen,
                                trackColor = successGreen.copy(alpha = 0.1f),
                                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                        }
                    }
                }

                // Call to action: Quick Contribution
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).background(Color.Red, RoundedCornerShape(4.dp)))
                                Spacer(Modifier.width(8.dp))
                                Text("MAKE UNRESTRICTED CONTRIBUTION", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("Every donation secured instantly generates a Section 18A dynamic PDF certificate.", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = onNavigateToDonorPortal,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = successGreen)
                            ) {
                                Text("Secure Contribution Transfer", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedButton(
                            onClick = onNavigateToDocuments,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.documents))
                        }
                        
                        // User Profile/XP summary
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                        ) {
                            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Stars, contentDescription = "XP: $xp", tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(4.dp))
                                Column {
                                    Text("Level $level", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                    Text("$xp XP", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
                
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.Unspecified
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.width(4.dp))
                Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                value, 
                style = MaterialTheme.typography.titleLarge, 
                fontWeight = FontWeight.ExtraBold,
                color = if (valueColor == Color.Unspecified) MaterialTheme.colorScheme.onSurface else valueColor
            )
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
    }
}
