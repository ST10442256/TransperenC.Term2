package com.example.transperenc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transperenc.data.AuthRepository
import com.example.transperenc.data.User
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    val currentUser: StateFlow<User?> = AuthRepository.currentUser
    val isInitialized: StateFlow<Boolean> = AuthRepository.isInitialized

    fun register(username: String, email: String, password: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            val result = AuthRepository.register(username, email, password)
            onResult(result)
        }
    }

    fun login(email: String, password: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            val result = AuthRepository.login(email, password)
            onResult(result)
        }
    }

    fun logout() {
        AuthRepository.logout()
    }

    fun updateProfile(username: String, role: String, organization: String) {
        val current = currentUser.value ?: return
        val updated = current.copy(username = username, role = role, organization = organization)
        viewModelScope.launch {
            AuthRepository.updateUserProfile(updated)
        }
    }
}
