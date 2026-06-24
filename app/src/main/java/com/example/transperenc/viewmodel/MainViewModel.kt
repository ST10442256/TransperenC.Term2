package com.example.transperenc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class Report(
    val id: String = "",
    val title: String = "",
    val status: String = "Active",
    val description: String = "",
    val targetBudget: Double = 0.0,
    val currentSpending: Double = 0.0,
    val donationGoal: Double = 0.0,
    val amountDonated: Double = 0.0,
    val beneficiariesReached: String = "0",
    val projectLocation: String = "",
    val dateSubmitted: String = ""
)

data class Document(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val fileUri: String = ""
)

data class Donation(
    val id: String = "",
    val amount: String = "",
    val date: String = "",
    val project: String = "",
    val projectId: String = "",
    val category: String = "Donation",
    val reference: String = ""
)

data class Alert(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "Info"
)

class MainViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports.asStateFlow()

    private val _documents = MutableStateFlow<List<Document>>(emptyList())
    val documents: StateFlow<List<Document>> = _documents.asStateFlow()

    private val _donations = MutableStateFlow<List<Donation>>(emptyList())
    val donations: StateFlow<List<Donation>> = _donations.asStateFlow()

    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts: StateFlow<List<Alert>> = _alerts.asStateFlow()

    private val _complianceScore = MutableStateFlow(0f)
    val complianceScore: StateFlow<Float> = _complianceScore.asStateFlow()

    private val _xp = MutableStateFlow(0)
    val xp: StateFlow<Int> = _xp.asStateFlow()

    private val _level = MutableStateFlow(1)
    val level: StateFlow<Int> = _level.asStateFlow()

    private val _totalRaised = MutableStateFlow(0.0)
    val totalRaised: StateFlow<Double> = _totalRaised.asStateFlow()

    init {
        fetchReports()
        fetchDocuments()
        fetchDonations()
        fetchAlerts()
    }

    private fun fetchAlerts() {
        db.collection("alerts").addSnapshotListener { snapshot, _ ->
            _alerts.value = snapshot?.documents?.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null
                Alert(
                    id = doc.id,
                    title = data["title"]?.toString() ?: "",
                    message = data["message"]?.toString() ?: "",
                    type = data["type"]?.toString() ?: "Info"
                )
            } ?: emptyList()
        }
    }

    private fun calculateComplianceScore() {
        var score = 0f
        
        // 1. Data Presence (40%)
        if (_reports.value.isNotEmpty()) score += 0.2f
        if (_documents.value.isNotEmpty()) score += 0.2f
        
        // 2. Project Health (30%)
        if (_reports.value.isNotEmpty()) {
            val healthScores = _reports.value.map { report ->
                val ratio = if (report.targetBudget > 0) report.currentSpending / report.targetBudget else 1.0
                when {
                    ratio <= 1.0 -> 1.0f 
                    ratio <= 1.1 -> 0.7f 
                    else -> 0.3f 
                }
            }
            score += (healthScores.average().toFloat() * 0.3f)
        }
        
        // 3. Activity (30%)
        val documentPoints = (_documents.value.size * 0.05f).coerceAtMost(0.15f)
        val totalRaisedAmt = _totalRaised.value
        val donationPoints = if (totalRaisedAmt > 0) 0.15f else 0f
        score += documentPoints + donationPoints
        
        _complianceScore.value = score.coerceIn(0f, 1f)
        
        val totalXp = (_documents.value.size * 20) + (_reports.value.size * 30) + (_donations.value.size * 50)
        _xp.value = totalXp
        _level.value = (totalXp / 1000) + 1
    }

    private fun fetchReports() {
        db.collection("reports").addSnapshotListener { snapshot, _ ->
            val reportList = snapshot?.documents?.mapNotNull { doc ->
                try {
                    val data = doc.data ?: return@mapNotNull null
                    
                    // Robust conversion helper
                    fun Any?.asDouble(): Double = when (this) {
                        is Number -> this.toDouble()
                        is String -> this.toDoubleOrNull() ?: 0.0
                        else -> 0.0
                    }

                    Report(
                        id = doc.id,
                        title = data["title"]?.toString() ?: "",
                        status = data["status"]?.toString() ?: "Active",
                        description = data["description"]?.toString() ?: "",
                        targetBudget = data["targetBudget"].asDouble(),
                        currentSpending = data["currentSpending"].asDouble(),
                        donationGoal = data["donationGoal"].asDouble(),
                        amountDonated = data["amountDonated"].asDouble(),
                        beneficiariesReached = data["beneficiariesReached"]?.toString() ?: "0",
                        projectLocation = data["projectLocation"]?.toString() ?: "",
                        dateSubmitted = data["dateSubmitted"]?.toString() ?: ""
                    )
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()
            
            _reports.value = reportList
            calculateComplianceScore()
        }
    }

    private fun fetchDocuments() {
        db.collection("documents").addSnapshotListener { snapshot, _ ->
            _documents.value = snapshot?.documents?.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null
                Document(
                    id = doc.id,
                    name = data["name"]?.toString() ?: "",
                    type = data["type"]?.toString() ?: "",
                    fileUri = data["fileUri"]?.toString() ?: ""
                )
            } ?: emptyList()
            calculateComplianceScore()
        }
    }

    private fun fetchDonations() {
        db.collection("donations").addSnapshotListener { snapshot, _ ->
            val donationList = snapshot?.documents?.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null
                Donation(
                    id = doc.id,
                    amount = data["amount"]?.toString() ?: "R 0",
                    date = data["date"]?.toString() ?: "",
                    project = data["project"]?.toString() ?: "",
                    projectId = data["projectId"]?.toString() ?: "",
                    category = data["category"]?.toString() ?: "Donation",
                    reference = data["reference"]?.toString() ?: ""
                )
            } ?: emptyList()
            
            _donations.value = donationList
            
            // Aggregate total raised across all donations
            _totalRaised.value = donationList.sumOf { 
                it.amount.replace("R", "").replace(",", "").replace(" ", "").toDoubleOrNull() ?: 0.0
            }

            calculateComplianceScore()
        }
    }

    fun addReport(
        title: String,
        description: String,
        targetBudget: Double,
        donationGoal: Double,
        location: String
    ) {
        viewModelScope.launch {
            val ref = db.collection("reports").document()
            val date = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date())
            
            val report = Report(
                id = ref.id,
                title = title,
                description = description,
                targetBudget = targetBudget,
                donationGoal = donationGoal,
                projectLocation = location,
                dateSubmitted = date,
                status = "Active",
                currentSpending = 0.0,
                amountDonated = 0.0,
                beneficiariesReached = "0"
            )
            ref.set(report).await()
        }
    }

    fun addDocument(name: String, type: String, uri: String) {
        viewModelScope.launch {
            val ref = db.collection("documents").document()
            val doc = Document(id = ref.id, name = name, type = type, fileUri = uri)
            ref.set(doc).await()
        }
    }

    fun processDonation(amountStr: String, projectId: String = "", projectName: String = "General Fund") {
        viewModelScope.launch {
            try {
                val amount = amountStr.replace(",", "").toDoubleOrNull() ?: 0.0
                if (amount <= 0) return@launch

                val ref = db.collection("donations").document()
                val donation = Donation(
                    id = ref.id,
                    amount = "R $amountStr",
                    date = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                    project = projectName,
                    projectId = projectId,
                    category = "Individual Donation",
                    reference = "TXN-${System.currentTimeMillis()}"
                )
                ref.set(donation).await()

                if (projectId.isNotEmpty()) {
                    val reportRef = db.collection("reports").document(projectId)
                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(reportRef)
                        if (!snapshot.exists()) return@runTransaction null
                        
                        // Robustly read the current amounts
                        val currentDonated = when (val v = snapshot.get("amountDonated")) {
                            is Number -> v.toDouble()
                            is String -> v.toDoubleOrNull() ?: 0.0
                            else -> 0.0
                        }
                        
                        val currentSpent = when (val v = snapshot.get("currentSpending")) {
                            is Number -> v.toDouble()
                            is String -> v.toDoubleOrNull() ?: 0.0
                            else -> 0.0
                        }

                        // Update both donated amount and simulate some spending (e.g., 10% immediate allocation)
                        val newDonated = currentDonated + amount
                        val newSpent = currentSpent + (amount * 0.1) // Simulate 10% of donation being used immediately
                        
                        transaction.update(reportRef, "amountDonated", newDonated)
                        transaction.update(reportRef, "currentSpending", newSpent)
                        null
                    }.await()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun deleteReport(reportId: String) {
        viewModelScope.launch {
            db.collection("reports").document(reportId).delete().await()
        }
    }
}
