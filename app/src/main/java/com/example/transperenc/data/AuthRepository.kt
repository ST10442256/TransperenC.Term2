package com.example.transperenc.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

object AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                fetchUserProfile(firebaseUser.uid)
            } else {
                _currentUser.value = null
                _isInitialized.value = true
            }
        }
    }

    private fun fetchUserProfile(uid: String) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val data = document.data
                if (data != null) {
                    _currentUser.value = User(
                        id = uid,
                        username = data["username"]?.toString() ?: "",
                        email = data["email"]?.toString() ?: "",
                        role = data["role"]?.toString() ?: "Project Manager",
                        organization = data["organization"]?.toString() ?: "Transparency NGO",
                    )
                }
                _isInitialized.value = true
            }
            .addOnFailureListener {
                _isInitialized.value = true
            }
    }

    suspend fun register(username: String, email: String, password: String): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Registration failed")
            val user = User(id = uid, username = username, email = email)
            db.collection("users").document(uid).set(user).await()
            _currentUser.value = user
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
    }

    suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            db.collection("users").document(user.id).set(user).await()
            _currentUser.value = user
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
