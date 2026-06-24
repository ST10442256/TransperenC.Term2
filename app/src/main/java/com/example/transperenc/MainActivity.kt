package com.example.transperenc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.transperenc.navigation.Screen
import com.example.transperenc.ui.analytics.AnalyticsScreen
import com.example.transperenc.ui.dashboard.DashboardScreen
import com.example.transperenc.ui.documents.DocumentsScreen
import com.example.transperenc.ui.donor.DonorPortalScreen
import com.example.transperenc.ui.login.LoginScreen
import com.example.transperenc.ui.profile.ProfileScreen
import com.example.transperenc.ui.register.RegisterScreen
import com.example.transperenc.ui.reports.ReportsScreen
import com.example.transperenc.ui.settings.SettingsScreen
import com.example.transperenc.ui.theme.TransperenCTheme
import com.example.transperenc.viewmodel.MainViewModel
import com.example.transperenc.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TransperenCTheme {
                MainNavigation(userViewModel, mainViewModel)
            }
        }
    }
}

@Composable
fun MainNavigation(userViewModel: UserViewModel, mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val currentUser by userViewModel.currentUser.collectAsState()
    val isInitialized by userViewModel.isInitialized.collectAsState()

    if (!isInitialized) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            // If we are on Login or Register screens, navigate to Dashboard
            navController.currentDestination?.route?.let { route ->
                if (route.contains("Login") || route.contains("Register")) {
                    navController.navigate(Screen.Dashboard) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) Screen.Dashboard else Screen.Login
    ) {
        composable<Screen.Login> {
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register)
                }
            )
        }
        composable<Screen.Register> {
            RegisterScreen(
                userViewModel = userViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Dashboard> {
            DashboardScreen(
                mainViewModel = mainViewModel,
                onNavigateToReports = { navController.navigate(Screen.Reports) },
                onNavigateToDocuments = { navController.navigate(Screen.Documents) },
                onNavigateToDonorPortal = { navController.navigate(Screen.DonorPortal) },
                onNavigateToSettings = { navController.navigate(Screen.Settings) },
                onNavigateToProfile = { navController.navigate(Screen.Profile) },
                onNavigateToAnalytics = { navController.navigate(Screen.Analytics) }
            )
        }
        composable<Screen.Analytics> {
            AnalyticsScreen(
                mainViewModel = mainViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Profile> {
            ProfileScreen(
                userViewModel = userViewModel,
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Dashboard) { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.Reports> {
            ReportsScreen(
                mainViewModel = mainViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Documents> {
            DocumentsScreen(
                mainViewModel = mainViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<Screen.DonorPortal> {
            DonorPortalScreen(
                mainViewModel = mainViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToReports = { navController.navigate(Screen.Reports) }
            )
        }
        composable<Screen.Settings> {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
