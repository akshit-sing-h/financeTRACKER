package com.example.financetracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.ui.theme.FinanceTRACKERTheme
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseApp.initializeApp(this)

            FinanceTRACKERTheme {
                FinanceTrackerApp()
            }
        }
    }
}

@Composable
fun FinanceTrackerApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "onboarding", // Start at onboarding screen
            modifier = Modifier.padding(innerPadding)
        ) {
            // Onboarding screen composable
            composable("onboarding") {
                OnboardingScreen(onGetStartedClick = {
                    // Navigate to the dashboard after getting started
                    navController.navigate("dashboard") {
                        // Clear the back stack to prevent returning to onboarding screen
                        popUpTo("onboarding") { inclusive = true }
                    }
                })
            }

            // Dashboard screen composable
            composable("dashboard") {
                DashboardScreen(navController) // Pass navController for navigation
            }

            // Transactions list screen composable
            composable("transactionsList") {
                TransactionsListScreen(navController)
            }
        }
    }
}



