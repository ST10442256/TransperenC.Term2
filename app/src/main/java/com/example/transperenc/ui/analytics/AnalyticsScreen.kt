package com.example.transperenc.ui.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.transperenc.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val reports by mainViewModel.reports.collectAsState()
    val totalRaised by mainViewModel.totalRaised.collectAsState()
    val complianceScore by mainViewModel.complianceScore.collectAsState()
    
    val totalBudget = reports.sumOf { it.targetBudget }
    val totalSpending = reports.sumOf { it.currentSpending }
    val totalGoals = reports.sumOf { it.donationGoal }
    val totalDonations = totalRaised

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transparency Analytics") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text("Transparency Score", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                ComplianceScoreCard(complianceScore)
            }

            item {
                Text("Budget vs Spending", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                BudgetComparisonChart(totalBudget, totalSpending)
            }
            
            item {
                Text("Donation Progress (All Projects)", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                DonationGoalChart(totalGoals, totalDonations)
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Financial Summary", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        SummaryRow("Total Projects", reports.size.toString())
                        SummaryRow("Total Budgeted", "R ${totalBudget.toInt()}")
                        SummaryRow("Total Spent", "R ${totalSpending.toInt()}")
                        SummaryRow("Donation Goals", "R ${totalGoals.toInt()}")
                        SummaryRow("Funds Raised", "R ${totalDonations.toInt()}")
                        SummaryRow("Overall Efficiency", "${if (totalSpending > 0) ((totalDonations/totalSpending)*100).toInt() else 0}%")
                    }
                }
            }
        }
    }
}

@Composable
fun ComplianceScoreCard(score: Float) {
    val color = when {
        score > 0.8f -> Color(0xFF4CAF50)
        score > 0.5f -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Compliance & Trust", style = MaterialTheme.typography.titleMedium, color = color)
                Text(
                    text = when {
                        score > 0.8f -> "Excellent transparency standards met."
                        score > 0.5f -> "Good, but some reporting is missing."
                        else -> "Critical data missing for transparency."
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { score },
                    modifier = Modifier.size(60.dp),
                    color = color,
                    strokeWidth = 8.dp,
                    trackColor = color.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round
                )
                Text("${(score * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, color = color)
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun BudgetComparisonChart(budget: Double, spending: Double) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.error
    
    Card(modifier = Modifier.fillMaxWidth().height(200.dp)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Canvas(modifier = Modifier.size(120.dp).weight(1f)) {
                val sweep = if (budget > 0) (spending / budget * 360f).toFloat().coerceIn(0f, 360f) else 0f
                drawArc(
                    color = primaryColor.copy(alpha = 0.2f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = if (spending > budget) secondaryColor else primaryColor,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                LegendItem("Budgeted", primaryColor.copy(alpha = 0.2f))
                LegendItem("Spent", if (spending > budget) secondaryColor else primaryColor)
                Spacer(Modifier.height(8.dp))
                Text("${((if (budget > 0) spending / budget else 0.0) * 100).toInt()}% Used", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}

@Composable
fun DonationGoalChart(goal: Double, raised: Double) {
    val primaryColor = MaterialTheme.colorScheme.secondary
    
    Card(modifier = Modifier.fillMaxWidth().height(200.dp)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
            val progress = if (goal > 0) (raised / goal).toFloat().coerceIn(0f, 1f) else 0f
            Text("Raised: R ${raised.toInt()} / Goal: R ${goal.toInt()}", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth().height(40.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawLine(
                        color = primaryColor.copy(alpha = 0.2f),
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 40.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = primaryColor,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width * progress, size.height / 2),
                        strokeWidth = 40.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("${(progress * 100).toInt()}% of goal reached", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
        Surface(modifier = Modifier.size(12.dp), color = color, shape = MaterialTheme.shapes.small) {}
        Spacer(Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}
