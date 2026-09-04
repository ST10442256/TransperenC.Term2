package com.example.transperenc.ui.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.transperenc.ui.theme.TransperenCTheme

@Composable
fun PresentationSlide(
    title: String,
    subtitle: String,
    icon: ImageVector,
    points: List<String>
) {
    TransperenCTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF001F2B)
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color(0xFF00A67E)
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF00A67E)
                )
                Spacer(Modifier.height(40.dp))
                points.forEach { point ->
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF00A67E),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(
                            point,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 450)
@Composable
fun Slide1OverviewPreview() {
    PresentationSlide(
        title = "TransperenC",
        subtitle = "Redefining NGO Accountability",
        icon = Icons.Default.Shield,
        points = listOf(
            "Live Audited Financial Ledger",
            "Real-Time Transparency Score",
            "Gamified Compliance Tracking",
            "Direct Donor-to-Project Linkage"
        )
    )
}

@Preview(showBackground = true, widthDp = 800, heightDp = 450)
@Composable
fun Slide2LedgerPreview() {
    PresentationSlide(
        title = "Live Audited Ledger",
        subtitle = "Blockchain-Style Transparency",
        icon = Icons.Default.AccountBalanceWallet,
        points = listOf(
            "Sequential Cryptographic Reference IDs",
            "Immediate Audit Clearance Status",
            "Publicly Verifiable Spending Ratios",
            "Automated Statutory Compliance Alerts"
        )
    )
}

@Preview(showBackground = true, widthDp = 800, heightDp = 450)
@Composable
fun Slide3DonorPortalPreview() {
    PresentationSlide(
        title = "Donor Empowerment",
        subtitle = "Secure & Targeted Contributions",
        icon = Icons.Default.Favorite,
        points = listOf(
            "Direct Funding of Specific Campaigns",
            "Anonymized Public Ledger Options",
            "Automated Section 18A Tax Certificates",
            "Instant Proof-of-Donation Verification"
        )
    )
}

@Preview(showBackground = true, widthDp = 800, heightDp = 450)
@Composable
fun Slide4GamificationPreview() {
    PresentationSlide(
        title = "Transparency Warriors",
        subtitle = "Gamified Compliance Tracking",
        icon = Icons.Default.Stars,
        points = listOf(
            "XP Rewards for Document Uploads",
            "Dynamic Tiered NGO Levels",
            "Public 'Trust Badge' System",
            "Incentivized Real-Time Data Updates"
        )
    )
}

@Preview(showBackground = true, widthDp = 800, heightDp = 450)
@Composable
fun Slide5TechnicalStackPreview() {
    PresentationSlide(
        title = "Technical Architecture",
        subtitle = "Modern, Scalable & Secure",
        icon = Icons.Default.Settings,
        points = listOf(
            "Frontend: Kotlin & Jetpack Compose",
            "Backend: Firebase Real-time Firestore",
            "Patterns: Clean MVVM Architecture",
            "Security: Firebase Cryptographic Auth"
        )
    )
}
